# Brulee Telie — Final Completion Plan for the Remaining Android Work

> Scope: only the remaining work listed in the current project status.
>
> Goal: give an implementation agent enough detail to continue from repository inspection through implementation, execution, evidence collection, and final validation **without stopping to ask routine questions**.
>
> The web application is out of scope.

---

# 0. Mission

Finish and validate the remaining Android technical work:

1. Execute and expand Macrobenchmark coverage.
2. Validate Medium/Expanded tablet layouts on a real runtime width.
3. Complete OCR stress tests.
4. Add a real encrypted PDF fixture and test.
5. Recover or classify the provenance of Room schema v1/v2 `identityHash` values.
6. Generate, package, validate, and benchmark Baseline Profiles.

The agent must not stop after adding code. Every section includes an **execution phase** and an **evidence phase**.

The job is only complete when the tests/commands have actually been run where the environment allows it.

---

# 1. Agent Execution Contract

## 1.1 Do not rewrite already completed work

Before modifying anything:

```bash
git status --short
git branch --show-current
git log -1 --oneline
```

Do not:

```text
reset unrelated user changes
delete existing tests just because they are flaky
replace working implementations without evidence
rewrite project architecture
change financial behavior
change Room schemas only to make a test pass
```

Changes must be limited to the six remaining areas.

---

# 2. Repository Discovery

The exact Gradle module names and packages must be discovered from the repository instead of assumed.

Run:

```bash
./gradlew projects
```

Then locate the relevant existing files:

```bash
rg -n "class StartupBenchmark|StartupBenchmark" .
rg -n "MacrobenchmarkRule|measureRepeated|StartupTimingMetric|FrameTimingMetric" .
rg -n "BaselineProfileRule|BaselineProfileGenerator|baselineProfile" .
rg -n "PdfPageRendererStressTest" .
rg -n "PdfRenderer" .
rg -n "identityHash|identity_hash|room_master_table" .
rg -n "\"version\": 1|\"version\": 2" --glob '*.json' .
rg -n "NavigationSuiteScaffold|NavigationRail|ListDetail|WindowSizeClass|currentWindowAdaptiveInfo" .
rg -n "TextRecognition|TextRecognizer|Ocr|OCR" app */src 2>/dev/null
```

Find existing connected-test modules:

```bash
find . -maxdepth 3 -type d \
  \( -name androidTest -o -name benchmark -o -name macrobenchmark -o -name baselineprofile -o -name baseline-profile \)
```

Find the application ID:

```bash
rg -n "applicationId\s*=|applicationId " --glob 'build.gradle*'
```

Find benchmark-related Gradle tasks:

```bash
./gradlew tasks --all | grep -iE "benchmark|baseline|connected"
```

Record the discovered values in temporary shell variables or notes:

```text
APP_MODULE
APP_ID
BENCHMARK_MODULE
BENCHMARK_PACKAGE
CURRENT_DB_VERSION
SCHEMA_DIRECTORY
OCR_TEST_PACKAGE
```

Do not proceed with guessed module names if discovery gives real names.

---

# 3. Create a Completion Evidence Directory

Create a repository-local evidence directory that is not used as application runtime data:

```text
artifacts/final-validation/
├── benchmark/
├── baseline-profile/
├── tablet/
├── ocr-stress/
├── pdf/
└── room-schema/
```

If `artifacts/` is intentionally ignored by Git, keep it ignored.

The final agent report must list the evidence produced in this directory.

---

# 4. Workstream A — Macrobenchmark Execution

## Objective

Actually run Macrobenchmark and obtain measurements for:

```text
startup
recipe
quote
production
finance
OCR
```

`StartupBenchmark.kt` already exists, so first inspect it before creating a duplicate benchmark class.

---

# 5. Macrobenchmark Device Preflight

## 5.1 Detect connected devices

Run:

```bash
adb devices -l
```

The requested device is:

```text
Small_Phone
```

Determine the actual serial.

If multiple devices are connected, set:

```bash
export ANDROID_SERIAL=<serial>
```

Verify:

```bash
adb -s "$ANDROID_SERIAL" get-state
```

---

# 6. Determine Whether `Small_Phone` Is Physical or Emulated

Run:

```bash
adb -s "$ANDROID_SERIAL" shell getprop ro.kernel.qemu
adb -s "$ANDROID_SERIAL" shell getprop ro.product.model
adb -s "$ANDROID_SERIAL" shell getprop ro.build.version.sdk
adb -s "$ANDROID_SERIAL" shell getprop ro.build.version.release
```

Interpretation:

```text
ro.kernel.qemu = 1
→ emulator

empty/0
→ usually physical device
```

Save output:

```bash
{
  echo "=== device ==="
  adb -s "$ANDROID_SERIAL" shell getprop ro.product.model
  echo "=== api ==="
  adb -s "$ANDROID_SERIAL" shell getprop ro.build.version.sdk
  echo "=== qemu ==="
  adb -s "$ANDROID_SERIAL" shell getprop ro.kernel.qemu
} > artifacts/final-validation/benchmark/device.txt
```

---

# 7. Benchmark Authority Rule

If `Small_Phone` is an emulator:

```text
run all benchmarks anyway
```

but classify them as:

```text
HARNESS VALIDATION / NON-AUTHORITATIVE PERFORMANCE DATA
```

Do not use emulator timings as the product performance baseline.

If a physical device is connected:

```text
use its measurements as the authoritative local baseline
```

Prefer API 34+ for benchmark execution when possible.

Do not suppress Macrobenchmark configuration errors merely to obtain numbers.

---

# 8. Device Stability Preflight

Before an authoritative physical-device run:

Check battery:

```bash
adb -s "$ANDROID_SERIAL" shell dumpsys battery
```

Avoid benchmarking while:

```text
device is thermally throttled
battery is critically low
Play Store/app updates are running
large downloads are running
screen recording is active
Android Studio profiler is attached
```

Record thermal state if available:

```bash
adb -s "$ANDROID_SERIAL" shell dumpsys thermalservice \
  > artifacts/final-validation/benchmark/thermal-before.txt || true
```

---

# 9. Validate Benchmark Build Configuration

Inspect the benchmark module and target app build type.

The benchmarked target should be as close to release as possible:

```text
non-debuggable
profileable
release-like optimization
release-like R8 configuration when already configured
debug signing allowed for local benchmark installation
```

Find:

```bash
rg -n "create\(\"benchmark\"\)|benchmark\s*\{|profileable|isDebuggable|debuggable" \
  --glob 'build.gradle*' --glob 'AndroidManifest.xml'
```

Do not convert the release build itself into a debug build.

If a `benchmark` build type exists, retain it.

If it does not exist and Macrobenchmark cannot run correctly, create one by inheriting release settings.

Conceptually:

```kotlin
create("benchmark") {
    initWith(getByName("release"))
    signingConfig = signingConfigs.getByName("debug")
    matchingFallbacks += listOf("release")
}
```

Only add this if the existing project does not already provide the equivalent.

---

# 10. First Macrobenchmark Run — Existing Startup Test

Before adding new CUJs, prove that the existing harness works.

Determine the benchmark module discovered earlier.

Then run all existing macrobenchmarks:

```bash
./gradlew :<BENCHMARK_MODULE>:connectedCheck
```

If the project exposes `connectedAndroidTest` instead:

```bash
./gradlew :<BENCHMARK_MODULE>:connectedAndroidTest
```

Do not guess. Use the task discovered from:

```bash
./gradlew :<BENCHMARK_MODULE>:tasks --all
```

---

# 11. Run `StartupBenchmark.kt` in Isolation

Find the fully qualified class name:

```bash
rg -n "package .*|class StartupBenchmark" <path-to-StartupBenchmark.kt>
```

Then run only it:

```bash
./gradlew :<BENCHMARK_MODULE>:connectedCheck \
  -Pandroid.testInstrumentationRunnerArguments.class=<FQCN_OF_STARTUP_BENCHMARK>
```

Capture output:

```bash
./gradlew :<BENCHMARK_MODULE>:connectedCheck \
  -Pandroid.testInstrumentationRunnerArguments.class=<FQCN_OF_STARTUP_BENCHMARK> \
  | tee artifacts/final-validation/benchmark/startup-console.txt
```

---

# 12. Locate Macrobenchmark Output

After execution, inspect:

```bash
find <BENCHMARK_MODULE>/build/outputs \
  -type f \( -name '*.json' -o -name '*.perfetto-trace' \) \
  -print
```

Expected general output area:

```text
<benchmark-module>/
  build/
    outputs/
      connected_android_test_additional_output/
```

Copy or index relevant result files under:

```text
artifacts/final-validation/benchmark/
```

Do not delete original Gradle outputs.

---

# 13. Benchmark Metrics to Use

## Startup

Use:

```text
StartupTimingMetric
```

Primary values to report:

```text
timeToInitialDisplayMs
timeToFullDisplayMs, if the app reports fully drawn
median
min
max
```

## UI journeys

Use:

```text
FrameTimingMetric
```

Report at least:

```text
frame-duration/jank-related metrics exposed by the current library version
iteration timing
```

If existing benchmark code already uses equivalent supported metrics, preserve it rather than rewriting just for naming consistency.

---

# 14. Do Not Put Setup Inside the Measured Journey

Every mutable flow must start from deterministic data.

The measured block should contain:

```text
navigation
rendering
scrolling
interaction being measured
```

The setup block should contain:

```text
return to known state
seed/reset benchmark data
dismiss first-run UI if necessary
prepare required records
```

Do not measure:

```text
database seeding
test fixture copying
unrelated initial setup
```

unless that operation is itself the performance target.

---

# 15. Create Shared Critical User Journey Helpers

If benchmarks and Baseline Profile generation currently duplicate UI navigation, create a helper in the benchmark/baseline module.

Example logical file:

```text
CriticalUserJourneys.kt
```

Do not create the file if an equivalent abstraction already exists.

Suggested functions:

```kotlin
fun MacrobenchmarkScope.openRecipeJourney(device: UiDevice)
fun MacrobenchmarkScope.createQuoteJourney(device: UiDevice)
fun MacrobenchmarkScope.productionJourney(device: UiDevice)
fun MacrobenchmarkScope.financeJourney(device: UiDevice)
fun MacrobenchmarkScope.ocrJourney(device: UiDevice)
```

Or a UI driver:

```kotlin
class BruleeUiDriver(
    private val device: UiDevice
)
```

The same journey helpers should be usable by:

```text
Macrobenchmark tests
BaselineProfileGenerator
```

---

# 16. Stable UI Selectors

Do not build long benchmark flows using fragile pixel coordinates.

Prefer:

```text
resource IDs
stable content descriptions
stable visible labels
stable semantic identifiers exposed to UiAutomator
```

Avoid selectors based on:

```text
list index only
dynamic price text
animation timing
screen coordinates
```

If a required element is impossible to address reliably, add the minimum stable semantic identifier necessary.

---

# 17. Benchmark Journey — Recipe

## Goal

Measure:

```text
Home
→ Recipes
→ recipe list rendered
→ open representative recipe
→ render detail
→ scroll ingredients/cost content
```

### Dataset

Use deterministic data:

```text
Bolo Brulee Benchmark
10–20 ingredients or enough content to exercise lazy lists
historical cost information if shown
```

### Measured block

Do not create the recipe during measurement.

Measure:

```text
tap Recipes
wait for list
tap benchmark recipe
wait for detail
scroll content
```

### Acceptance

```text
every iteration completes
correct recipe opens
FrameTiming metrics generated
Perfetto traces generated
```

Suggested initial iterations:

```text
5
```

Use 10 later for a stable physical baseline if runtime is reasonable.

---

# 18. Benchmark Journey — Quote

Prepare a deterministic draft or benchmark-specific data state.

Measured flow should exercise:

```text
New Quote
→ recipe selection/resume
→ ingredient step
→ operational costs
→ cost recalculation
→ summary
→ suggested price
```

If typing every field through UI is too brittle, seed a draft in setup and measure:

```text
resume quote
→ edit quantity
→ recalculate
→ summary
→ pricing
```

The measured code path must still use the real:

```text
ViewModel
use cases
cost engine
repositories
Room reads
Compose UI
```

Do not replace production calculations with benchmark fakes.

---

# 19. Benchmark Journey — Production

Use a pre-seeded approved order.

Measured flow:

```text
open Production
→ render list
→ select benchmark order
→ render detail
→ start production
→ open finish/review screen
```

Reset the order in `setupBlock` so every iteration starts from the same state.

---

# 20. Benchmark Journey — Finance

Seed:

```text
at least 6 months
multiple sales per month
positive/negative outcomes if supported
ingredient price history
estimated/actual deviations
```

Measured flow:

```text
open Finance
→ wait for KPI cards
→ wait for charts
→ scroll ranking/profitability section
→ change one period filter if stable
```

Use the real database/query path.

---

# 21. Benchmark Journey — OCR

The performance benchmark must be deterministic.

Do not depend on photographing a physical receipt each iteration.

Preferred path:

```text
benchmark-only fixture launcher
→ production OCR use case/gateway
→ deterministic image fixture
→ real ML Kit OCR
→ parser
→ review screen
```

Do not mock the recognizer for this benchmark.

The camera/scanner itself remains covered by functional tests.

Measured OCR journey:

```text
fixture import
→ real OCR
→ parse
→ review rendered
```

Do not confirm financial persistence.

---

# 22. Benchmark-Only Data Seed Mechanism

If current benchmark setup cannot reset data, implement a benchmark-only mechanism.

Requirements:

```text
available only in benchmark build
absent from release
idempotent
uses production repositories/use cases where practical
can reset benchmark records
```

Possible forms:

```text
benchmark-only Activity
benchmark-only BroadcastReceiver
benchmark-only deep link
benchmark-only ContentProvider
```

Support conceptual commands:

```text
RESET_BENCHMARK_DATA
SEED_RECIPE
SEED_QUOTE
SEED_PRODUCTION
SEED_FINANCE
```

---

# 23. Macrobenchmark Result Summary

Create:

```text
artifacts/final-validation/benchmark/summary.md
```

Record:

```text
device model
serial
physical/emulator
API
benchmark variant
git commit
journey
iterations
metrics
median/min/max
result paths
```

If run on emulator, state:

```text
Performance numbers are non-authoritative; harness execution is validated.
```

---

# 24. Macrobenchmark Exit Gate

- [ ] Existing `StartupBenchmark.kt` runs successfully.
- [ ] Recipe benchmark exists and runs.
- [ ] Quote benchmark exists and runs.
- [ ] Production benchmark exists and runs.
- [ ] Finance benchmark exists and runs.
- [ ] OCR benchmark exists and runs.
- [ ] JSON/traces are generated.
- [ ] Summary is written.
- [ ] Device authority is documented.
- [ ] No benchmark errors are hidden with blanket suppressions.

---

# 25. Workstream B — Tablet / Adaptive Layout Validation

## Objective

Validate adaptive navigation at actual runtime widths.

Target:

```text
Medium: 600dp <= width < 840dp
Expanded: 840dp <= width < 1200dp
```

Do not rely only on Compose Preview.

---

# 26. Determine Current Runtime Width

Run:

```bash
adb -s "$ANDROID_SERIAL" shell wm size
adb -s "$ANDROID_SERIAL" shell wm density
```

Compute:

```text
widthDp = widthPx * 160 / densityDpi
```

---

# 27. Validation Layers

Use both:

## Layer A — Instrumented adaptive tests

Validate:

```text
Medium behavior
Expanded behavior
rotation
selection continuity
```

## Layer B — Actual runtime width

Run the app at widths whose WindowMetrics resolve to Medium and Expanded.

---

# 28. Emulator Resize Safety

Only use `wm` overrides on an emulator.

Record originals:

```bash
adb shell wm size > artifacts/final-validation/tablet/wm-size-original.txt
adb shell wm density > artifacts/final-validation/tablet/wm-density-original.txt
```

If `Small_Phone` is physical, use a tablet/resizable AVD instead.

---

# 29. Medium Runtime

Target around:

```text
700dp
```

Calculate target pixels using current density:

```text
targetPx = 700 * density / 160
```

Apply the size on the emulator and relaunch the app.

Verify behavior consistent with Medium.

---

# 30. Expanded Runtime

Target around:

```text
1000dp
```

Calculate target pixels using current density.

Apply width, relaunch, and validate Expanded behavior.

---

# 31. Adaptive Navigation

At sizes where the design expects a navigation rail:

```text
rail visible
bottom bar not duplicated
selected destination correct
labels/icons usable
```

Do not force a rail if the implementation's documented adaptive policy intentionally selects another navigation form.

---

# 32. List-Detail Validation

Validate natural list/detail screens:

```text
Recipes
Production
Ingredients/price history if implemented this way
OCR review if split-pane is implemented
```

Expanded expectations:

```text
list + detail visible simultaneously
selection remains clear
correct detail entity
no redundant mobile-only back arrow in side-by-side detail
```

Medium behavior must match the selected Material adaptive strategy.

Standard Material adaptive list-detail often shows one pane at a time at Medium widths; do not treat that as a defect automatically.

---

# 33. Recipe Expanded Test

1. Open Recipes.
2. Verify list.
3. Select recipe A.
4. Verify detail A beside list.
5. Select recipe B.
6. Verify detail B.
7. Verify list remains visible.
8. Rotate.
9. Verify selected recipe preserved.
10. Rotate back.
11. Verify two-pane state restores.

Capture:

```text
recipe-expanded-landscape.png
recipe-expanded-rotated.png
```

---

# 34. Production Expanded Test

1. Seed at least three production orders.
2. Open Production.
3. Verify list pane.
4. Select one order.
5. Verify detail pane.
6. Change status only on a safe test order.
7. Verify list updates without losing selection.
8. Rotate.
9. Verify order identity/state remains correct.

---

# 35. Medium Width Test

At approximately 700dp:

1. Open a list screen.
2. Select an item.
3. Verify intended detail transition.
4. Press Back.
5. Verify return to list.
6. Rotate.
7. Re-evaluate window class.
8. Verify state remains intact.

---

# 36. Navigation Rotation Test

1. Start in width using adaptive rail/navigation.
2. Select Finance.
3. Rotate or resize narrower.
4. Verify navigation adapts.
5. Finance remains selected.
6. Rotate/resize back.
7. Rail/layout returns.
8. No duplicate back-stack destination appears.

---

# 37. Tablet Evidence

Create:

```text
artifacts/final-validation/tablet/
├── medium-portrait.png
├── medium-detail.png
├── expanded-list-detail.png
├── expanded-rotated.png
├── navigation-rail.png
└── tablet-validation.md
```

Record:

```text
device/AVD
widthPx
density
calculated widthDp
window class
screen
expected
actual
pass/fail
```

---

# 38. Restore Emulator Configuration

If overridden:

```bash
adb shell wm size reset
adb shell wm density reset
```

Verify reset afterward.

---

# 39. Tablet Exit Gate

- [ ] Medium tested at runtime.
- [ ] Expanded tested at runtime.
- [ ] Adaptive navigation verified.
- [ ] List-detail verified.
- [ ] Rotation verified.
- [ ] Selection/state continuity verified.
- [ ] Compact regression verified.
- [ ] Evidence captured.
- [ ] Emulator display overrides restored.

---

# 40. Workstream C — OCR Stress Test Completion

## Objective

Complete:

```text
50 sequential OCR operations
20 large images
repeated cancel/retry
rotation
background/foreground
```

Use the real OCR implementation.

---

# 41. Discover Existing OCR Test Infrastructure

Run:

```bash
rg -n "OcrStress|OCR.*Stress|TextRecognitionGateway|MlKit.*Text|TextRecognizer"   --glob '*.kt'
```

Reuse existing:

```text
fixture loader
gateway
parser
test DB
Hilt test bindings
coroutine helpers
```

---

# 42. Split Stress Coverage

## Pipeline stress

```text
50 sequential
20 large images
cancel/retry
resource stability
```

## UI lifecycle stress

```text
rotation
background/foreground
review-state continuity
duplicate-persistence prevention
```

---

# 43. Fixture Policy

Use:

```text
anonymized fixtures
synthetic receipts
generated test images
```

Do not commit real customer receipts.

---

# 44. 50 Sequential OCR Test

Suggested name:

```text
ocr50SequentialRuns_completesWithoutLeakOrFailure
```

Flow:

1. Warm up with 3–5 OCR runs.
2. Record baseline diagnostics.
3. Execute real OCR 50 times sequentially.
4. Per run:
   - load/create one image;
   - run recognizer;
   - await result;
   - assert expected marker/key text;
   - run parser if included;
   - do not persist financial data;
   - release temporary image resources.
5. Record iteration failures.
6. Record final diagnostics.

Assertions:

```text
50/50 complete
0 crash
0 OOM
0 ANR
0 duplicate financial commit
valid fixtures return expected marker
```

Do not invent a hard memory threshold if none exists.

Fail automatically on:

```text
OOM
process death
resource growth that causes failure
```

---

# 45. OCR Stress Diagnostics

Record per iteration where practical:

```text
iteration
fixture
durationMs
success
recognizedElementCount
parserItemCount
exception
```

Optionally emit:

```text
ocr-sequential-stress.json
```

---

# 46. 20 Large Images Test

Suggested:

```text
ocr20LargeImages_completesWithoutOom
```

Representative size:

```text
~3000x4000
~3072x4096
```

Never allocate all 20 simultaneously.

Pattern:

```text
load/generate one
→ OCR
→ assert marker
→ release
→ next
```

Synthetic images may contain:

```text
BRULEE TEST
ITEM CHOCOLATE
TOTAL 123,45
```

Vary text position slightly per image.

---

# 47. Large Image Resource Rule

Never:

```kotlin
val images = List(20) { hugeBitmap() }
```

Process incrementally.

The test itself must not cause an artificial OOM unrelated to the application.

---

# 48. Cancel / Retry Stress

Suggested:

```text
ocrRepeatedCancelRetry_doesNotDuplicateState
```

Run at least:

```text
20 cycles
```

Cycle:

1. Start OCR.
2. Cancel.
3. Return.
4. Retry fixture.
5. Allow controlled attempts to complete.
6. Avoid confirm unless test explicitly checks final persistence.

Assertions:

```text
no duplicate DocumentImport
no duplicate Purchase
no duplicate PriceObservation
no stuck Loading
retry succeeds
cancelled result never commits financial data
```

Even if the underlying ML Kit task continues, the app must ignore cancelled results for persistence.

---

# 49. Rotation Stress

Suggested:

```text
ocrReviewRepeatedRotation_preservesDraftAndNeverCommitsAutomatically
```

1. Import fixture.
2. Reach review.
3. Manually edit one value.
4. Rotate portrait→landscape.
5. Verify review state.
6. Rotate back.
7. Repeat several times.
8. Leave without confirm.
9. Inspect DB.

Assertions:

```text
state coherent
no duplicate candidates
manual edit follows ViewModel persistence policy
no Purchase/PriceObservation committed without confirm
```

---

# 50. Background / Foreground Stress

Suggested:

```text
ocrBackgroundForeground_doesNotDuplicateProcessingOrPersistence
```

1. Start OCR.
2. Press Home.
3. Wait briefly.
4. Return.
5. Repeat 5–10 times around processing/review.
6. Confirm once only if persistence is in scope.

Assertions:

```text
one review session
at most one confirmed import
no duplicate Purchase
no duplicate PriceObservation
no crash
no stale fixture shown
```

---

# 51. Failure Diagnostics

Capture:

```text
fixture
iteration
exception
logcat
current screen
record counts
memory diagnostics if relevant
```

Example:

```bash
adb logcat -c
# run tests
adb logcat -d > artifacts/final-validation/ocr-stress/logcat.txt
```

---

# 52. Execute OCR Stress

Run targeted class first:

```bash
./gradlew :<APP_MODULE>:connectedAndroidTest   -Pandroid.testInstrumentationRunnerArguments.class=<FQCN_OCR_STRESS_TEST>
```

Then broader OCR package/suite using actual available tasks.

---

# 53. OCR Stress Evidence

Create:

```text
artifacts/final-validation/ocr-stress/summary.md
```

Record:

```text
50 sequential result
20 large-image result
cancel/retry cycles
rotation cycles
background/foreground cycles
device/API
duration
memory observations
failures
```

---

# 54. OCR Stress Exit Gate

- [ ] 50 sequential test implemented and run.
- [ ] 50/50 completes.
- [ ] 20 large-image test implemented and run.
- [ ] No OOM.
- [ ] Cancel/retry stress implemented and run.
- [ ] Rotation stress implemented and run.
- [ ] Background/foreground stress implemented and run.
- [ ] Cancelled/unconfirmed OCR never becomes financial data.
- [ ] Logs/evidence stored.

---

# 55. Workstream D — Real Encrypted PDF Fixture

## Objective

Add a real password-encrypted PDF fixture and validate graceful handling.

`Android PdfDocument` cannot produce the required encrypted fixture.

Generate externally once and commit the binary fixture.

---

# 56. Source PDF

Reuse a small synthetic PDF fixture.

If none exists, create one page containing:

```text
BRULEE TEST RECEIPT
ITEM TEST
TOTAL R$ 123,45
```

Suggested paths, adapted to actual module:

```text
app/src/androidTest/assets/pdf/plain_receipt.pdf
app/src/androidTest/assets/pdf/encrypted_receipt.pdf
```

---

# 57. Generate Encrypted Fixture with qpdf

Check:

```bash
qpdf --version
```

On Linux if missing and installation is permitted:

```bash
sudo apt-get install qpdf
```

Generate:

```bash
qpdf   --encrypt "brulee-test" "brulee-owner-test" 256   --   plain_receipt.pdf   encrypted_receipt.pdf
```

Passwords are test-only.

---

# 58. Verify Encryption

Run:

```bash
qpdf --show-encryption encrypted_receipt.pdf
```

Save:

```bash
qpdf --show-encryption encrypted_receipt.pdf   > artifacts/final-validation/pdf/encrypted-fixture-info.txt
```

Hash:

```bash
sha256sum encrypted_receipt.pdf   > artifacts/final-validation/pdf/encrypted-fixture.sha256
```

---

# 59. Fixture Generation Script

If repository conventions allow, add:

```text
tools/test-fixtures/generate-encrypted-pdf.sh
```

It should:

```text
take source fixture
run qpdf
verify encryption
print SHA-256
```

Normal Android tests must not require qpdf at runtime.

Commit the encrypted binary fixture.

---

# 60. Add Test to `PdfPageRendererStressTest.kt`

Suggested:

```text
encryptedPdf_isRejectedAsPasswordProtected_withoutCrash
```

Flow:

1. Load fixture.
2. Open through production PDF import/renderer boundary.
3. Expect domain result:
   - PasswordRequired, or
   - EncryptedPdfUnsupported, or equivalent.
4. Assert:
   - no pages returned;
   - OCR not launched;
   - no financial data persisted;
   - resources closed.

Do not assert raw platform exception at the UI boundary.

If current code leaks `SecurityException`, translate it at the PDF import boundary.

Conceptual result:

```kotlin
sealed interface PdfOpenResult {
    data class Success(...) : PdfOpenResult
    data object PasswordRequired : PdfOpenResult
    data class InvalidDocument(...) : PdfOpenResult
}
```

Reuse existing error abstractions when present.

---

# 61. API 35 Scope Decision

API 35 supports loading parameters including passwords in newer `PdfRenderer` APIs.

Do not expand product scope to password-entry UI unless already required.

Current required behavior:

```text
encrypted PDF
→ controlled rejection
→ clear message
→ no crash
```

---

# 62. Regression After Encrypted PDF

Add:

```text
encryptedPdfFailure_doesNotBreakNextNormalPdf
```

Flow:

1. Attempt encrypted PDF.
2. Receive controlled error.
3. Immediately import normal PDF.
4. Render page 0.
5. Assert success.

---

# 63. Encrypted PDF UI Path

If the importer exposes a user-facing error:

1. Open encrypted fixture.
2. Verify password-protected/unsupported error state.
3. Verify choose-another-file/retry remains functional.
4. Import normal PDF.
5. Verify OCR/review proceeds.

Prefer semantic error IDs over exact localized wording.

---

# 64. Encrypted PDF Exit Gate

- [ ] Real encrypted fixture created.
- [ ] Encryption externally verified.
- [ ] SHA-256 recorded.
- [ ] Fixture contains no sensitive data.
- [ ] Stress test added.
- [ ] Domain/UI error is controlled.
- [ ] Normal PDF works after encrypted failure.
- [ ] Tests executed.

---

# 65. Workstream E — Room Schema v1/v2 `identityHash` Provenance

## Objective

Determine whether current `1.json` and `2.json` `identityHash` values are the original values used by historical app versions.

This is a provenance task.

Do not invent or regenerate historical evidence.

---

# 66. Inspect Current Schema JSON

Locate:

```bash
find . \( -path '*schemas*' -a -name '1.json' \) -o        \( -path '*schemas*' -a -name '2.json' \)
```

Inspect:

```bash
jq '.database.version, .database.identityHash' <path>/1.json
jq '.database.version, .database.identityHash' <path>/2.json
```

Fallback:

```bash
grep -n "identityHash\|version" <path>/1.json
grep -n "identityHash\|version" <path>/2.json
```

Record:

```text
v1 current hash
v2 current hash
Git commit that introduced each file
whether generated at the time or later reconstructed, if evidence exists
```

---

# 67. Determine Current Room Database

Locate:

```bash
rg -n "@Database" --glob '*.kt'
```

Record:

```text
database class
current version
entities
exportSchema
schema directory config
```

Confirm migration tests reference exported schemas.

---

# 68. Provenance Source Priority

Search:

```text
1. original Git schema JSON
2. real historical database
3. historical APK generated Room implementation
4. historical AAB
5. release/CI artifact
6. current reconstructed JSON only
```

Level 6 alone does not prove historical identity.

---

# 69. Git History Recovery

For schema paths:

```bash
git log --all --follow -- <schema-path>/1.json
git log --all --follow -- <schema-path>/2.json
```

Inspect commits:

```bash
git show <commit>:<schema-path>/1.json
git show <commit>:<schema-path>/2.json
```

Search related history:

```bash
git log --all --name-status -- '*1.json' '*2.json'
git log --all -S '@Database' -- '*.kt'
git log --all -S 'version = 1' -- '*.kt'
git log --all -S 'version = 2' -- '*.kt'
git tag --list
```

Inspect relevant tags if present.

---

# 70. Historical Database Recovery

Search:

```bash
find . -type f \( -name '*.db' -o -name '*.sqlite' -o -name '*.sqlite3' \)
```

Never modify the only historical copy.

Copy:

```bash
cp historical.db /tmp/brulee-room-hash-check.db
```

Inspect:

```bash
sqlite3 /tmp/brulee-room-hash-check.db   'PRAGMA user_version;'

sqlite3 /tmp/brulee-room-hash-check.db   'SELECT id, identity_hash FROM room_master_table;'
```

Record version/hash/source.

A DB produced by the historical app is strong evidence.

---

# 71. Historical APK Recovery

Search:

```bash
find . -type f -name '*.apk'
find . -type f -name '*.aab'
```

If old APK exists:

```bash
rm -rf /tmp/brulee-apk-old
jadx -d /tmp/brulee-apk-old old.apk
```

Search:

```bash
rg -n "room_master_table|identityHash|identity_hash" /tmp/brulee-apk-old
rg -n "[a-f0-9]{32,64}" /tmp/brulee-apk-old/sources
```

Locate generated:

```text
BruleeDatabase_Impl or equivalent
RoomOpenHelper / RoomOpenDelegate construction
```

Record a hash only if its context clearly belongs to the database schema.

---

# 72. Historical AAB Recovery

If only AAB exists:

```text
use existing bundletool
build/extract universal APK
decompile APK
search generated Room code
```

Do not install unknown old artifacts on a production personal device.

---

# 73. CI / Release Artifact Recovery

Inspect project pipelines:

```bash
rg -n "artifact|apk|aab|release" .github .gitlab-ci.yml Jenkinsfile 2>/dev/null
```

Retrieve historical artifacts only if authentication/workflow is already available.

Do not block all independent work if remote history is inaccessible.

---

# 74. Hash Classification

Assign one status per schema version.

## VERIFIED_STRONG

Two independent original sources agree.

Examples:

```text
historical JSON + real old DB
old DB + historical APK generated code
```

## VERIFIED_SINGLE_SOURCE

One trustworthy original source confirms it.

## RECONSTRUCTED_NOT_HISTORICALLY_VERIFIED

Current schema exists, but no original artifact proves the hash.

## NOT_APPLICABLE_PRE_RELEASE

Use only when repository/release evidence proves that version was never distributed to real users.

---

# 75. Never “Fix” an Unknown Historical Hash

Do not:

```text
edit JSON until MigrationTestHelper passes
generate a new value and call it historical
claim compatibility from reconstructed JSON alone
```

If unverifiable:

1. Keep migration tests.
2. Document limitation.
3. Determine whether users could possess that DB version.
4. If yes, mark release risk.
5. If no, document pre-release status and freeze current schemas going forward.

---

# 76. Create Provenance Documentation

Create:

```text
docs/database/room-schema-provenance.md
```

Template:

```markdown
# Room Schema Provenance

| Version | JSON hash | Original JSON | Old DB | Old APK/AAB | Status |
|---|---|---|---|---|---|
| 1 | ... | ... | ... | ... | ... |
| 2 | ... | ... | ... | ... | ... |

## Evidence
...

## Limitations
...

## Migration Risk
...
```

---

# 77. Save Evidence

Create:

```text
artifacts/final-validation/room-schema/
├── v1-current-json.txt
├── v2-current-json.txt
├── git-history.txt
├── old-db-hashes.txt
├── old-apk-hashes.txt
└── conclusion.md
```

Only include non-sensitive metadata/hashes.

---

# 78. Migration Test Interpretation

Keep `MigrationTestHelper`.

It validates migration behavior against exported schemas.

However:

```text
if v1/v2 JSON was reconstructed later,
passing MigrationTestHelper does not independently prove
that the JSON exactly represents a DB shipped historically.
```

Document this distinction.

---

# 79. Room Hash Exit Gate

- [ ] Current v1 hash recorded.
- [ ] Current v2 hash recorded.
- [ ] Git history searched.
- [ ] Historical DB searched.
- [ ] APK/AAB searched.
- [ ] Generated Room code inspected when possible.
- [ ] v1 classified.
- [ ] v2 classified.
- [ ] No hash fabricated.
- [ ] Provenance document created.
- [ ] Release risk documented if needed.

---

# 80. Workstream F — Baseline Profile Generation and Validation

## Objective

Generate a real Baseline Profile covering critical journeys, package it in release, and measure it.

Reuse the Macrobenchmark CUJ driver.

---

# 81. Inspect Current Setup

Run:

```bash
rg -n "androidx.baselineprofile|baselineProfile|BaselineProfileRule|BaselineProfileGenerator"   --glob 'build.gradle*' --glob '*.kt' .
```

Identify:

```text
generator module
target app
Gradle plugin
baselineProfile dependency
generator test
existing generated profile
```

List tasks:

```bash
./gradlew :<APP_MODULE>:tasks --all | grep -i baseline
```

---

# 82. Baseline Profile Module Requirements

If using modern plugin setup, conceptually the generator module should have:

```kotlin
plugins {
    id("com.android.test")
    id("androidx.baselineprofile")
}

android {
    targetProjectPath = ":app"
}
```

App conceptually:

```kotlin
plugins {
    id("androidx.baselineprofile")
}

dependencies {
    baselineProfile(project(":<baseline-module>"))
}
```

Do not duplicate configuration already present.

---

# 83. Generator Journeys

Include:

```text
startup
recipe
quote
production
finance
OCR
```

Reuse `CriticalUserJourneys`.

Do not leave generator as startup-only.

---

# 84. Startup Profile Boundary

Startup path should cover:

```text
cold launch
home composition
initial nav
minimum async state required for interaction
```

Recipe/quote/production/finance/OCR should contribute to broader Baseline Profile coverage without being incorrectly treated as startup-only rules.

---

# 85. Generate Profile

Discover exact task:

```bash
./gradlew :<APP_MODULE>:tasks --all | grep -i "generate.*BaselineProfile"
```

Typical task:

```bash
./gradlew :<APP_MODULE>:generateBaselineProfile
```

or variant-specific equivalent.

Capture:

```bash
./gradlew :<APP_MODULE>:generateBaselineProfile   | tee artifacts/final-validation/baseline-profile/generation-console.txt
```

---

# 86. Generation Device

Record:

```bash
adb shell getprop ro.build.version.sdk
adb shell getprop ro.kernel.qemu
```

Use existing connected/GMD configuration.

Do not add insecure rooting steps merely because old tutorials used them.

---

# 87. Verify Generated Text Profile

Find:

```bash
find <APP_MODULE>/src   -path '*generated/baselineProfiles*'   -type f -print
```

Expected shape:

```text
app/src/<variant>/generated/baselineProfiles/baseline-prof.txt
```

Check:

```bash
wc -l <baseline-prof.txt>
wc -c <baseline-prof.txt>
head -n 20 <baseline-prof.txt>
```

Acceptance:

```text
exists
non-empty
contains production app rules
not only benchmark classes
```

Hash:

```bash
sha256sum <baseline-prof.txt>   > artifacts/final-validation/baseline-profile/baseline-prof.sha256
```

---

# 88. Verify Profile in Release APK

Build:

```bash
./gradlew :<APP_MODULE>:assembleRelease
```

Find:

```bash
find <APP_MODULE>/build/outputs -type f -name '*release*.apk' -print
```

Inspect:

```bash
unzip -l <release.apk>   | grep -E 'assets/dexopt/baseline\.prof|assets/dexopt/baseline\.profm'
```

At minimum the release APK should contain:

```text
assets/dexopt/baseline.prof
```

Save output.

---

# 89. Verify Profile in AAB

If AAB is produced:

```bash
./gradlew :<APP_MODULE>:bundleRelease
```

Inspect:

```bash
unzip -l <release.aab>   | grep 'BUNDLE-METADATA/com.android.tools.build.profiles/baseline.prof'
```

Save evidence.

---

# 90. Check Binary Profile Size

For APK:

```bash
unzip -l <release.apk> | grep 'assets/dexopt/baseline.prof'
```

Record size.

If unexpectedly large, inspect overly broad CUJs/rules rather than deleting important paths blindly.

---

# 91. Validate Device Compilation State

After installing release-like build, inspect:

```bash
adb shell dumpsys package dexopt | grep -A 5 "<APP_ID>"
```

Save:

```bash
adb shell dumpsys package dexopt | grep -A 5 "<APP_ID>"   > artifacts/final-validation/baseline-profile/dexopt.txt
```

Look for profile-guided compilation evidence such as:

```text
speed-profile
```

Packaging verification remains mandatory even if device compilation status differs by install method/API.

---

# 92. Benchmark Baseline Profile Impact

Compare startup:

```text
CompilationMode.None
vs
CompilationMode.Partial(BaselineProfileMode.Require)
```

using APIs supported by current dependency version.

Use identical:

```text
device
startup mode
target variant
iterations
data state
```

Suggested:

```text
10 None
10 Baseline Profile
```

Record:

```text
no-profile median TTID
profile median TTID
absolute delta
percentage delta
```

Formula:

```text
improvement% =
(noProfileMedian - baselineProfileMedian)
/
noProfileMedian
* 100
```

Do not force a positive conclusion.

---

# 93. Compare One Heavy Non-Startup CUJ

Use:

```text
Finance
```

or:

```text
Quote
```

Compare no-profile and baseline-profile modes using identical seeded data.

Record frame/journey metrics.

---

# 94. Profile Coverage Sanity Check

Inspect `baseline-prof.txt` for application namespaces associated with:

```text
startup/home
recipe
quote
production
finance
OCR
```

If expected paths are absent:

1. verify CUJ really executes;
2. verify waits reach async content;
3. verify correct target build;
4. verify generator did not exit too early.

Do not manually add arbitrary rules as a substitute for exercising the real path unless project policy explicitly requires manual rules.

---

# 95. Fully Drawn Handling

If startup async content is marked fully drawn too early, inspect whether the Compose app should use appropriate fully-drawn reporting.

Do not use sleeps to manipulate TTFD.

Change fully-drawn reporting only if evidence shows current behavior is semantically incorrect.

---

# 96. Baseline Profile Summary

Create:

```text
artifacts/final-validation/baseline-profile/summary.md
```

Include:

```text
generator task
device/API
generated profile path
line count
SHA-256
release APK/AAB paths
packaging verification
dexopt output
startup None median
startup profile median
heavy-CUJ comparison
conclusion
```

---

# 97. Baseline Profile Exit Gate

- [ ] Startup CUJ included.
- [ ] Recipe CUJ included.
- [ ] Quote CUJ included.
- [ ] Production CUJ included.
- [ ] Finance CUJ included.
- [ ] OCR CUJ included.
- [ ] Generation task succeeds.
- [ ] Text profile exists and is non-empty.
- [ ] Release APK includes profile.
- [ ] AAB includes profile when applicable.
- [ ] Dexopt inspected.
- [ ] None vs Baseline startup comparison run.
- [ ] One non-startup comparison run.
- [ ] Results documented.

---

# 98. Integration Order

Execute in this order:

```text
Phase 1  Repository/device discovery
Phase 2  Room hash provenance
Phase 3  Encrypted PDF fixture
Phase 4  OCR stress tests
Phase 5  Tablet validation
Phase 6  Shared critical user journey driver
Phase 7  Macrobenchmark execution
Phase 8  Baseline Profile generation/validation
Phase 9  Full regression
Phase 10 Final report
```

---

# 99. Phase 1 — Discovery

Complete:

```text
modules
packages
tests
benchmark/profile config
devices
evidence directories
```

before editing.

---

# 100. Phase 2 — Room Hash Provenance

Run:

```text
current-hash extraction
Git search
old DB search
old APK/AAB search
classification
documentation
```

If no historical artifact exists, classify and continue.

---

# 101. Phase 3 — Encrypted PDF

Run:

```text
generate fixture
verify encryption
hash fixture
add tests
execute stress test
normal-PDF regression
```

---

# 102. Phase 4 — OCR Stress

Implement and execute:

```text
50 sequential
20 large
cancel/retry
rotation
background/foreground
```

Fix defects discovered before continuing.

---

# 103. Phase 5 — Tablet

Validate:

```text
Medium
Expanded
navigation
list-detail
rotation
compact regression
```

Restore emulator config afterward.

---

# 104. Phase 6 — Shared CUJs

Refactor/reuse:

```text
stable UI driver
stable selectors
benchmark seed/reset
```

Verify journeys function before timing them.

---

# 105. Phase 7 — Macrobenchmark

Run:

```text
startup
recipe
quote
production
finance
OCR
```

Collect JSON, traces, console, summary.

---

# 106. Phase 8 — Baseline Profiles

Run:

```text
generate
inspect
build release
verify packaging
inspect dexopt
compare performance
document
```

---

# 107. Phase 9 — Full Regression

Use actual available project tasks.

At minimum, where defined:

```bash
./gradlew lint
./gradlew test
./gradlew connectedAndroidTest
./gradlew assembleRelease
```

Discover tasks rather than assuming root aliases.

---

# 108. Phase 10 — Final Report

Create:

```text
artifacts/final-validation/FINAL_REPORT.md
```

Sections:

```text
1. Environment
2. Files Changed
3. Room v1/v2 Hash Provenance
4. Encrypted PDF Fixture
5. OCR Stress Results
6. Tablet Validation
7. Macrobenchmark Results
8. Baseline Profile Generation
9. Release Packaging Verification
10. Regression Test Results
11. Remaining External Limitations
```

---

# 109. Environment Report

Record:

```text
git commit
branch
dirty files before/after
JDK
Gradle
AGP if easy to extract
device
API
physical/emulator
```

---

# 110. Files Changed Report

Explain every modified file.

Example:

```text
StartupBenchmark.kt
→ added complete CUJ benchmarks

CriticalUserJourneys.kt
→ shared benchmark/profile navigation

OcrStressTest.kt
→ completed stress coverage

PdfPageRendererStressTest.kt
→ encrypted PDF regression

room-schema-provenance.md
→ v1/v2 identity hash evidence
```

---

# 111. Remaining External Limitations

Only include genuine external limitations.

Examples:

```text
Small_Phone is an emulator; authoritative physical-device timings remain unavailable.

No original v1 database/schema/APK exists, so the v1 hash is RECONSTRUCTED_NOT_HISTORICALLY_VERIFIED.
```

Do not defer something as “manual” if the available emulator/test harness can verify it.

---

# 112. Required Final Re-runs

After the final code change, rerun targeted categories:

```text
Room migrations
PDF renderer stress
OCR stress
adaptive/tablet
Macrobenchmark harness
Baseline Profile generation
release build
```

Then run broad regression.

---

# 113. Failure Handling Policy

Do not stop at first failure.

Loop:

```text
run
→ capture failure
→ root cause
→ fix
→ rerun failed test
→ rerun related suite
→ continue
```

Only stop a specific branch when it depends on an unavailable external artifact or impossible environment capability.

Document that limitation and continue independent work.

---

# 114. Do Not Hide Failures

Never:

```text
add arbitrary sleeps until test passes
disable failing stress tests
weaken assertions without technical reason
suppress Macrobenchmark errors for numbers
replace real OCR with fake OCR in stress/performance
change Room historical hashes to satisfy tests
```

---

# 115. Suggested Final File Layout

Adapt to actual existing paths:

```text
benchmark/
└── src/main/java/.../
    ├── StartupBenchmark.kt
    ├── CriticalJourneysBenchmark.kt
    ├── BaselineProfileGenerator.kt
    └── CriticalUserJourneys.kt

app/
├── src/androidTest/java/.../
│   ├── OcrStressTest.kt
│   └── PdfPageRendererStressTest.kt
└── src/androidTest/assets/pdf/
    └── encrypted_receipt.pdf

docs/database/
└── room-schema-provenance.md

tools/test-fixtures/
└── generate-encrypted-pdf.sh

artifacts/final-validation/
├── benchmark/
├── baseline-profile/
├── tablet/
├── ocr-stress/
├── pdf/
├── room-schema/
└── FINAL_REPORT.md
```

Do not create duplicates when equivalents already exist.

---

# 116. Definition of Done — Macrobenchmark

Done means **executed**, not merely implemented.

Required:

```text
startup measured
recipe measured
quote measured
production measured
finance measured
OCR measured
traces generated
summary generated
device authority documented
```

---

# 117. Definition of Done — Tablet

Required:

```text
Medium runtime validation
Expanded runtime validation
list-detail
adaptive navigation
rotation
state continuity
compact regression
screenshots/evidence
```

---

# 118. Definition of Done — OCR Stress

Required:

```text
50 sequential real OCR passes
20 large image passes
cancel/retry passes
rotation passes
background/foreground passes
no unconfirmed financial persistence
no OOM/ANR
```

---

# 119. Definition of Done — Encrypted PDF

Required:

```text
real encrypted binary fixture
external encryption verification
SHA-256
controlled import error
no crash
normal PDF works immediately after failure
```

---

# 120. Definition of Done — Room Hashes

Required:

```text
v1 classified
v2 classified
evidence documented
no fabricated historical claim
```

Acceptable unresolved state:

```text
RECONSTRUCTED_NOT_HISTORICALLY_VERIFIED
```

when no original evidence exists.

---

# 121. Definition of Done — Baseline Profiles

Required:

```text
all CUJs included
profile generated
profile non-empty
APK packaging verified
AAB packaging verified if applicable
dexopt inspected
startup compared with/without profile
one heavy CUJ compared
result documented
```

---

# 122. Final Completion Checklist

## Room

- [ ] v1 current hash recorded.
- [ ] v2 current hash recorded.
- [ ] Git history searched.
- [ ] Old DB searched.
- [ ] APK/AAB searched.
- [ ] v1 classified.
- [ ] v2 classified.
- [ ] Provenance document created.

## PDF

- [ ] Encrypted fixture generated.
- [ ] Encryption verified.
- [ ] SHA-256 recorded.
- [ ] Encrypted test added.
- [ ] Normal-PDF regression added.
- [ ] Tests executed.

## OCR

- [ ] 50 sequential test.
- [ ] 20 large-image test.
- [ ] Cancel/retry.
- [ ] Rotation.
- [ ] Background/foreground.
- [ ] All executed.
- [ ] Logs captured.

## Tablet

- [ ] Medium tested.
- [ ] Expanded tested.
- [ ] Navigation tested.
- [ ] List-detail tested.
- [ ] Rotation tested.
- [ ] State continuity tested.
- [ ] Evidence captured.
- [ ] Overrides reset.

## Macrobenchmark

- [ ] Startup executed.
- [ ] Recipe executed.
- [ ] Quote executed.
- [ ] Production executed.
- [ ] Finance executed.
- [ ] OCR executed.
- [ ] JSON saved.
- [ ] Traces saved.
- [ ] Summary written.

## Baseline Profile

- [ ] Shared CUJs used.
- [ ] Profile generated.
- [ ] File non-empty.
- [ ] APK contains `baseline.prof`.
- [ ] AAB contains profile when applicable.
- [ ] Dexopt inspected.
- [ ] None benchmark run.
- [ ] Baseline benchmark run.
- [ ] Comparison documented.

## Regression

- [ ] Migration tests pass.
- [ ] PDF tests pass.
- [ ] OCR tests pass.
- [ ] Adaptive UI tests pass.
- [ ] Unit tests pass.
- [ ] Lint passes.
- [ ] Release build passes.

---

# 123. Expected Final State

After completing this plan, there should be no unresolved implementation gap from the six listed pending items.

The only acceptable remaining notes are evidence-bound external limitations, such as:

```text
authoritative benchmark requires a physical device because only an emulator was available
```

or:

```text
Room v1 historical identityHash cannot be proven because no original schema, database, APK, AAB, tag, or release artifact exists
```

Those are evidence limitations, not implementation omissions.

Everything executable in the repository and available Android environment must be completed, run, and reported.
