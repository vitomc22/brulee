"""Atomic ZIP fixtures for the distribution guard; no Android app data is touched."""
from contextlib import closing
import pathlib
import sqlite3
import subprocess
import sys
import tempfile
import unittest
import zipfile

CHECK = pathlib.Path(__file__).with_name("check-apk-data.py")


class ApkDataRegressionTest(unittest.TestCase):
    def check_apk(self, path):
        return subprocess.run([sys.executable, str(CHECK), str(path)], capture_output=True, text=True)

    def test_clean_distribution_is_accepted(self):
        with tempfile.TemporaryDirectory() as directory:
            apk = pathlib.Path(directory) / "clean.apk"
            with zipfile.ZipFile(apk, "w") as archive:
                archive.writestr("classes.dex", b"runtime application code")
            self.assertEqual(0, self.check_apk(apk).returncode)

    def test_an_accidentally_packaged_database_is_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            database = pathlib.Path(directory) / "test.db"
            with closing(sqlite3.connect(database)) as connection:
                connection.execute("CREATE TABLE customer(name TEXT)")
                connection.execute("INSERT INTO customer VALUES (?)", ("Fixture criada pelo teste",))
                connection.commit()
            apk = pathlib.Path(directory) / "contaminated.apk"
            with zipfile.ZipFile(apk, "w") as archive:
                archive.write(database, "assets/brulee_telie.db")
            result = self.check_apk(apk)
            self.assertNotEqual(0, result.returncode)
            self.assertIn("assets/brulee_telie.db", result.stderr)

    def test_test_fixture_code_in_runtime_dex_is_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            apk = pathlib.Path(directory) / "fixtures.apk"
            with zipfile.ZipFile(apk, "w") as archive:
                archive.writestr("classes.dex", b"Lcom/bruleetelie/app/LegacyFixtures;")
            result = self.check_apk(apk)
            self.assertNotEqual(0, result.returncode)
            self.assertIn("Test fixtures", result.stderr)


if __name__ == "__main__":
    unittest.main()
