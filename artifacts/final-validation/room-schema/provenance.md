# Room schema provenance

The exported artifacts for schema versions 1 and 2 exist at:

- `data/schemas/com.bruleetelie.data.db.BruleeDatabase/1.json`
- `data/schemas/com.bruleetelie.data.db.BruleeDatabase/2.json`

The repository history contains no earlier versions of these files and no original
database, APK, or AAB artifact from which historical identity hashes could be
recovered. The current exports for versions 1, 2, and 3 carry the same identity
hash, while version 4 carries the post-migration hash. This is consistent with
the current Room schema representation, but it is not historical proof of the
identity hashes emitted by the original application builds.

Status: `RECONSTRUCTED_NOT_HISTORICALLY_VERIFIED`.

Migration behavior is covered by the existing Room migration tests and the final
build validation. No schema hash was changed merely to manufacture provenance.
