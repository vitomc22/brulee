# Brulee Telie — Remaining Technical Work Plan

## 1. Purpose

This document contains **only the remaining technical work** for Brulee Telie.

The web application is intentionally excluded from this plan.

The goal is to finish the Android application by completing:

- real document scanning;
- real OCR;
- real PDF import;
- real PDF generation;
- CSV export;
- secure file sharing;
- Android architecture cleanup;
- Room schema safety;
- missing domain entities;
- migration coverage;
- OCR quality tests;
- PDF robustness tests;
- stress tests;
- accessibility;
- tablet layouts;
- performance instrumentation;
- CI automation.

---

# 2. Recommended Implementation Order

```text
P0 — Data integrity and domain completeness
1. Remove fallbackToDestructiveMigration()
2. Implement explicit Room migrations
3. Add migration tests
4. Complete missing entities and relationships

P0 — Document ingestion
5. Integrate ML Kit Document Scanner
6. Integrate ML Kit Text Recognition
7. Implement receipt parsing and review
8. Implement real PDF import using PdfRenderer

P1 — Documents and sharing
9. Replace textual PDF generator with Android PdfDocument
10. Complete CSV export
11. Complete FileProvider integration
12. Complete Android sharing flow

P1 — Android architecture
13. Introduce ViewModels + StateFlow
14. Introduce Hilt
15. Migrate navigation to Navigation 3

P2 — Analytics UI and quality
16. Integrate Vico for charts
17. Add OCR fixtures and OCR quality metrics
18. Add large-PDF tests
19. Add OCR stress tests
20. Add accessibility tests
21. Add tablet/adaptive layout tests
22. Add Macrobenchmark + Baseline Profiles
23. Add CI pipeline
```

Data integrity must be completed before additional UX or performance work.

---

# 3. Remove `fallbackToDestructiveMigration()`

## Current problem

`BruleeDatabase.kt` still contains:

```kotlin
fallbackToDestructiveMigration()
```

This allows Room to delete and recreate the database when a required migration is missing.

For a local-first financial application, this is unacceptable because it can destroy:

- ingredients;
- recipes;
- recipe versions;
- ingredient price history;
- quotes;
- production records;
- sales;
- payments;
- financial history.

## Required change

Remove:

```kotlin
fallbackToDestructiveMigration()
```

Every schema change must have an explicit migration.

Example:

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE sales
            ADD COLUMN actual_cost_cents INTEGER
            NOT NULL DEFAULT 0
            """.trimIndent()
        )
    }
}
```

Database setup:

```kotlin
Room.databaseBuilder(
    context,
    BruleeDatabase::class.java,
    "brulee.db"
)
.addMigrations(
    MIGRATION_1_2,
    MIGRATION_2_3,
    MIGRATION_3_4
)
.build()
```

## Acceptance criteria

- `fallbackToDestructiveMigration()` is absent from production code.
- Every supported schema version has an explicit migration path.
- Historical sales and quotes survive upgrades unchanged.
- A missing migration causes a visible development/test failure rather than silent data deletion.

---

# 4. Room Schema Versioning

Introduce a strict schema versioning policy:

```text
schema v1
schema v2
schema v3
...
schema vN
```

Every schema version must include:

- migration implementation;
- exported Room schema;
- migration tests;
- brief documentation describing the change.

Enable Room schema export.

Example:

```kotlin
room {
    schemaDirectory("$projectDir/schemas")
}
```

Commit the `schemas/` directory to Git.

---

# 5. Room Migration Tests

Use `MigrationTestHelper`.

Minimum coverage:

```text
v1 → v2
v2 → v3
v3 → v4
...
vN-1 → vN
```

Also test:

```text
v1 → vN
```

Each migration test must:

1. create a database using the old schema;
2. insert representative data;
3. close the database;
4. execute the migration;
5. open the current schema;
6. validate the schema;
7. validate preserved rows;
8. validate quote snapshots;
9. validate sale snapshots;
10. validate important financial calculations.

## Acceptance criteria

- `v1 → current` is covered.
- Every intermediate step is covered.
- No valid record disappears during migration.
- Money values remain identical before and after migration.
- Relationships and foreign keys remain valid.

---

# 6. Complete the Domain Entity Model

The current model still needs full coverage for:

```text
Supplier
Purchase
PurchaseItem
Payment
Expense
CostResource
QuoteStatusHistory
ProductionStatusHistory
PaymentStatusHistory
PriceObservation
DocumentImport
```

---

# 7. `Supplier`

Represents ingredient/material suppliers.

Suggested fields:

```text
id
name
document
phone
notes
createdAt
updatedAt
```

Relationship:

```text
Supplier
    ↓
Purchase
    ↓
PurchaseItem
```

---

# 8. `Purchase`

Represents an actual purchase event.

Suggested fields:

```text
id
supplierId
purchaseDate
totalCents
documentImportId
notes
createdAt
```

A purchase should be preserved historically rather than only replacing the current ingredient price.

---

# 9. `PurchaseItem`

Suggested fields:

```text
id
purchaseId
ingredientId
descriptionOriginal
quantity
unit
normalizedQuantity
totalCents
normalizedUnitPrice
createdAt
```

Each confirmed purchase item may create a `PriceObservation`.

---

# 10. `Payment`

Payment must be independent from production status.

Suggested fields:

```text
id
saleId
amountCents
method
paidAt
status
notes
createdAt
```

Supported states:

```text
PENDING
PARTIAL
PAID
REFUNDED
```

A completed order must not automatically imply a paid sale.

---

# 11. `Expense`

Add support for expenses that are not tied directly to one recipe.

Examples:

```text
rent
maintenance
gas
electricity
transport
cleaning
marketing
equipment
other
```

Suggested fields:

```text
id
category
description
amountCents
expenseDate
isFixed
notes
createdAt
```

---

# 12. `CostResource`

Create a generic operational cost model.

Initial types:

```text
LABOR
LPG
ELECTRICITY
PACKAGING
DELIVERY
EQUIPMENT
OVERHEAD
CUSTOM
```

Suggested fields:

```text
id
type
name
unit
defaultRate
active
createdAt
updatedAt
```

This avoids hard-coding gas, electricity and labor as special cases throughout the codebase.

---

# 13. Status History

Add:

```text
QuoteStatusHistory
ProductionStatusHistory
PaymentStatusHistory
```

Suggested fields:

```text
id
entityId
previousStatus
newStatus
changedAt
reason
```

This allows the app to preserve a real audit trail instead of only storing the latest state.

## Acceptance criteria

The model is complete when the app can trace:

```text
Supplier
→ Purchase
→ PurchaseItem
→ Ingredient
→ PriceObservation
→ Recipe
→ Quote
→ Production
→ Sale
→ Payment
```

without losing historical context.

---

# 14. Replace Simulated OCR

## Current problem

`OcrCaptureScreen.kt` currently uses pre-filled text to simulate OCR.

Replace it with a real document pipeline using:

```text
ML Kit Document Scanner
+
ML Kit Text Recognition
```

---

# 15. Real Document Scanner

Expected flow:

```text
User taps "Scan receipt"
    ↓
ML Kit Document Scanner
    ↓
camera capture
    ↓
crop
    ↓
perspective correction
    ↓
image enhancement
    ↓
final image/PDF
    ↓
OCR
```

Create an abstraction:

```kotlin
interface DocumentScannerGateway {
    suspend fun scan(): ScanResult
}
```

Example result:

```kotlin
data class ScanResult(
    val imageUris: List<Uri>,
    val pdfUri: Uri?
)
```

Compose screens must not call ML Kit directly.

---

# 16. Real Text Recognition

After scanning:

```text
ScanResult
    ↓
TextRecognitionGateway
    ↓
OCR result
    ↓
ReceiptParser
```

Create:

```kotlin
interface TextRecognitionGateway {
    suspend fun recognize(image: InputImage): OcrResult
}
```

Preserve OCR structure:

```text
block
line
element
bounding box
recognized text
confidence when available
page
```

Do not flatten all OCR output into one string immediately.

---

# 17. Receipt Parsing

Implement:

```text
OCR result
    ↓
normalization
    ↓
line grouping
    ↓
currency detection
    ↓
quantity detection
    ↓
product candidate detection
    ↓
ingredient matching
    ↓
confidence scoring
    ↓
human review
```

Attempt to extract:

```text
supplier/store
document number
purchase date
product description
quantity
unit
unit price
line total
document total
```

---

# 18. Ingredient Matching

Suggested order:

```text
1. exact known aliases
2. normalized name comparison
3. token similarity
4. fuzzy matching
5. manual selection
```

Persist confirmed aliases.

Example:

```text
"OVOS BCO DZ"
→ Ingredient: Eggs
```

---

# 19. OCR Review Screen

Nothing recognized by OCR should be committed automatically.

The review UI must allow:

```text
confirm
edit
ignore
associate with existing ingredient
create new ingredient
```

Only confirmed values may update purchases or ingredient price history.

## Error handling

Handle:

```text
scanner cancelled
camera unavailable
image unreadable
OCR returns empty result
partial receipt
unsupported document
corrupt image
out-of-memory condition
```

## Acceptance criteria

- pre-filled OCR simulation is removed from production;
- a real receipt can be captured;
- OCR runs on the captured image;
- spatial OCR information is preserved;
- items are parsed into candidates;
- ingredient matches are suggested;
- user corrections are possible;
- no unconfirmed data is persisted.

---

# 20. Real PDF Import with `PdfRenderer`

## Current problem

There is no real `PdfRenderer` pipeline for imported PDFs.

Implement:

```text
User selects PDF
    ↓
validate file
    ↓
open ParcelFileDescriptor
    ↓
PdfRenderer
    ↓
render one page
    ↓
Bitmap
    ↓
OCR
    ↓
release page/bitmap
    ↓
next page
```

---

# 21. PDF Memory Safety

Never render all PDF pages into memory at the same time.

Process incrementally:

```text
page 1
→ render
→ OCR
→ release

page 2
→ render
→ OCR
→ release
```

Set explicit limits for:

```text
maximum file size
maximum page count
render resolution
bitmap dimensions
OCR concurrency
```

Validate:

```text
MIME type
file size
page count
readability
encrypted/unsupported PDF
corrupt PDF
```

## Acceptance criteria

- PDF selection uses Android document APIs;
- pages are rendered using `PdfRenderer`;
- OCR runs page by page;
- memory usage remains bounded;
- unsupported/corrupt PDFs fail safely;
- parsed content enters the same review pipeline as camera OCR.

---

# 22. Replace Textual PDF Generation

## Current problem

`PdfGenerator.kt` still produces textual output instead of a complete PDF implementation.

Replace it with:

```text
Android PdfDocument
```

Architecture:

```text
PdfGenerator
    ↓
PdfTemplate
    ↓
PdfLayoutEngine
    ↓
PdfDocument
```

Templates:

```text
CUSTOMER
INTERNAL_DETAILED
```

---

# 23. Customer PDF

Include:

```text
Brulee Telie branding
quote number
customer name
date
items
quantity
unit price
subtotal
total
validity
notes
contact information
```

Never include:

```text
ingredient cost
labor cost
gas cost
electricity cost
profit
margin
internal notes
```

---

# 24. Internal PDF

May include:

```text
estimated cost
actual cost
ingredient breakdown
labor
gas
electricity
packaging
other costs
profit
margin
estimated vs actual
recipe version
production data
```

---

# 25. PDF Layout Engine

Must support:

```text
multiple pages
headers
footers
page numbers
text wrapping
long ingredient names
large quotes
currency formatting
dates
branding
```

Do not make Activities, composables or ViewModels responsible for drawing the PDF.

## Acceptance criteria

- output is a valid PDF;
- Android PDF viewers can open it;
- customer PDF never leaks internal financial data;
- internal PDF shows correct persisted values;
- long content paginates correctly;
- totals exactly match the source quote/sale.

---

# 26. Complete CSV Export

Implement a real CSV exporter.

Requirements:

```text
UTF-8
RFC 4180-compatible escaping
stable column order
header row
predictable numeric representation
```

Export use cases:

```text
sales history
ingredient prices
purchases
financial summary
recipe profitability
```

Correctly escape:

```text
comma
double quote
CR
LF
```

## Acceptance criteria

- CSV opens correctly in common spreadsheet software;
- row count matches the exported dataset;
- totals can be recomputed from exported values;
- quotes, commas and newlines are escaped correctly;
- UTF-8 accents are preserved.

---

# 27. Complete `FileProvider`

Configure secure sharing with:

```text
FileProvider
```

Never expose:

```text
file://
```

Use:

```text
content://
```

with temporary URI permissions.

---

# 28. Complete Android Sharing

Implement:

```text
Intent.ACTION_SEND
FLAG_GRANT_READ_URI_PERMISSION
```

Flow:

```text
Generate PDF/CSV
    ↓
FileProvider URI
    ↓
Android Sharesheet
    ↓
WhatsApp / Gmail / Drive / other target
```

WhatsApp may have a dedicated shortcut, but must not be a hard dependency.

## Acceptance criteria

- PDF and CSV can be shared;
- receiver can read the URI;
- no broad storage permission is required;
- no `file://` URI is exposed;
- cancelling sharing does not change quote status;
- sharing does not change financial values.

---

# 29. Introduce ViewModels

## Current problem

The architecture does not consistently use ViewModels.

Add ViewModels for stateful flows, for example:

```text
HomeViewModel
RecipeEditorViewModel
QuoteWizardViewModel
OcrReviewViewModel
ProductionViewModel
FinanceViewModel
SettingsViewModel
```

ViewModels should:

```text
receive UI events
call use cases
observe repositories
expose immutable UI state
manage screen-level state
```

ViewModels must not:

```text
perform SQL
call ML Kit directly
draw PDFs
access Activity UI APIs
contain composable code
```

Expose state using:

```kotlin
StateFlow<UiState>
```

## Acceptance criteria

- major stateful screens use ViewModels;
- Compose screens do not call DAOs directly;
- Compose screens do not call ML Kit directly;
- configuration changes preserve screen state;
- ViewModels are unit-testable.

---

# 30. Introduce Hilt

Inject:

```text
repositories
DAOs
database
OCR gateways
scanner gateways
PDF generator
CSV exporter
clock
dispatchers where useful
```

Suggested modules:

```text
DatabaseModule
RepositoryModule
DocumentModule
CoroutineModule
AppModule
```

Prefer constructor injection.

Avoid global mutable service locators.

## Acceptance criteria

- repositories use constructor injection;
- ViewModels use `@HiltViewModel`;
- scanner/OCR/PDF implementations are injected behind interfaces;
- WorkManager integration uses Hilt where applicable;
- no application-wide mutable service locator remains.

---

# 31. Migrate to Navigation 3

## Current problem

Navigation 3 is not yet implemented.

Migrate navigation to Navigation 3 for:

```text
Home
Recipes
Recipe Editor
Quote Wizard
OCR Capture
OCR Review
Production
Sale History
Finance
Settings
```

Do not pass full domain objects between destinations.

Prefer IDs:

```text
recipeId
quoteId
saleId
purchaseId
```

Then load data through ViewModels/repositories.

## Acceptance criteria

- primary flows use Navigation 3;
- routes are typed/stable where practical;
- back navigation is correct;
- process recreation does not corrupt navigation state;
- repeated taps do not create accidental duplicate destinations.

---

# 32. Integrate Vico

Use Vico for Android charts.

Initial charts:

```text
revenue over time
profit over time
ingredient price history
estimated vs actual
cost breakdown
```

Do not build charts directly from Room entities.

Create presentation models such as:

```text
ChartPoint
TimeSeries
CategoryValue
ProfitabilityPoint
```

## Acceptance criteria

- charts render from persisted data;
- empty data is handled;
- zero and negative values are handled;
- chart totals match numeric cards;
- period filters update cards and charts consistently.

---

# 33. OCR Fixture Test Suite

Create deterministic OCR fixtures.

Example structure:

```text
test-data/
  receipts/
    receipt_001.jpg
    receipt_002.jpg
    receipt_003.png
    receipt_004.pdf

  expected/
    receipt_001.json
    receipt_002.json
    receipt_003.json
    receipt_004.json
```

Include:

```text
clean receipt
low contrast
rotation
perspective distortion
small font
long receipt
discounts
duplicate product lines
different store layouts
partial receipt
accents
noisy background
```

---

# 34. OCR Quality Metrics

Measure:

```text
field accuracy
product detection rate
price detection accuracy
quantity detection accuracy
false product rate
ingredient match accuracy
```

Optional low-level metrics:

```text
CER
WER
```

Business-field correctness is more important than raw OCR character accuracy.

Suggested initial quality targets:

```text
document total accuracy >= 95%
purchase date accuracy >= 95%
item price accuracy >= 90%
item detection >= 90%
false automatic persistence = 0%
```

Manual review remains mandatory regardless of confidence score.

---

# 35. Large PDF Tests

Add fixtures for:

```text
1 page
5 pages
20 pages
50 pages
large page dimensions
image-heavy PDF
mixed text/image PDF
corrupt PDF
encrypted PDF
```

Validate:

```text
memory usage
page iteration
OCR completion
cancellation
error handling
```

## Acceptance criteria

- pages are never rendered all at once;
- bitmaps/pages are released;
- unsupported PDFs fail gracefully;
- processing can be cancelled;
- failed import does not create partial financial data.

---

# 36. OCR Stress Tests

Add repeated OCR scenarios:

```text
50 sequential scans
20 large images
repeated cancel/retry cycles
orientation changes during review
background/foreground transitions
low-memory conditions
```

Validate:

```text
no leaked scanner resources
no duplicate purchases
no frozen UI
no unbounded memory growth
```

---

# 37. Accessibility Tests

Cover:

```text
TalkBack labels
content descriptions
touch target size
font scaling
contrast
keyboard/focus navigation where applicable
reduced motion
error announcements
```

Test font scale at least at:

```text
100%
130%
160%
200%
```

Critical screens:

```text
quote wizard
cost summary
OCR review
production
finance
settings
```

## Acceptance criteria

- actionable controls have meaningful labels;
- financial values are screen-reader accessible;
- enlarged fonts do not hide critical actions;
- color is not the only status indicator;
- reduced motion works;
- no critical accessibility issue exists in the primary flows.

---

# 38. Tablet / Adaptive Layouts

Implement adaptive layouts using conceptual width classes:

```text
Compact
Medium
Expanded
```

For tablets, prefer:

```text
list + detail
navigation rail
larger review panels
side-by-side summaries where useful
```

Good candidates:

```text
recipe list + recipe detail
production list + order detail
ingredient list + price history
OCR item list + selected item editor
```

Do not simply stretch phone layouts.

## Acceptance criteria

- important screens have intentional large-screen layouts;
- list-detail works on medium/expanded widths;
- orientation changes preserve state;
- navigation adapts correctly;
- no duplicate business logic is introduced for tablet.

---

# 39. Macrobenchmark

Add Macrobenchmark coverage for critical journeys:

```text
cold start
open recipe list
open recipe
calculate quote
open production queue
open finance
open OCR flow
```

Measure at least:

```text
cold start
warm start
frame timing
jank
database query latency
large list rendering
```

---

# 40. Baseline Profiles

Generate Baseline Profiles for:

```text
launch
home
recipe list
quote wizard
production queue
finance
```

Include profile generation as part of release engineering.

## Acceptance criteria

- critical journeys have benchmark coverage;
- release builds include the intended Baseline Profile;
- performance regressions can be compared on controlled hardware or a stable emulator profile.

---

# 41. CI Pipeline

## Current problem

There is no complete CI pipeline covering the required quality gates.

Implement a pull-request pipeline:

```text
checkout
↓
JDK/Android setup
↓
dependency restore
↓
compile
↓
lint
↓
unit tests
↓
Room migration tests
↓
instrumented tests
↓
Compose UI tests
↓
assemble release
↓
artifact upload
```

Expensive/scheduled workflow:

```text
Macrobenchmark
Baseline Profile generation
OCR fixture tests
large PDF tests
OCR stress tests
```

---

# 42. CI Quality Gates

A pull request must fail when:

```text
compilation fails
blocking lint issue exists
unit tests fail
migration tests fail
critical instrumented tests fail
release build fails
P0 financial correctness tests fail
```

Store artifacts:

```text
test reports
lint reports
APK/AAB
failed UI screenshots
benchmark results
migration logs
OCR metrics
```

## Acceptance criteria

- every PR runs compile/lint/unit/migration/release checks;
- critical instrumentation is automated;
- failed UI tests attach useful evidence;
- release build is reproducible in CI;
- migration failure blocks merge;
- P0 financial correctness failures block merge.

---

# 43. Recommended Milestones

## Milestone 1 — Database Safety

Deliver:

```text
remove fallbackToDestructiveMigration
explicit migrations
schema exports
migration tests
missing core entities
```

Exit criterion:

```text
No supported application upgrade can silently delete user data.
```

---

## Milestone 2 — Real Document Input

Deliver:

```text
ML Kit Document Scanner
ML Kit Text Recognition
structured OCR model
receipt parser
ingredient matching
review UI
```

Exit criterion:

```text
A real receipt can become confirmed purchase data without simulated text.
```

---

## Milestone 3 — Real PDF Import

Deliver:

```text
SAF document selection
PdfRenderer
page-by-page rendering
OCR per page
memory limits
```

Exit criterion:

```text
A real PDF receipt can enter the same review pipeline as a scanned receipt.
```

---

## Milestone 4 — Documents and Sharing

Deliver:

```text
Android PdfDocument
customer template
internal template
CSV export
FileProvider
Android Sharesheet
```

Exit criterion:

```text
A real quote PDF can be generated and securely shared from Android.
```

---

## Milestone 5 — Android Architecture

Deliver:

```text
ViewModels
StateFlow
Hilt
Navigation 3
```

Exit criterion:

```text
Compose contains presentation logic only and application services are injected behind interfaces.
```

---

## Milestone 6 — Charts

Deliver:

```text
Vico
revenue chart
profit chart
ingredient price chart
estimated vs actual chart
```

Exit criterion:

```text
Charts use the same persisted financial data as the numeric summary cards.
```

---

## Milestone 7 — Quality and Resilience

Deliver:

```text
OCR fixture suite
OCR metrics
large PDF tests
OCR stress tests
accessibility tests
tablet layouts
```

Exit criterion:

```text
Core flows remain correct with non-ideal documents, larger screens and accessibility settings.
```

---

## Milestone 8 — Performance and CI

Deliver:

```text
Macrobenchmark
Baseline Profiles
CI pipeline
quality gates
artifacts
```

Exit criterion:

```text
Every change is automatically validated before release.
```

---

# 44. Definition of Done

## Database

- no destructive migration fallback;
- migration path for every supported version;
- schemas versioned;
- `v1 → current` tested;
- historical financial data survives upgrades.

## Domain Model

- suppliers implemented;
- purchases and purchase items implemented;
- payments separated from production state;
- expenses implemented;
- cost resources implemented;
- price observations implemented;
- status history auditable.

## OCR

- real camera scanner;
- real OCR;
- no pre-filled production simulation;
- structured OCR result;
- receipt parser implemented;
- manual review mandatory.

## PDF Import

- Android document selection implemented;
- `PdfRenderer` used;
- page-by-page processing implemented;
- memory limits enforced.

## PDF Generation

- `Android PdfDocument` used;
- customer and internal templates implemented;
- pagination works;
- real PDF files open externally.

## Export and Sharing

- valid CSV export;
- `FileProvider` configured;
- sharing uses `content://`;
- Sharesheet works;
- WhatsApp remains optional.

## Architecture

- core screens use ViewModels;
- UI state uses StateFlow;
- dependencies managed with Hilt;
- Navigation 3 active;
- charts use Vico.

## Tests

- migration tests implemented;
- OCR fixtures implemented;
- OCR metrics implemented;
- large-PDF tests implemented;
- OCR stress tests implemented;
- accessibility tests implemented;
- tablet layout tests implemented.

## Performance

- Macrobenchmark implemented;
- critical journeys measured;
- Baseline Profiles generated.

## CI

- compile automated;
- lint automated;
- unit tests automated;
- migration tests automated;
- critical instrumentation automated;
- release build automated;
- P0 correctness failures block merging.

---

# 45. Final Technical Priority

If implementation time becomes constrained, use this order:

```text
1. Database safety
2. Domain model completeness
3. Real scanner/OCR
4. Real PDF import
5. Real PDF generation
6. File sharing/export
7. ViewModels + StateFlow
8. Hilt
9. Navigation 3
10. Test coverage
11. Tablet/accessibility
12. Vico
13. Macrobenchmark/Baseline Profiles
14. CI hardening
```

The first six items directly affect data integrity and real production usability.

Architecture cleanup, charts, performance and CI should follow immediately afterward, but they must not delay removal of destructive migrations or implementation of real document processing.
