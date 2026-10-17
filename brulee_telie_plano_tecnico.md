# Brulee Telie — Consolidated Technical Plan

## 1. Overview

**Brulee Telie** will be an app for artisanal confectionery focused on:

- real recipe cost calculation;
- pricing;
- quote generation;
- sending quotes to customers;
- production control;
- comparison between estimated and actual cost;
- sales history;
- financial control;
- automatic invoice/receipt reading;
- primarily offline operation;
- daily use on Android;
- detailed financial analysis in a web interface.

The product must avoid looking like a traditional ERP. On Android, the experience must be simple, guided and based on micro-steps, with one question per screen.

The product's main flow is:

```text
Register costs
→ choose recipe
→ enter ingredients
→ calculate operating costs
→ calculate price
→ generate quote
→ send to customer
→ approve quote
→ send to production
→ record actual execution
→ complete sale
→ analyze financial results
```

---

# 2. Architectural Assumptions

## 2.1 Operation

- local-first app;
- works without a mandatory backend;
- local database as the primary source of truth;
- one user per installation;
- no login required in the MVP;
- Android as the main operational platform;
- web interface focused mainly on analysis;
- default currency BRL;
- visual localization pt-BR;
- phone and tablet support;
- PDF and CSV export;
- native Android sharing.

## 2.2 Offline

Core functions must work offline:

- recipes;
- ingredients;
- costs;
- quotes;
- production;
- history;
- financials;
- PDF generation;
- local database.

OCR can use local ML Kit components.

---

# 3. Technology Stack

## 3.1 Android

### Language

**Kotlin**

### UI

**Jetpack Compose + Material 3**

### Navigation

**Navigation 3**

### State

**ViewModel + StateFlow**

### Concurrency

**Kotlin Coroutines**

### Dependency injection

**Hilt**

### Database

**Room on SQLite**

### Settings

**DataStore**

### Serialization

**kotlinx.serialization**

### Scanner

**ML Kit Document Scanner**

### OCR

**ML Kit Text Recognition v2**

Preferably with the bundled model for more predictable offline behavior.

### Received PDF

**PdfRenderer**

### Generated PDF

**Android PdfDocument**

### Sharing

**FileProvider + Android Sharesheet**

### Background jobs

**WorkManager**

### Files

**Storage Access Framework**

### Charts

**Vico Compose**

### Performance

- Macrobenchmark;
- Baseline Profiles.

---

# 4. Web Interface

The web version will focus on:

- dashboards;
- financial analysis;
- history;
- comparisons;
- reports;
- recipe analysis;
- price evolution;
- profitability.

The web must not be just an enlarged version of the Android interface.

The desktop experience must be denser and more analytical.

---

# 5. Architecture

Use a layered architecture with unidirectional data flow.

```text
Compose UI
    ↓ events
ViewModel
    ↓
Use Cases / Domain
    ↓
Repositories
    ↓
Room / OCR / Files / PDF
```

State flow:

```text
Room
 ↓ Flow
Repository
 ↓
ViewModel
 ↓ StateFlow
 ↓
Compose
```

Do not introduce full MVI without need.

---

# 6. Modularization

Recommended initial structure:

```text
:app
:domain
:data
:documents
```

## 6.1 `:domain`

Pure Kotlin.

Contains:

- Money;
- Quantity;
- Ingredient;
- Recipe;
- RecipeVersion;
- CostEngine;
- PricingEngine;
- ProfitabilityEngine;
- financial rules;
- use cases.

Does not depend on Android.

## 6.2 `:data`

Contains:

- Room;
- Entities;
- DAO;
- repositories;
- migrations;
- DataStore;
- backup serialization.

## 6.3 `:documents`

Contains:

- scanner;
- OCR;
- receipt parser;
- PDF;
- CSV;
- sharing;
- file processing.

## 6.4 `:app`

Contains:

- Compose;
- ViewModels;
- Navigation;
- DI;
- final integration.

---

# 7. Financial Model

## 7.1 Money

Do not use:

```text
Double
Float
```

to represent money.

Persist values in cents:

```kotlin
data class Money(
    val cents: Long
)
```

Example:

```text
R$ 12,34
→ 1234
```

For intermediate calculations use:

```text
BigDecimal
```

with an explicit rounding policy.

---

# 8. Quantities

Use normalized units.

Examples:

```text
kg → g
liter → ml
hour → minutes
```

Conceptual model:

```text
Quantity
 ├─ value
 ├─ unit
 └─ dimension
```

Do not allow incompatible conversions.

Example:

```text
kg → g      valid
L → ml      valid
kg → liter  invalid
```

---

# 9. Historical Model

The system must never recalculate old sales using current prices.

Structure:

```text
Ingredient
      ↓
IngredientPriceHistory

Recipe
      ↓
RecipeVersion
      ↓
RecipeIngredient
```

Changing a recipe creates a new version.

Example:

```text
Recipe v1
Recipe v2
Recipe v3
```

---

# 10. Sale Snapshot

When turning a quote into a sale, persist the values from that moment.

```text
Sale
 ├─ salePrice
 ├─ estimatedCost
 ├─ actualCost
 ├─ recipeVersionId
 ├─ ingredientCostSnapshot
 ├─ laborCostSnapshot
 ├─ energyCostSnapshot
 ├─ overheadCostSnapshot
 ├─ createdAt
 └─ completedAt
```

Future changes to ingredients must not modify historical results.

---

# 11. Main Entities

```text
Ingredient
IngredientPrice
Supplier

Recipe
RecipeVersion
RecipeIngredient

CostResource
OverheadCost

Customer

Quote
QuoteItem
QuoteStatusHistory

ProductionOrder
ProductionEntry

Sale
SaleItem
Payment

Expense
Purchase
PurchaseItem

DocumentImport
PriceObservation

BackupMetadata
```

---

# 12. Operating Costs

Do not treat gas or energy as isolated fixed fields.

Create a generic concept:

```text
CostResource
```

Examples:

```text
LPG
Electricity
Labor
Packaging
Delivery
Equipment depreciation
Fixed overhead
```

---

# 13. Cost Engine

The engine must be Pure Kotlin.

Pipeline:

```text
RecipeVersion
    ↓
Ingredient cost
    ↓
Packaging
    ↓
Energy / gas
    ↓
Labor
    ↓
Variable overhead
    ↓
Fixed-cost allocation
    ↓
Total cost
    ↓
Pricing strategy
    ↓
Suggested sale price
```

---

# 14. Financial Concepts

Explicitly separate:

```text
cost
markup
margin
profit
```

Example:

```text
profit = revenue - cost

margin = profit / revenue

markup = salePrice / cost
```

---

# 15. Fixed Settings

The user can configure once:

```text
labor hourly rate
gas price
average gas cost per hour
energy price
electricity cost per hour
default margin
fixed costs
```

These values must not be asked for on every recipe.

---

# 16. Gas Cost

Simple mode:

```text
Average cost per hour
R$ ___
```

Advanced mode:

```text
P13 cylinder
Price: R$ ___

Average duration
___ hours
```

Calculation:

```text
gasCostPerHour =
gasCylinderPrice / averageDurationHours
```

---

# 17. Energy Cost

Simple mode:

```text
Average cost per hour
```

Advanced mode:

```text
Energy price
R$ ___ / kWh

Equipment power
___ kW
```

Calculation:

```text
costPerHour =
powerKw × pricePerKwh
```

---

# 18. Labor

Setting:

```text
Labor hourly rate
```

During each recipe, collect:

```text
preparation time
assembly time
decoration time
```

Calculation:

```text
laborCost =
laborHours × hourlyRate
```

---

# 19. Complexity

Complexity must not directly change gas or energy.

It represents human effort.

Options:

```text
Simple
Normal
Laborious
Complex
```

It can act as:

- labor multiplier;
- risk indicator;
- analytical information.

---

# 20. Manual Ingredient Entry

Flow:

```text
Ingredient
 ↓
Quantity purchased
 ↓
Purchase price
 ↓
Quantity used
 ↓
Proportional cost
```

Example:

```text
2 kg = R$ 64,90

Usage:
350 g

Cost used:
R$ 11,36
```

---

# 21. Receipt Scanner

Use:

```text
ML Kit Document Scanner
```

Responsible for:

- capture;
- crop;
- perspective;
- rotation;
- filters;
- image/PDF generation.

CameraX is not needed in the MVP if the ML Kit scanner is used.

---

# 22. OCR

Scanner and OCR are separate components.

Pipeline:

```text
Camera / PDF
     ↓
Document Scanner
     ↓
Corrected image
     ↓
ML Kit Text Recognition
     ↓
Structured text
     ↓
Parser
     ↓
Human review
```

---

# 23. PDF Import

Pipeline:

```text
PDF
 ↓
PdfRenderer
 ↓
Bitmap
 ↓
OCR
```

Process page by page.

Never load all pages into memory at the same time.

---

# 24. Receipt Parser

Pipeline:

```text
OCR
 ↓
normalization
 ↓
segmentation
 ↓
item detection
 ↓
matching
 ↓
confidence
 ↓
review
 ↓
persistence
```

Extract:

```text
establishment
CNPJ
date
product
quantity
unit
unit price
total price
total amount
```

---

# 25. Ingredient Matching

Example:

```text
"MUSSARELA TIROL 1KG"
↓
normalize
↓
"MUSSARELA TIROL"
↓
candidate matching
```

Strategy:

1. known aliases;
2. normalization;
3. token similarity;
4. fuzzy matching;
5. human confirmation.

Confirmed mappings must be persisted.

Example:

```text
OVOS BCO DZ
→ eggs
```

---

# 26. Mandatory OCR Review

Never update prices automatically.

Screen:

```text
Detected product
Linked ingredient
Quantity
Unit
Price
Confidence
```

Actions:

```text
Confirm
Edit
Ignore
```

---

# 27. Duplicate Detection

Use:

```text
SHA-256
```

of the file.

Also check:

```text
CNPJ
date
amount
receipt number
```

---

# 28. New Quote Flow

## Step 1

Choose recipe.

## Step 2

Add ingredients.

## Step 3

Collect times.

## Step 4

Calculate operating costs.

## Step 5

Show total cost.

## Step 6

Set price.

## Step 7

Register customer.

## Step 8

Generate quote.

---

# 29. Ingredient Wizard

The experience must be similar to learning apps.

One question per screen.

Example:

```text
Which ingredient will you use?

[ Milk chocolate ]

Continue →
```

Then:

```text
How much did you buy?

[ 2 ] [ kg ]
```

Then:

```text
How much did it cost?

R$ [ 64,90 ]
```

Then:

```text
How much will be used?

[ 350 ] [ g ]
```

---

# 30. Operating Costs in the Wizard

Ask separately:

```text
preparation time
cooking time
energy source
decoration time
complexity
```

---

# 31. Cost Summary

Screen:

```text
Ingredients
R$ 38,40

Labor
R$ 31,25

Gas
R$ 3,20

Energy
R$ 2,50

Packaging
R$ 4,50

----------------

Total cost
R$ 79,85
```

---

# 32. Pricing

Show:

```text
Suggested price
R$ 129,90

Profit
R$ 50,05

Margin
38,5%
```

Allow price editing.

Update profit and margin in real time.

---

# 33. Quote

Data:

```text
customer
product
quantity
date
price
validity
notes
```

---

# 34. Customer PDF

Show only:

```text
brand
customer
products
quantity
price
subtotal
total
validity
notes
```

Never show:

```text
internal costs
profit
margin
labor
gas
energy
```

---

# 35. Internal PDF

May contain:

```text
costs
margin
profit
operating costs
recipe version
estimated × actual difference
```

---

# 36. PDF Generation

Create an abstraction:

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

# 37. Sharing

Use:

```text
FileProvider
content:// URI
Intent.ACTION_SEND
Android Sharesheet
```

Do not depend exclusively on WhatsApp.

The main button can be:

```text
Send on WhatsApp
```

but with a fallback to Sharesheet.

---

# 38. Quote Status

```text
DRAFT
SENT
APPROVED
REJECTED
EXPIRED
CONVERTED_TO_SALE
```

---

# 39. Approval

On approval:

```text
Quote
 ↓
ProductionOrder
```

Show feedback:

```text
Order approved!
Add to production
```

---

# 40. Production Queue

TODO-style screen.

Sections:

```text
Today
Tomorrow
Coming days
```

Card:

```text
Brûlée Cake
Customer: Ana
Delivery: 17:30
```

---

# 41. Production Execution

CTA:

```text
Start production
```

On completion:

```text
Did everything go as planned?
```

Options:

```text
Yes

I had changes
```

---

# 42. Production Without Changes

Use:

```text
actualCost = estimatedCost
```

Show:

```text
Estimated cost
Actual cost
Profit
Margin
```

---

# 43. Production With Changes

Show only fields that may have changed:

```text
Ingredients
Preparation time
Cooking time
Decoration
Packaging
Other costs
```

Example:

```text
Chocolate

Estimated:
350 g

Actual:
420 g
```

Difference:

```text
+70 g
+R$ 2,27
```

---

# 44. Estimated × Actual

Show:

```text
Estimated cost
R$ 79,85

Actual cost
R$ 86,40

Difference
+ R$ 6,55
```

Then:

```text
Price
R$ 139,90

Actual profit
R$ 53,50

Actual margin
38,2%
```

---

# 45. Sales History

Group by month.

Filters:

```text
All
Paid
Pending
Cancelled
```

Each sale must preserve:

```text
customer
recipe
version
price
cost
payment
profit
date
```

---

# 46. Operational and Financial Status

Separate:

```text
ProductionStatus
```

from:

```text
PaymentStatus
```

Example:

```text
ProductionStatus:
PENDING
IN_PROGRESS
COMPLETED
CANCELLED

PaymentStatus:
PENDING
PARTIAL
PAID
REFUNDED
```

---

# 47. Financial Metrics

## Revenue

```text
SUM(sales)
```

## Profit

```text
revenue - actualCost
```

## Average Ticket

```text
revenue / salesCount
```

## Conversion

```text
approvedQuotes / eligibleQuotes
```

## Profit per hour

Requires production time.

## Break-even

Requires:

```text
fixedCosts
variableCosts
contributionMargin
```

---

# 48. Price History

Each purchase generates:

```text
PriceObservation
```

Fields:

```text
ingredientId
supplierId
price
quantity
unit
normalizedUnitPrice
purchaseDate
source
```

---

# 49. Price Alerts

Example:

```text
Chocolate went up 16,7%
```

Calculate:

```text
last price
moving average
minimum
maximum
variation
```

---

# 50. Android Dashboard

Show only essential information.

```text
Revenue
Costs
Profit
Margin
```

Then:

```text
Last 6 months
```

and:

```text
Most profitable recipes
```

---

# 51. Web Dashboard

Sidebar:

```text
Overview
Sales
Recipes
Ingredients
Costs
Financials
Reports
Settings
```

KPIs:

```text
Revenue
Costs
Profit
Margin
Average ticket
```

---

# 52. Web Financials

Charts:

```text
Revenue × Profit
Costs by category
Estimated × Actual
Margin evolution
Ingredient prices
```

Recipe table:

```text
Recipe
Sales
Revenue
Average cost
Profit
Margin
Profit/hour
```

---

# 53. Recipe Ranking

Sort by:

```text
absolute profit
margin
profit/hour
revenue
quantity sold
```

Show sample size.

---

# 54. Database and Queries

Do not compute dashboards by loading everything into memory.

Use SQL aggregations.

Example:

```sql
SELECT SUM(total_amount)
FROM sales
WHERE completed_at BETWEEN :start AND :end
```

Create indexes for:

```text
sale.completedAt
quote.createdAt
purchase.purchaseDate
ingredientPrice.ingredientId
payment.saleId
recipeVersion.recipeId
```

---

# 55. Settings with DataStore

Examples:

```text
currency
locale
defaultMargin
hourlyLaborCost
gasCostPerHour
electricityCostPerHour
backupUri
backupEnabled
theme
```

---

# 56. Backup

Do not use a direct copy of `app.db` as the main format.

Portable format:

```text
backup.zip
│
├── manifest.json
├── data.json
└── attachments/
```

Manifest:

```json
{
  "formatVersion": 1,
  "appVersion": "1.0.0",
  "createdAt": "...",
  "checksum": "..."
}
```

---

# 57. Restore

Flow:

```text
file
 ↓
checksum
 ↓
manifest
 ↓
version
 ↓
validation
 ↓
migration
 ↓
temporary database
 ↓
integrity checks
 ↓
commit
```

Never destroy current data before full validation.

---

# 58. Android Auto Backup

Also use:

```text
Android Auto Backup / Device Transfer
```

as complementary protection.

---

# 59. WorkManager

Use for:

```text
periodic backup
temp file cleanup
maintenance tasks
aggregate rebuild
```

---

# 60. Security

Threat model:

```text
lost device
exposed backup
corrupted file
malicious file
duplicate import
```

Controls:

- app sandbox;
- FileProvider;
- MIME validation;
- maximum size;
- page limit;
- checksum;
- schema validation;
- transactions;
- atomic restore.

---

# 61. Mobile Design

## Principle

Must not look like an ERP.

Experience based on:

```text
one question per screen
large cards
visual progression
immediate feedback
animations
little text
```

---

# 62. Visual Identity

Use the Brulee Telie logo as a base.

Extract:

```text
brand-primary
brand-secondary
brand-soft
background
surface
text-primary
text-secondary
success
warning
danger
```

Personality:

```text
artisanal
premium
fun
welcoming
professional
```

---

# 63. Animations

Use animations heavily, without hurting performance.

Examples:

```text
transition between questions
progress bar
animated values
sliding cards
success feedback
approval
production completed
charts
OCR
loading
```

Respect the system's:

```text
Reduce Motion
```

setting.

---

# 64. Android Navigation

Bottom navigation:

```text
Home
Recipes
Production
Financials
More
```

Main CTA:

```text
+ New quote
```

---

# 65. Home

Example:

```text
Hello 👋

[ + New quote ]

To produce today

Ana's order
Brûlée Cake
17:30

Month summary

Revenue
R$ 4.850

Profit
R$ 1.720
```

---

# 66. Wizard Design

Top:

```text
Step 2 of 6
```

or:

```text
● ● ○ ○ ○ ○
```

A single decision per screen.

---

# 67. Ingredient Screen

```text
Which ingredient will you use?

[ Milk chocolate ]

Continue
```

---

# 68. Quantity Purchased Screen

```text
How much did you buy?

[ 2 ] [ kg ]
```

---

# 69. Price Screen

```text
How much did it cost?

R$ 64,90
```

---

# 70. Quantity Used Screen

```text
How much will be used?

[ 350 ] [ g ]
```

Result:

```text
Cost of this recipe

R$ 11,36
```

---

# 71. OCR Flow in the Design

Initial screen:

```text
📷 Scan receipt
```

Loading:

```text
Reading your receipt...
Looking for products...
Finding prices...
```

Then:

```text
We found 8 products
```

---

# 72. OCR Review

Card:

```text
Nestlé Chocolate 1 kg

Ingredient:
Milk chocolate

Quantity:
1 kg

Price:
R$ 34,90

Confidence:
High

[ Confirm ]
[ Edit ]
```

---

# 73. Preparation Time

Screen:

```text
How long does the preparation take?

[-] 1h 30m [+]
```

---

# 74. Cooking Time

```text
How long does it stay on the stove or in the oven?

45 min
```

Show estimated cost.

---

# 75. Energy Source

Cards:

```text
Gas stove
Electric oven
Both
```

---

# 76. Complexity

Cards:

```text
Simple
Normal
Laborious
Complex
```

---

# 77. Decoration

```text
How long does the finishing take?

45 minutes
```

---

# 78. Result Screen

Show:

```text
Ingredients
Labor
Gas
Energy
Packaging
Others
```

Then:

```text
Total cost
Suggested price
Profit
Margin
```

---

# 79. Customer

Ask:

```text
Who is this quote for?
```

Fields:

```text
name
WhatsApp
date
notes
```

---

# 80. Quote Actions

```text
Send on WhatsApp
Mark as approved
Save draft
```

---

# 81. Visual Approval

Show:

```text
🎉 Order approved!
```

Then:

```text
Add to production
```

---

# 82. Production

Cards in TODO format.

```text
Today

□ Brûlée Cake
Ana
17:30
```

---

# 83. Completion

Question:

```text
Did everything go as planned?
```

Buttons:

```text
Yes, exactly
I had changes
```

---

# 84. Empty States

Example:

```text
You don't have any recipes yet 🍰

Create your first recipe
and find out how much it really costs.

[ Create recipe ]
```

---

# 85. Errors

Avoid technical codes.

Example:

```text
We couldn't read this receipt.

Try again with:
- more light;
- the whole document visible;
- less tilt.
```

---

# 86. Loading

Avoid a generic spinner.

OCR example:

```text
📄 Reading the receipt...
🔍 Looking for products...
💰 Finding prices...
```

---

# 87. Design System Components

```text
PrimaryButton
SecondaryButton
AnimatedNumber
ProgressHeader
QuestionCard
IngredientCard
RecipeCard
OrderCard
FinanceCard
MetricCard
StatusChip
MoneyInput
QuantityInput
UnitSelector
TimeSelector
ComplexitySelector
EmptyState
SuccessState
OCRItemReview
ChartCard
BottomSheet
ConfirmationDialog
```

---

# 88. Responsiveness

## Compact

Smartphones.

Prioritize:

```text
wizard
production
quote
```

## Medium

Tablets.

Allow:

```text
list + detail
```

## Expanded

Desktop.

Prioritize:

```text
dashboard
charts
tables
comparisons
```

---

# 89. Master Prompt for Stitch

```text
Create a design system and the main screens of an app called Brulee Telie, aimed at a confectioner who needs to calculate the real cost of sweets, generate quotes, control production and analyze sales.

The product must be mobile-first, primarily Android, with a responsive web version intended for more detailed financial analysis.

Do not create an interface that looks like an ERP, accounting system or banking app. The mobile experience must be extremely simple, gamified and based on micro-steps. Use one question per screen, large buttons, cards, a progress bar, immediate feedback and many micro-animations.

Use the colors and shapes of the Brulee Telie logo to create the design system. The identity must convey artisanal confectionery, premium product, warmth, lightness and fun.

The main flow starts at New quote.

The user chooses a recipe and enters each ingredient individually.

For each ingredient the app asks, on separate screens:

- ingredient name;
- quantity purchased;
- unit;
- price paid;
- quantity used in the recipe.

The app immediately shows the proportional cost used.

Allow adding ingredients manually or using Scan receipt.

In the receipt flow show:

- capture;
- animated processing;
- recognized items;
- matching with ingredients;
- price;
- quantity;
- human review.

No item should be saved automatically.

After the ingredients, ask:

- preparation time;
- cooking time;
- energy source;
- decoration time;
- complexity.

Labor hourly rate, gas cost and energy cost are persistent settings.

Then show the cost breakdown:

- ingredients;
- labor;
- gas;
- energy;
- packaging;
- others;
- total cost;
- suggested price;
- profit;
- margin.

Allow changing the final price and immediately show the impact on margin and profit.

Then request customer data and show a quote preview.

Create a highlighted button:

Send on WhatsApp

In addition to:

Mark as approved
Save draft

The customer PDF must show:

- brand;
- customer;
- products;
- quantity;
- price;
- subtotal;
- total;
- validity;
- notes.

Never show internal costs, margin or profit.

When the quote is approved, use a success animation and automatically add it to the production queue.

Create a Production screen similar to a task list.

Separate:

Today
Tomorrow
Coming days

When finishing a production, ask:

Did everything go as planned?

Options:

Yes, exactly
I had changes

If there were changes, show the estimated data and allow editing only:

- ingredient quantities;
- time;
- cooking;
- decoration;
- packaging;
- other costs.

Then show the comparison:

Estimated × Actual

Including:

- estimated cost;
- actual cost;
- difference;
- actual profit;
- actual margin.

Create a sales history grouped by month.

On the Android version, financials must be simple and show:

- revenue;
- cost;
- profit;
- margin;
- monthly evolution;
- most profitable recipes.

On the web version create a more analytical experience.

Use:

- sidebar;
- KPI cards;
- charts;
- tables;
- history;
- estimated × actual;
- ingredient price evolution;
- recipe ranking.

Use many micro-interactions:

- sliding cards;
- progress bar;
- animated numbers;
- success states;
- order approval;
- production transition;
- OCR loading;
- animated charts.

Animations must respect Reduce Motion.

Create layouts for:

- Android smartphone;
- tablet;
- desktop/web.

Use Material Design 3 as the structural foundation, but create a unique identity for Brulee Telie.
```

---

# 90. Recommended Order in Stitch

Generate in stages:

```text
1. Design System + Home
2. Ingredient wizard
3. Operating costs
4. Summary and pricing
5. Quote + WhatsApp
6. Production
7. Estimated × actual
8. Scanner + OCR
9. History
10. Android financials
11. Web financials
12. Settings
```

---

# 91. Testing Strategy

## Domain

JVM tests.

Test:

```text
cost
markup
margin
rounding
units
waste
labor
energy
gas
fixed costs
break-even
```

---

# 92. Database Tests

Test:

```text
DAO
transactions
constraints
migrations
```

Migrations:

```text
v1 → v2
v2 → v3
v1 → vN
```

---

# 93. Backup Tests

Pipeline:

```text
dataset A
 ↓
export
 ↓
erase
 ↓
import
 ↓
dataset B
```

Validate:

```text
canonical(A) == canonical(B)
```

---

# 94. OCR Tests

Create dataset:

```text
receipt01.jpg
receipt02.jpg
receipt03.jpg
```

With:

```text
expected01.json
expected02.json
expected03.json
```

Metrics:

```text
field accuracy
item detection rate
price accuracy
false match rate
```

---

# 95. Parser Tests

Separate parser from OCR.

Input:

```text
OCR fixture
```

Output:

```text
ParsedReceipt
```

---

# 96. UI Tests

Test journeys:

```text
create ingredient
create recipe
calculate price
generate quote
approve quote
production
review OCR
complete sale
restore backup
```

---

# 97. Performance

Critical User Journeys:

```text
cold start
open dashboard
open recipe
calculate price
scan receipt
open production
generate PDF
```

Add:

```text
Baseline Profiles
Macrobenchmark
```

---

# 98. CI

Pipeline:

```text
compile
 ↓
lint
 ↓
unit tests
 ↓
Room migration tests
 ↓
instrumentation tests
 ↓
release build
```

Separately:

```text
Macrobenchmark
Baseline Profile generation
```

---

# 99. Roadmap

## Phase 0 — Foundation

Implement:

```text
architecture
Money
Quantity
financial model
Room schema
versioning
snapshots
migrations
PDF spike
OCR spike
backup format
```

---

## Phase 1 — Cost MVP

Flow:

```text
Ingredient
→ Recipe
→ Cost
→ Price
→ Quote
→ PDF
→ Sharing
```

---

## Phase 2 — Sales and Production

Add:

```text
customers
quotes
approval
production
sales
payments
snapshots
```

---

## Phase 3 — OCR and Purchases

Add:

```text
Document Scanner
Text Recognition
PDF import
Receipt Parser
Ingredient Matcher
Review Screen
PriceObservation
Duplicate Detection
```

---

## Phase 4 — Financials

Add:

```text
revenue
cost
profit
average ticket
conversion
profit/hour
price trends
break-even
```

---

## Phase 5 — Backup

Add:

```text
portable backup
restore
checksum
migration
Auto Backup
WorkManager
SAF
```

---

## Phase 6 — Web

Create analytical interface:

```text
dashboard
reports
history
ranking
charts
analyses
```

---

## Phase 7 — Performance and Quality

Add:

```text
Macrobenchmark
Baseline Profiles
large dataset tests
large PDF tests
OCR stress
accessibility
tablet layouts
```

---

## Phase 8 — Secondary Features

Only after the core:

```text
shopping list
goals
streak
mascot
sounds
advanced animations
widgets
```

---

# 100. Main Product Flow

```text
HOME
 │
 └── New quote
       │
       ├── Choose recipe
       │
       ├── Ingredients
       │    ├── quantity purchased
       │    ├── price
       │    └── quantity used
       │
       ├── Preparation time
       ├── Cooking
       ├── Energy / gas
       ├── Complexity
       ├── Decoration
       │
       ├── Final calculation
       │
       ├── Set price
       │
       ├── Customer
       │
       ├── Quote
       │
       ├── WhatsApp
       │
       └── Approve
             │
             ▼
          PRODUCTION
             │
             ├── Produce
             │
             └── Finish
                   │
                   ├── No changes
                   │
                   └── With changes
                         │
                         ▼
                  ESTIMATED × ACTUAL
                         │
                         ▼
                    SALE COMPLETED
                         │
                         ▼
                     FINANCIALS
```

---

# 101. Architectural Quality Criterion

The architecture will be adequate when it is possible to swap:

```text
Room
OCR
PDF generator
UI
```

without changing the financial engine.

The domain must be able to run:

```kotlin
val result = costEngine.calculate(recipe)
```

without depending on:

```text
Context
Activity
Room
Compose
ML Kit
Android SDK
```

---

# 102. Implementation Priority

The technical priority must be:

```text
1. correct financial model
2. history and snapshots
3. calculation
4. persistence
5. quote
6. production
7. estimated × actual
8. OCR
9. financials
10. web
11. gamification
```

The UI can be visually rich from the start, but secondary features must not delay the integrity of the financial model.

---

# 103. Final Goal

Brulee Telie must make it possible to answer precisely:

```text
How much does it really cost to produce this sweet?

How much should I sell it for?

How much am I earning?

Which recipes are the most profitable?

Which ingredients are getting more expensive?

How far is the actual cost deviating from the quote?

How much work am I putting into each recipe?
```

The system must turn these answers into a flow simple enough to be used during the production routine without looking like a complex financial system.
