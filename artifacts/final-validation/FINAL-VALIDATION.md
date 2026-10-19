# Brulee Telie final validation

Date: 2026-10-19

## Completed

- Adaptive navigation corrected to use a lateral `NavigationRail` on Medium/Expanded widths.
- `Medium_Phone(AVD) - 17`, API 37.2, 1080x2400: accessibility/adaptive smoke tests passed 2/2.
- OCR stress on `Medium_Phone`: 3/3 passed, including 50 sequential runs, 20 large images and 20 cancel/retry cycles.
- PDF stress on `Medium_Phone`: 4/4 passed, including 20/50 pages, corruption, cancellation, repeated cycles and encrypted PDF rejection.
- Release APK contains `assets/dexopt/baseline.prof` and `assets/dexopt/baseline.profm`.
- Macrobenchmark on `Medium_Phone`: 5/5 passed with Perfetto traces for finance, quote, production, shopping and stock journeys.
- Full compact UI suite on `Small_Phone(AVD) - 17`: 250/250 passed, 0 skipped, 0 failed.
- JVM/build regression passed: `test :app:assembleDebug :documents:assembleDebug`.
- `git diff --check` passed.

## Classification

Macrobenchmark values are harness validation from an emulator, not an authoritative physical-device performance baseline.

## External limitations

- Historical Room `identityHash` values for schema versions 1 and 2 cannot be proven without original schema/database/APK/AAB artifacts.
- The automated OCR stress suite covers pipeline cancellation/retry; physical camera lifecycle rotation and background/foreground behavior still require a dedicated UI lifecycle test if those product states must be certified separately.
- The Medium runtime proves adaptive navigation rail rendering, but not every tablet list-detail interaction because the current app does not expose a separate list-detail route for every domain screen.
