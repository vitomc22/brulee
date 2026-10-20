#!/usr/bin/env python3
"""Distribution regression: runtime APKs must not ship databases or test fixture code."""
import sys
import zipfile

apk = sys.argv[1]
with zipfile.ZipFile(apk) as archive:
    databases = []
    fixtures = []
    for entry in archive.infolist():
        if entry.is_dir():
            continue
        with archive.open(entry) as stream:
            if stream.read(16) == b"SQLite format 3\x00":
                databases.append(entry.filename)
        if entry.filename.endswith(".dex") and b"LegacyFixtures" in archive.read(entry):
            fixtures.append(entry.filename)
    assert not databases, f"Packaged databases: {databases}"
    assert not fixtures, f"Test fixtures in runtime code: {fixtures}"
print("PASS: APK without packaged databases or LegacyFixtures test code")
