# Brulee Telie final validation

> Documento histórico da entrega anterior. A revisão e o APK atuais estão em [fix/REVIEW.md](../fix/REVIEW.md).
Date: 2026-10-19

## Completed

- Adaptive navigation corrected to use a lateral `NavigationRail` on Medium/Expanded widths.
- `Medium_Phone(AVD) - 17`, API 37.2, 1080x2400: accessibility/adaptive smoke tests passed 2/2.
- OCR stress on `Medium_Phone`: 3/3 passed, including 50 sequential runs, 20 large images and 20 cancel/retry cycles.
- PDF stress on `Medium_Phone`: 4/4 passed, including 20/50 pages, corruption, cancellation, repeated cycles and encrypted PDF rejection.
- Release APK contains `assets/dexopt/baseline.prof` and `assets/dexopt/baseline.profm`.
- Macrobenchmark on `Medium_Phone`: 5/5 passed with Perfetto traces for finance, quote, production, shopping and stock journeys.
- Full compact UI suite on `Small_Phone(AVD) - 17`: 252/252 passed, 0 skipped, 0 failed, including OCR lifecycle persistence tests.
- JVM/build regression passed: `test :app:assembleDebug :documents:assembleDebug :app:assembleRelease`.
- `git diff --check` passed.

## Classification

Macrobenchmark values are harness validation from an emulator, not an authoritative physical-device performance baseline.

## External limitations

- Historical Room `identityHash` values for schema versions 1 and 2 cannot be proven without original schema/database/APK/AAB artifacts.
- OCR draft state now survives activity recreation and background/foreground transitions without duplicate persistence; physical camera hardware behavior remains outside emulator certification.
- The Medium runtime proves adaptive navigation rail rendering, but not every tablet list-detail interaction because the current app does not expose a separate list-detail route for every domain screen.
