# Brulee Telie — Detailed E2E / Acceptance Test Suite for Remaining Work

This file contains **255 detailed test cases** covering the remaining Android-only technical work.

> Note: a few requirements such as Hilt wiring, ViewModel boundaries, DAO dependency rules, and Navigation 3 usage cannot be proven purely by black-box E2E. Those cases are marked as **Integration/Acceptance** or **Structural** while the rest remain end-to-end behavioral tests.

## Global Rules

- Every financial assertion must use exact deterministic fixtures.
- No test may rely on network access unless the tested Android/Google component explicitly requires it.
- No destructive database reset is allowed in upgrade tests.
- OCR must never commit financial data before explicit user confirmation.
- Historical quote/sale snapshots are immutable.
- `content://` must be used for shared files.
- No successful UI flow may end in `NaN`, `Infinity`, corrupted partial state, duplicate financial records, or silent data loss.
- P0 failures block release.

## Suite Summary

| Area | Count |
|---|---:|

| Database Safety and Room Migrations | 24 |
| Domain Model Completeness | 20 |
| Real Document Scanner | 15 |
| Real OCR and Receipt Review | 25 |
| PDF Import with PdfRenderer | 18 |
| Real PDF Generation | 20 |
| CSV Export | 14 |
| FileProvider and Android Sharing | 12 |
| Android Architecture Acceptance | 14 |
| Vico Charts | 10 |
| OCR Fixture Quality Metrics | 15 |
| Large PDF and OCR Stress | 12 |
| Accessibility | 16 |
| Tablet and Adaptive Layouts | 12 |
| Macrobenchmark and Baseline Profiles | 12 |
| CI Pipeline and Quality Gates | 16 |
| **Total** | **255** |

| Priority | Count |
|---|---:|
| P0 | 101 |
| P1 | 107 |
| P2 | 47 |

# Database Safety and Room Migrations

## DB-E2E-001 — Upgrade v1 database to current without data loss

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Install/build app version using schema v1.
- Seed ingredients, recipe, quote, sale, payment and price history.

### Steps
1. Upgrade to current app build.
2. Launch app.
3. Open each seeded record.
4. Open financial summary.

### Expected Results
- Database opens without destructive recreation.
- All seeded records remain.
- Historical amounts are unchanged.
- Financial summary matches pre-upgrade values.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DB-E2E-002 — Upgrade v1→vN preserves quote snapshot

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Seed an approved quote in schema v1 with known estimated cost and sale price.

### Steps
1. Upgrade to current build.
2. Open the historical quote.

### Expected Results
- Estimated cost and sale price exactly match the original snapshot.
- No current ingredient price is substituted.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DB-E2E-003 — Upgrade preserves completed sale actual-cost snapshot

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Seed completed sale with estimated and actual cost differing.

### Steps
1. Upgrade.
2. Open sale details and finance summary.

### Expected Results
- Actual cost remains original.
- Profit/margin remain original.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DB-E2E-004 — Missing migration does not silently wipe data

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Use a test build where a required migration path is intentionally absent.
- Seed old-version data.

### Steps
1. Launch newer build.

### Expected Results
- App/test fails fast with migration error.
- Database is not silently recreated.
- Original database file remains intact for inspection.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DB-E2E-005 — No destructive migration fallback in production path

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Production-like build configured with real Room builder.

### Steps
1. Create old-version database requiring migration.
2. Launch current app.

### Expected Results
- Migration runs explicitly or app fails safely.
- User data is never deleted to recover.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DB-E2E-006 — vN-1→vN migration preserves foreign keys

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Seed related supplier→purchase→purchase item→ingredient records in previous schema.

### Steps
1. Upgrade to current schema.
2. Open purchase detail.

### Expected Results
- All relationships resolve.
- No orphaned row is created.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DB-E2E-007 — Migration preserves status histories

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Seed quote, production and payment status transitions.

### Steps
1. Upgrade.
2. Open histories.

### Expected Results
- All events remain ordered and associated to correct entity.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DB-E2E-008 — Migration adds non-null column safely

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Seed old database rows before new non-null column exists.

### Steps
1. Upgrade.

### Expected Results
- Migration supplies a valid deterministic default or backfill.
- Existing records remain readable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-009 — Migration renames/restructures table without duplicate rows

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Seed multiple rows with unique IDs.

### Steps
1. Upgrade through restructuring migration.

### Expected Results
- Row count remains identical unless migration intentionally transforms it.
- No duplicates.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-010 — Migration preserves indexes used by finance queries

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Previous schema with representative financial dataset.

### Steps
1. Upgrade.
2. Run key finance screens.

### Expected Results
- Queries return correct results.
- Schema validation confirms expected indexes.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-011 — Migration preserves unique constraints

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Seed valid unique alias/product mappings.

### Steps
1. Upgrade.
2. Attempt duplicate alias insertion.

### Expected Results
- Constraint still prevents duplicate.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-012 — Migration handles empty database

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Create empty database at oldest supported version.

### Steps
1. Upgrade to current.

### Expected Results
- App opens successfully.
- Current schema validates.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-013 — Migration handles large populated database

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Seed thousands of ingredients, purchases and sales in old schema.

### Steps
1. Upgrade.

### Expected Results
- Migration completes within acceptable test timeout.
- No row-count loss.
- No crash/OOM.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-014 — Migration handles null legacy optional fields

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Seed legacy rows containing allowed null values.

### Steps
1. Upgrade.

### Expected Results
- Rows remain valid.
- Nulls map to intended current representation.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-015 — Migration preserves timestamps and chronological order

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Seed events across multiple dates.

### Steps
1. Upgrade.
2. Open timeline/history.

### Expected Results
- Original timestamps are preserved.
- Sorting remains chronological.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-016 — Migration preserves normalized unit prices

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Seed price observations with known normalized values.

### Steps
1. Upgrade.
2. Open ingredient history.

### Expected Results
- Normalized unit prices remain exact.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-017 — Migration rollback safety on failure

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Create migration test that fails after opening a copied test database.

### Steps
1. Trigger migration failure.

### Expected Results
- Test database is not left as a partially accepted current schema.
- Failure is explicit.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-018 — Fresh install creates only current schema

**Priority:** P2  
**Test type:** E2E

### Preconditions
- No existing app data.

### Steps
1. Install current build.
2. Launch app.

### Expected Results
- Current schema is created directly.
- No migration path executes.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-019 — Schema export exists for every supported version

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Repository/workspace contains Room schema export directory.

### Steps
1. Inspect generated schema files in test/build pipeline.

### Expected Results
- Every supported version has committed schema metadata.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-020 — Migration chain works across every intermediate version

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Prepare databases for each supported version.

### Steps
1. Upgrade each version to current.

### Expected Results
- Every supported starting version reaches current schema successfully.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-021 — Migration maintains database identity after process restart

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Upgrade old database successfully.

### Steps
1. Force-stop app.
2. Relaunch.

### Expected Results
- Migrated data remains intact and current schema is reused.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-022 — Migration does not reset user configuration

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Old app has labor/gas/energy settings configured.

### Steps
1. Upgrade.
2. Open settings.

### Expected Results
- Settings are preserved exactly.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-023 — Migration preserves imported document references

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Old database references existing imported receipt metadata.

### Steps
1. Upgrade.
2. Open document-linked purchase.

### Expected Results
- Reference metadata remains valid or migration marks missing file explicitly without deleting purchase.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DB-E2E-024 — Migration preserves backup metadata

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Old schema has backup metadata.

### Steps
1. Upgrade.
2. Open backup/status area.

### Expected Results
- Last-backup information remains coherent.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Domain Model Completeness

## DOMAIN-E2E-001 — Create supplier and use it in a purchase

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Clean test DB.

### Steps
1. Create supplier.
2. Create purchase selecting supplier.
3. Reopen purchase.

### Expected Results
- Supplier persists.
- Purchase references correct supplier.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-002 — Create purchase with multiple purchase items

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Supplier and ingredients exist.

### Steps
1. Create purchase with 3 items.
2. Save.
3. Reopen.

### Expected Results
- All items persist with quantities, units and totals.
- Purchase total matches sum or defined total policy.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-003 — Confirmed purchase item creates price observation

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Ingredient exists.

### Steps
1. Create and confirm purchase item.
2. Open ingredient price history.

### Expected Results
- New price observation exists with normalized unit price and purchase date.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-004 — Payment is independent from production status

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Completed production order exists.

### Steps
1. Open sale/payment state without recording payment.

### Expected Results
- Production can be COMPLETED while payment remains PENDING.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-005 — Partial payment lifecycle

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Sale of known total exists.

### Steps
1. Record first partial payment.
2. Record second payment completing balance.

### Expected Results
- Status transitions PENDING→PARTIAL→PAID.
- Remaining balance is exact.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-006 — Refund creates payment history

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Paid sale exists.

### Steps
1. Record refund.

### Expected Results
- Payment status/history records refund event.
- Sale history remains auditable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-007 — Expense creation affects correct financial period

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Clean finance period.

### Steps
1. Create expense in selected month.
2. Open finance summary.

### Expected Results
- Expense appears in selected period and does not alter unrelated months.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-008 — CostResource supports LPG, electricity and labor

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Clean DB.

### Steps
1. Create/activate each resource type.
2. Use them in recipe costing.

### Expected Results
- Each resource persists and contributes through generic resource model.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-009 — Quote status history is auditable

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Draft quote exists.

### Steps
1. Send quote.
2. Approve quote.

### Expected Results
- History shows DRAFT→SENT→APPROVED with timestamps.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-010 — Production status history is auditable

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Approved order exists.

### Steps
1. Start production.
2. Complete production.

### Expected Results
- History shows each transition in order.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## DOMAIN-E2E-011 — Supplier deletion with referenced purchases is safe

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Supplier has purchases.

### Steps
1. Attempt supplier deletion.

### Expected Results
- App blocks destructive delete or applies defined archive behavior without orphaning purchases.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DOMAIN-E2E-012 — Purchase preserves original product description

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Ingredient exists.

### Steps
1. Create purchase item with OCR/manual description different from canonical ingredient name.
2. Save.

### Expected Results
- Original description remains available for audit.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DOMAIN-E2E-013 — Expense fixed/variable classification persists

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Clean DB.

### Steps
1. Create one fixed and one variable expense.
2. Reopen.

### Expected Results
- Classification remains correct.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DOMAIN-E2E-014 — Custom cost resource works

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Clean DB.

### Steps
1. Create CUSTOM resource 'Delivery'.
2. Use in quote.

### Expected Results
- Cost appears in quote breakdown and persists.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DOMAIN-E2E-015 — Price observations remain historical after current ingredient edits

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Ingredient has price observations.

### Steps
1. Edit ingredient display name/default fields.
2. Open price history.

### Expected Results
- Historical prices are unchanged.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DOMAIN-E2E-016 — Status history reason persists

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Entity supports transition reason.

### Steps
1. Change status with reason.
2. Reopen history.

### Expected Results
- Reason text persists with transition.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DOMAIN-E2E-017 — Deleting a purchase does not silently erase historical sale snapshots

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Purchase influenced ingredient price; completed sale exists.

### Steps
1. Delete/archive purchase according to allowed flow.
2. Open old sale.

### Expected Results
- Old sale cost/profit remains unchanged.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DOMAIN-E2E-018 — Supplier notes and optional fields support empty values

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Clean DB.

### Steps
1. Create supplier with minimal required fields.
2. Save/reopen.

### Expected Results
- Record is valid; optional fields remain empty without crash.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DOMAIN-E2E-019 — Large purchase with many items remains usable

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Many ingredients exist.

### Steps
1. Create purchase with 100 items.
2. Save/reopen/search.

### Expected Results
- All items persist; totals remain correct; UI remains usable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## DOMAIN-E2E-020 — Status histories remain immutable after entity rename

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Quote/order history exists.

### Steps
1. Rename customer/recipe where allowed.
2. Open status history.

### Expected Results
- History remains attached to same entity identity.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record IDs/counts or exported test-database evidence when useful.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Real Document Scanner

## SCAN-E2E-001 — Scan real receipt from camera and return image

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Device/emulator has supported camera scanner path.

### Steps
1. Tap Scan receipt.
2. Capture one receipt.
3. Confirm crop.

### Expected Results
- Scanner returns image URI.
- App advances to OCR flow.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SCAN-E2E-002 — Cancel scanner before capture

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Scanner available.

### Steps
1. Open scanner.
2. Cancel.

### Expected Results
- No purchase/document record is created.
- User returns safely.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SCAN-E2E-003 — Cancel scanner after capture but before confirmation

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Scanner available.

### Steps
1. Capture receipt.
2. Cancel confirmation.

### Expected Results
- No partial financial record is persisted.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SCAN-E2E-004 — Scan multi-page document

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Scanner supports multiple pages.

### Steps
1. Capture two pages.
2. Confirm.

### Expected Results
- Both page URIs are returned in correct order.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SCAN-E2E-005 — Scanner result survives navigation to review

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Successful scan result exists.

### Steps
1. Complete scan.
2. Navigate to OCR review.

### Expected Results
- Correct scanned document is processed; no stale previous scan.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SCAN-E2E-006 — Perspective correction result is accepted

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Use angled receipt fixture/physical receipt.

### Steps
1. Capture at angle.
2. Confirm corrected crop.

### Expected Results
- Corrected image is passed to OCR and readable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SCAN-E2E-007 — Low-light scan fails safely or remains reviewable

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Low-light condition/fixture.

### Steps
1. Capture receipt.

### Expected Results
- App does not persist guessed financial data before review.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SCAN-E2E-008 — Camera unavailable

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Simulate unavailable camera/scanner service.

### Steps
1. Tap Scan receipt.

### Expected Results
- Friendly error shown; app remains functional.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SCAN-E2E-009 — Scanner service error

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Simulate ML Kit scanner failure.

### Steps
1. Start scan.

### Expected Results
- Error is handled; no partial document is persisted.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SCAN-E2E-010 — Repeated scan sessions do not reuse stale URI

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Complete one scan.

### Steps
1. Start a second scan with different receipt.

### Expected Results
- Second result references only new capture.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SCAN-E2E-011 — Orientation change after scan result

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Complete scan.

### Steps
1. Rotate device before review loads.

### Expected Results
- Scan result/review state remains consistent.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SCAN-E2E-012 — Background/foreground around scanner result

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Start scanner.

### Steps
1. Send app to background.
2. Return and finish scan.

### Expected Results
- App resumes without duplicate import.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SCAN-E2E-013 — Double-tap Scan receipt does not launch duplicate flows

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Scanner button visible.

### Steps
1. Tap rapidly twice.

### Expected Results
- Only one active scan flow is created.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SCAN-E2E-014 — Very long receipt capture

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Long receipt available.

### Steps
1. Capture full document.
2. Confirm.

### Expected Results
- Scanner result is produced or clear size limitation is shown.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SCAN-E2E-015 — Scanner result metadata not persisted before review

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Scan succeeds.

### Steps
1. Return from scanner but cancel review.

### Expected Results
- No confirmed purchase/price observation exists.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Real OCR and Receipt Review

## OCR-E2E-001 — Recognize text from scanned receipt

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Known receipt fixture has expected text.

### Steps
1. Scan/import fixture.
2. Run OCR.

### Expected Results
- Recognized text is non-empty and review screen is populated.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-002 — OCR preserves line/block structure for parser

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Fixture with spatially separated item/price columns.

### Steps
1. Run OCR and parse.

### Expected Results
- Parser receives structured elements, not only flattened text.
- Correct item-price pairing is possible.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-003 — OCR review prevents automatic persistence

**Priority:** P0  
**Test type:** E2E

### Preconditions
- OCR returns candidate items.

### Steps
1. Open review.
2. Do not confirm.
3. Leave flow.

### Expected Results
- No purchase item or price observation is committed.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-004 — Confirm one OCR item

**Priority:** P0  
**Test type:** E2E

### Preconditions
- OCR review has multiple candidates.

### Steps
1. Confirm only one item.
2. Finish review.

### Expected Results
- Only confirmed item becomes financial data.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-005 — Edit OCR price before confirmation

**Priority:** P0  
**Test type:** E2E

### Preconditions
- OCR misreads known item price.

### Steps
1. Edit price to expected value.
2. Confirm.

### Expected Results
- Persisted purchase/price observation uses corrected value.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-006 — Ignore OCR candidate

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Candidate exists.

### Steps
1. Tap Ignore.
2. Confirm remaining items.

### Expected Results
- Ignored candidate is not persisted.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-007 — Match OCR item to existing ingredient

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Ingredient 'Chocolate' exists.

### Steps
1. OCR detects 'CHOC NESTLE 1KG'.
2. Select Chocolate.
3. Confirm.

### Expected Results
- Purchase item references Chocolate.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-008 — Create new ingredient from OCR review

**Priority:** P0  
**Test type:** E2E

### Preconditions
- OCR detects unknown product.

### Steps
1. Choose Create ingredient.
2. Complete minimal fields.
3. Confirm.

### Expected Results
- New ingredient and purchase item are created atomically.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-009 — Duplicate receipt is detected before duplicate persistence

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Receipt already imported/confirmed.

### Steps
1. Scan/import same receipt again.

### Expected Results
- App warns/blocks duplicate based on fingerprint/metadata.
- No duplicate price observations.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-010 — Empty OCR result

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Blank/unreadable image fixture.

### Steps
1. Run OCR.

### Expected Results
- Clear 'could not read' state shown.
- No financial data created.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCR-E2E-011 — Receipt date parsing

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture contains known date.

### Steps
1. Run OCR/parse.
2. Review result.

### Expected Results
- Expected purchase date is proposed.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-012 — Supplier/store parsing

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture contains store name/CNPJ.

### Steps
1. Run OCR/parse.

### Expected Results
- Expected supplier candidate is shown.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-013 — Document total parsing

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture has known total.

### Steps
1. Run OCR/parse.

### Expected Results
- Total candidate matches expected.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-014 — Quantity and unit parsing

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture item contains quantity/unit.

### Steps
1. Run OCR/parse.

### Expected Results
- Quantity/unit candidate is correct or marked uncertain for review.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-015 — Discounted line handling

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture contains item discount.

### Steps
1. Run OCR/parse.

### Expected Results
- Persisted effective value follows defined pricing rule and is reviewable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-016 — Repeated product lines remain distinct until review

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture repeats same product on two lines.

### Steps
1. Run OCR/parse.

### Expected Results
- Both lines are represented; parser does not silently merge without rule.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-017 — Alias learned from confirmed match

**Priority:** P1  
**Test type:** E2E

### Preconditions
- No alias exists.

### Steps
1. Confirm OCR description mapping to ingredient.
2. Scan another receipt with same description.

### Expected Results
- Existing ingredient is suggested automatically.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-018 — Incorrect fuzzy match can be changed

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Parser suggests wrong ingredient.

### Steps
1. Change selection.
2. Confirm.

### Expected Results
- Correct ingredient is persisted and future alias/match follows confirmed mapping.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-019 — Partial receipt

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture cuts off bottom total.

### Steps
1. Run OCR/parse.

### Expected Results
- Visible items are reviewable; missing total is not fabricated.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-020 — Accented product text

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture contains accented Portuguese names.

### Steps
1. Run OCR/parse.

### Expected Results
- Text remains legible and matching does not corrupt accents.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-021 — OCR failure is retryable

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Force OCR exception once.

### Steps
1. Run OCR.
2. Tap retry.

### Expected Results
- Second attempt can complete; no duplicate records created.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-022 — OCR review state survives rotation

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Review has edited values not yet confirmed.

### Steps
1. Rotate device.

### Expected Results
- Edits remain or are restored according to state policy without silent loss.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-023 — OCR review state survives process recreation after safe checkpoint

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Review draft is designed to persist.

### Steps
1. Force process recreation.
2. Return.

### Expected Results
- State restores consistently or flow restarts cleanly without partial committed financial data.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-024 — Very large image is downscaled safely

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Large image fixture.

### Steps
1. Run OCR.

### Expected Results
- No OOM; result processed within defined limits.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCR-E2E-025 — Unsupported image fails safely

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Invalid/corrupt image URI.

### Steps
1. Attempt OCR.

### Expected Results
- Friendly error; no crash; no financial persistence.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# PDF Import with PdfRenderer

## PDFI-E2E-001 — Import one-page PDF and OCR it

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Known one-page receipt PDF.

### Steps
1. Select PDF with SAF.
2. Process page.
3. Review OCR.

### Expected Results
- PdfRenderer opens file; page is rendered and recognized.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFI-E2E-002 — Import multi-page PDF sequentially

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Known 5-page PDF.

### Steps
1. Select file.
2. Process all pages.

### Expected Results
- Pages processed in order.
- Review includes page-linked results.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFI-E2E-003 — Do not load all PDF pages into memory

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Large multi-page PDF and memory instrumentation available.

### Steps
1. Import/process file.

### Expected Results
- Memory remains bounded; pages/bitmaps are released between iterations.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFI-E2E-004 — Cancel PDF processing

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Large PDF.

### Steps
1. Start import.
2. Cancel during processing.

### Expected Results
- Processing stops safely.
- No partial confirmed purchase is created.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFI-E2E-005 — Corrupt PDF

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Corrupt PDF fixture.

### Steps
1. Select file.

### Expected Results
- App reports invalid/corrupt PDF and remains stable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFI-E2E-006 — Encrypted/unsupported PDF

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Encrypted PDF fixture.

### Steps
1. Select file.

### Expected Results
- Unsupported state shown; no crash/data corruption.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFI-E2E-007 — PDF MIME validation

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Non-PDF file renamed with .pdf.

### Steps
1. Select file.

### Expected Results
- Validation rejects or safely fails before financial persistence.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-008 — PDF size limit

**Priority:** P1  
**Test type:** E2E

### Preconditions
- File exceeds configured size limit.

### Steps
1. Select file.

### Expected Results
- App blocks processing with clear message.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-009 — PDF page-count limit

**Priority:** P1  
**Test type:** E2E

### Preconditions
- PDF exceeds page limit.

### Steps
1. Select file.

### Expected Results
- App blocks or requests reduced input according to policy.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-010 — High-resolution page

**Priority:** P1  
**Test type:** E2E

### Preconditions
- PDF with large page dimensions.

### Steps
1. Import/process.

### Expected Results
- Renderer uses bounded resolution; no OOM.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-011 — Mixed image/text PDF

**Priority:** P1  
**Test type:** E2E

### Preconditions
- PDF contains mixed content.

### Steps
1. Import/process.

### Expected Results
- Rendered page enters OCR pipeline and review.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-012 — PDF document with blank page

**Priority:** P1  
**Test type:** E2E

### Preconditions
- PDF contains blank page between receipts.

### Steps
1. Import/process.

### Expected Results
- Blank page produces no fabricated items; other pages still process.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-013 — Failure on page N does not corrupt prior review data

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Multi-page PDF with one intentionally invalid/problematic page.

### Steps
1. Process until failure.

### Expected Results
- User receives partial/error state explicitly; no hidden partial commit.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-014 — Retry failed PDF import

**Priority:** P1  
**Test type:** E2E

### Preconditions
- First processing attempt forced to fail.

### Steps
1. Retry same PDF.

### Expected Results
- Second attempt succeeds without duplicate document record.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-015 — Background/foreground during PDF processing

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Multi-page PDF.

### Steps
1. Start processing.
2. Background app.
3. Return.

### Expected Results
- State resumes or cancels cleanly; no duplication.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-016 — Rotate during PDF processing

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Multi-page PDF.

### Steps
1. Start processing.
2. Rotate.

### Expected Results
- Processing state remains coherent.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-017 — Two consecutive different PDFs

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Two fixtures.

### Steps
1. Import first and cancel/finish.
2. Import second.

### Expected Results
- Second import does not reuse first results.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFI-E2E-018 — PDF selected from external document provider

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Document provider available.

### Steps
1. Select PDF through SAF provider.

### Expected Results
- URI permission/read flow works for processing session.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Real PDF Generation

## PDFG-E2E-001 — Generate valid customer PDF

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Approved or draft quote with known data.

### Steps
1. Tap Generate customer PDF.
2. Open generated file in PDF viewer.

### Expected Results
- File opens successfully as PDF and shows expected quote data.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFG-E2E-002 — Customer PDF does not expose internal costs

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Quote has ingredient/labor/gas/energy/profit data.

### Steps
1. Generate customer PDF.
2. Inspect all pages/text visually.

### Expected Results
- No internal cost, profit, margin or internal notes appear.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFG-E2E-003 — Customer PDF total equals persisted quote total

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Known quote total.

### Steps
1. Generate PDF.

### Expected Results
- Displayed total exactly matches quote snapshot.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFG-E2E-004 — Generate internal detailed PDF

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Completed sale with estimated vs actual data.

### Steps
1. Generate internal PDF.
2. Open file.

### Expected Results
- PDF includes estimated cost, actual cost, profit, margin and breakdown.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFG-E2E-005 — Internal PDF uses actual values after completion

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Completed production with deviation.

### Steps
1. Generate internal PDF.

### Expected Results
- Actual values match completed sale, not current ingredient prices.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PDFG-E2E-006 — Long customer name wraps correctly

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Quote with long customer name.

### Steps
1. Generate PDF.

### Expected Results
- Text wraps/clips safely; no overlap with totals.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-007 — Long product names wrap correctly

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Quote contains very long item names.

### Steps
1. Generate PDF.

### Expected Results
- Rows remain readable and totals aligned.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-008 — Multi-page quote paginates

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Quote contains enough items for multiple pages.

### Steps
1. Generate PDF.

### Expected Results
- Multiple pages created; content not lost/overlapped.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-009 — Header/footer repeat according to template

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Multi-page quote.

### Steps
1. Generate PDF.

### Expected Results
- Brand/header/footer/page numbers follow template rules.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-010 — Currency formatting pt-BR

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Quote includes cents and large values.

### Steps
1. Generate PDF.

### Expected Results
- Monetary values use expected BRL formatting.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-011 — Date formatting

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Quote has known date/validity.

### Steps
1. Generate PDF.

### Expected Results
- Dates display correctly.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-012 — Special characters and accents

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Names contain ç, ã, é, apostrophe etc.

### Steps
1. Generate PDF.

### Expected Results
- Text renders legibly; no missing glyph placeholders.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-013 — Zero-item invalid quote cannot generate misleading PDF

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Quote has no valid items.

### Steps
1. Attempt generation.

### Expected Results
- Generation is blocked or clearly marked invalid according to product rule.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-014 — Very large quote generates without OOM

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Quote with hundreds of lines.

### Steps
1. Generate PDF.

### Expected Results
- Generation completes or fails gracefully within limits; app does not crash.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-015 — Regenerate PDF after quote edit

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Draft quote already had a PDF generated.

### Steps
1. Edit quote.
2. Generate again.

### Expected Results
- New file reflects updated draft; old historical approved file/snapshot policy remains clear.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-016 — Approved quote PDF remains historically consistent

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Approved quote snapshot exists.

### Steps
1. Change ingredient prices.
2. Regenerate/open approved quote PDF.

### Expected Results
- Approved quote values do not change.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-017 — PDF generation cancellation/failure handling

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Inject write failure/full storage in test environment.

### Steps
1. Generate PDF.

### Expected Results
- Error is surfaced; no zero-byte file is treated as success.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-018 — PDF filename sanitization

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Customer/quote name contains filesystem-invalid characters.

### Steps
1. Generate PDF.

### Expected Results
- File created with safe deterministic name.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-019 — PDF file is readable after app restart

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Generate PDF in persistent/export location according to policy.

### Steps
1. Restart app.
2. Open/share file.

### Expected Results
- File remains accessible as designed.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PDFG-E2E-020 — Concurrent generation requests do not corrupt output

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Generate button available.

### Steps
1. Trigger rapid repeated taps.

### Expected Results
- One logical generation or separate valid files; no corrupt partial PDF.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# CSV Export

## CSV-E2E-001 — Export sales history CSV

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Known sales dataset.

### Steps
1. Export sales CSV.
2. Open resulting file.

### Expected Results
- Header and row count match dataset.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CSV-E2E-002 — CSV totals can be recomputed

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Known financial dataset.

### Steps
1. Export CSV.
2. Recompute total from rows.

### Expected Results
- Recomputed total matches app summary.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CSV-E2E-003 — UTF-8 accents preserved

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Data contains Portuguese accents.

### Steps
1. Export CSV.
2. Open with UTF-8 capable reader.

### Expected Results
- Accents remain correct.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CSV-E2E-004 — Comma escaping

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Field contains comma.

### Steps
1. Export CSV.

### Expected Results
- Field is quoted correctly.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CSV-E2E-005 — Double-quote escaping

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Field contains quote.

### Steps
1. Export CSV.

### Expected Results
- Quotes are doubled/escaped per RFC 4180.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CSV-E2E-006 — Newline escaping

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Notes field contains newline.

### Steps
1. Export CSV.

### Expected Results
- CSV remains structurally valid.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CSV-E2E-007 — Ingredient price history CSV

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Known price observations.

### Steps
1. Export ingredient prices.

### Expected Results
- Rows and normalized prices match history.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CSV-E2E-008 — Purchases CSV

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Known purchases dataset.

### Steps
1. Export purchases.

### Expected Results
- Supplier, dates, totals and items map correctly.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CSV-E2E-009 — Recipe profitability CSV

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Known profitability dataset.

### Steps
1. Export profitability.

### Expected Results
- Profit/margin columns match app calculations.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CSV-E2E-010 — Stable column order

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Run export twice without schema change.

### Steps
1. Compare headers.

### Expected Results
- Column order is deterministic.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CSV-E2E-011 — Empty export

**Priority:** P1  
**Test type:** E2E

### Preconditions
- No records for selected period.

### Steps
1. Export CSV.

### Expected Results
- Valid file with header-only or documented empty behavior.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CSV-E2E-012 — Large export

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Thousands of rows.

### Steps
1. Export CSV.

### Expected Results
- Completes without OOM and all rows present.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CSV-E2E-013 — Date filter respected in export

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Records across months.

### Steps
1. Select month.
2. Export.

### Expected Results
- Only selected period included.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CSV-E2E-014 — CSV export cancellation/error

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Inject write failure.

### Steps
1. Export.

### Expected Results
- Failure is surfaced; incomplete file not reported as success.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# FileProvider and Android Sharing

## SHARE-E2E-001 — Share PDF via content URI

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Valid generated PDF.

### Steps
1. Tap Share.
2. Inspect outgoing intent in test harness/target app.

### Expected Results
- URI scheme is content://, not file://.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SHARE-E2E-002 — Recipient app can read shared PDF

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Test recipient app available.

### Steps
1. Share PDF.
2. Open in recipient.

### Expected Results
- Recipient can read file using granted temporary permission.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SHARE-E2E-003 — Share CSV via content URI

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Valid CSV.

### Steps
1. Share CSV.
2. Open in recipient.

### Expected Results
- Recipient reads CSV successfully.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SHARE-E2E-004 — Cancelling Sharesheet does not alter quote status

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Quote in known status.

### Steps
1. Open share sheet.
2. Cancel.

### Expected Results
- Quote status/financial values unchanged.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SHARE-E2E-005 — Sharing does not mark quote as sent unless explicit rule/action

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Draft quote.

### Steps
1. Open generic share sheet and cancel/complete.

### Expected Results
- Status changes only according to explicit product behavior, never just because chooser opened.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## SHARE-E2E-006 — WhatsApp shortcut when installed

**Priority:** P1  
**Test type:** E2E

### Preconditions
- WhatsApp/test package available.

### Steps
1. Tap WhatsApp share action.

### Expected Results
- Intent targets supported app and file remains readable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SHARE-E2E-007 — Fallback when WhatsApp unavailable

**Priority:** P1  
**Test type:** E2E

### Preconditions
- WhatsApp absent.

### Steps
1. Tap share action.

### Expected Results
- Generic Sharesheet remains available; no crash.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SHARE-E2E-008 — Multiple rapid share taps

**Priority:** P1  
**Test type:** E2E

### Preconditions
- File generated.

### Steps
1. Tap Share repeatedly.

### Expected Results
- No duplicate status mutation or URI permission corruption.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SHARE-E2E-009 — Share after process restart

**Priority:** P1  
**Test type:** E2E

### Preconditions
- File metadata persisted as designed.

### Steps
1. Restart app.
2. Open quote.
3. Share generated file.

### Expected Results
- Share succeeds or app regenerates file cleanly.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SHARE-E2E-010 — No broad storage permission required

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Fresh install.

### Steps
1. Generate/share PDF and CSV.

### Expected Results
- Flow works without requesting unnecessary broad storage permission.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SHARE-E2E-011 — Share invalid/missing file fails gracefully

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Metadata references missing file.

### Steps
1. Tap share.

### Expected Results
- User gets clear error; app does not crash.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## SHARE-E2E-012 — Temporary permission scope

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Share to recipient app.

### Steps
1. Complete share; inspect intent flags/test behavior.

### Expected Results
- Read permission is granted as needed, without unrelated file exposure.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated/shared artifact when the test succeeds.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Android Architecture Acceptance

## ARCH-E2E-001 — Quote wizard survives configuration change

**Priority:** P0  
**Test type:** Integration/Acceptance

### Preconditions
- Quote wizard partially completed.

### Steps
1. Rotate device/change configuration.

### Expected Results
- Entered state remains consistent and no duplicate calculations occur.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## ARCH-E2E-002 — OCR review survives configuration change

**Priority:** P0  
**Test type:** Integration/Acceptance

### Preconditions
- Edited OCR review state exists.

### Steps
1. Rotate device.

### Expected Results
- Screen state remains correct; no financial record auto-committed.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## ARCH-E2E-003 — Production screen survives configuration change

**Priority:** P0  
**Test type:** Integration/Acceptance

### Preconditions
- Production item in progress.

### Steps
1. Rotate device.

### Expected Results
- Status and unsaved/saved state follow defined policy without duplicate transition.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## ARCH-E2E-004 — Navigation uses stable IDs instead of serialized domain objects

**Priority:** P0  
**Test type:** Integration/Acceptance

### Preconditions
- Deep-link/navigation test harness available.

### Steps
1. Navigate recipe list→recipe detail→edit using ID-backed destination.
2. Recreate process.

### Expected Results
- Destination reloads domain data from repository and remains valid.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## ARCH-E2E-005 — Back stack from quote wizard is correct

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Start from Home.

### Steps
1. Open quote wizard through several steps.
2. Press Back repeatedly.

### Expected Results
- Back returns through intended destinations without duplicate screens or app exit too early.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## ARCH-E2E-006 — Dependency substitution works in instrumented test

**Priority:** P1  
**Test type:** Integration/Acceptance

### Preconditions
- Test Hilt component provides fake scanner/OCR implementation.

### Steps
1. Launch target screen.
2. Trigger scan/OCR.

### Expected Results
- Injected fake is used, proving external services are behind injectable interfaces.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## ARCH-E2E-007 — ViewModel state is not recreated unnecessarily on recomposition

**Priority:** P1  
**Test type:** Integration/Acceptance

### Preconditions
- Debug/instrumented observable counter.

### Steps
1. Trigger recompositions without navigation.

### Expected Results
- Screen data/load operation is not duplicated due only to recomposition.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## ARCH-E2E-008 — Process recreation restores navigable state

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Navigate to entity detail by ID.

### Steps
1. Kill/recreate process via test harness.
2. Restore activity.

### Expected Results
- Correct entity is reloaded or app returns to safe entry state.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## ARCH-E2E-009 — DAO is not directly required by Compose screen contract

**Priority:** P1  
**Test type:** Structural

### Preconditions
- Architecture test/build rule enabled.

### Steps
1. Run architecture check.

### Expected Results
- UI layer does not directly depend on DAO package/classes.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## ARCH-E2E-010 — ML Kit implementation is hidden behind gateway

**Priority:** P1  
**Test type:** Structural

### Preconditions
- Architecture rule enabled.

### Steps
1. Run dependency check.

### Expected Results
- Compose/UI layer does not reference ML Kit implementation directly.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## ARCH-E2E-011 — PDF generator is injectable behind interface

**Priority:** P1  
**Test type:** Integration/Acceptance

### Preconditions
- Test build with fake PDF generator.

### Steps
1. Trigger generate action.

### Expected Results
- Fake implementation receives request; UI handles success state.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## ARCH-E2E-012 — Navigation duplicate tap protection

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Destination CTA visible.

### Steps
1. Double tap navigation CTA.

### Expected Results
- Single logical destination instance/action occurs.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## ARCH-E2E-013 — Error state survives recomposition

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Force repository error.

### Steps
1. Display error.
2. Trigger recomposition.

### Expected Results
- Error remains until handled; no infinite retry loop.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## ARCH-E2E-014 — Loading state is cancelled when ViewModel scope ends

**Priority:** P2  
**Test type:** Integration/Acceptance

### Preconditions
- Long-running fake operation.

### Steps
1. Leave screen during operation.

### Expected Results
- Operation follows cancellation policy; no stale UI callback crashes app.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Vico Charts

## CHART-E2E-001 — Revenue chart matches KPI card

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Known monthly sales dataset.

### Steps
1. Open Finance.
2. Select period.

### Expected Results
- Chart point total/values match revenue KPI data.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CHART-E2E-002 — Profit chart matches actual-cost finance logic

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Completed sales with actual costs.

### Steps
1. Open Profit chart.

### Expected Results
- Values use actual cost and match numeric summary.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CHART-E2E-003 — Ingredient price chart matches price observations

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Known chocolate history.

### Steps
1. Open ingredient chart.

### Expected Results
- Every plotted point matches persisted observation/date.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CHART-E2E-004 — Estimated vs actual chart matches completed productions

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Known deviation dataset.

### Steps
1. Open comparison chart.

### Expected Results
- Estimated and actual series match sale snapshots.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CHART-E2E-005 — Chart period filter updates chart and cards together

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Data across several months.

### Steps
1. Change period filter.

### Expected Results
- Chart and KPI cards show same filtered dataset.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CHART-E2E-006 — Chart handles negative profit

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Dataset includes loss.

### Steps
1. Open chart.

### Expected Results
- Negative value rendered correctly and not clamped to zero silently.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CHART-E2E-007 — Chart handles empty dataset

**Priority:** P1  
**Test type:** E2E

### Preconditions
- No data in period.

### Steps
1. Open Finance.

### Expected Results
- Empty state shown; no crash/fake zero trend line.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CHART-E2E-008 — Chart handles zero values

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Dataset includes zero period.

### Steps
1. Open chart.

### Expected Results
- Zero is represented correctly.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CHART-E2E-009 — Chart labels remain readable with font scaling

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Large font enabled.

### Steps
1. Open Finance chart.

### Expected Results
- Critical labels remain accessible/readable or available via semantics.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CHART-E2E-010 — Chart updates after new sale

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Finance screen open or reopened after sale.

### Steps
1. Complete new sale.
2. Return to Finance.

### Expected Results
- Chart includes new persisted value exactly once.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# OCR Fixture Quality Metrics

## OCRQ-E2E-001 — Clean receipt field accuracy fixture

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Expected JSON exists for clean receipt.

### Steps
1. Run fixture evaluation.

### Expected Results
- Required fields meet configured accuracy threshold.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCRQ-E2E-002 — Low-contrast receipt fixture

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Low-contrast fixture + expected JSON.

### Steps
1. Run evaluation.

### Expected Results
- Result is measured and review-safe; no auto-persist false positives.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCRQ-E2E-003 — Rotated receipt fixture

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Rotated fixture.

### Steps
1. Run scanner/OCR or OCR pipeline.

### Expected Results
- Expected fields meet threshold or are flagged for review.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCRQ-E2E-004 — Perspective-distorted receipt fixture

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Distorted fixture.

### Steps
1. Run pipeline.

### Expected Results
- Critical fields remain measurable and no silent financial persistence occurs.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCRQ-E2E-005 — Document total accuracy metric

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Fixture set with known totals.

### Steps
1. Evaluate all fixtures.

### Expected Results
- Configured document-total accuracy threshold is enforced.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCRQ-E2E-006 — Item price accuracy metric

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Fixture set.

### Steps
1. Evaluate item prices.

### Expected Results
- Configured item-price threshold is enforced.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCRQ-E2E-007 — Item detection rate metric

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Fixture set with labeled items.

### Steps
1. Run parser metrics.

### Expected Results
- Detection rate is reported and threshold enforced.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCRQ-E2E-008 — False automatic persistence remains zero

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Adversarial/noisy fixtures.

### Steps
1. Run full OCR review workflow without confirmation.

### Expected Results
- No item is persisted automatically.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## OCRQ-E2E-009 — Quantity accuracy metric

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture set with labeled quantities.

### Steps
1. Run evaluation.

### Expected Results
- Quantity accuracy is reported.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCRQ-E2E-010 — Ingredient match accuracy metric

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Known alias/match fixtures.

### Steps
1. Run matcher evaluation.

### Expected Results
- Match accuracy is reported and regressions visible.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCRQ-E2E-011 — Long receipt fixture

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Long receipt + expected JSON.

### Steps
1. Run pipeline.

### Expected Results
- Metrics generated; memory remains bounded.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCRQ-E2E-012 — Discount receipt fixture

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Discount fixture.

### Steps
1. Run parser.

### Expected Results
- Expected effective prices/totals follow rules.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCRQ-E2E-013 — Different store layout fixtures

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Multiple store formats.

### Steps
1. Evaluate batch.

### Expected Results
- Metrics reported per store/layout and overall.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCRQ-E2E-014 — Partial receipt fixture

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Partial fixture.

### Steps
1. Run evaluation.

### Expected Results
- Missing fields are counted as missing, not fabricated.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## OCRQ-E2E-015 — CER/WER diagnostic metrics

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Ground-truth text available.

### Steps
1. Run text-level evaluation.

### Expected Results
- CER/WER report generated as diagnostic without replacing business-field metrics.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Large PDF and OCR Stress

## STRESS-E2E-001 — Process 50 sequential receipt scans

**Priority:** P0  
**Test type:** E2E

### Preconditions
- 50 deterministic fixtures.

### Steps
1. Run scan/OCR/review cycle sequentially.

### Expected Results
- No resource leak, duplicate record or progressive failure.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## STRESS-E2E-002 — Process 20 large images

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Large-image fixtures.

### Steps
1. Run OCR sequentially.

### Expected Results
- No OOM; memory returns near baseline between runs.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## STRESS-E2E-003 — Process 50-page PDF

**Priority:** P0  
**Test type:** E2E

### Preconditions
- 50-page fixture within allowed policy.

### Steps
1. Import/process or validate policy.

### Expected Results
- If allowed, pages process sequentially; if above limit, rejected before expensive work.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## STRESS-E2E-004 — Repeated cancel/retry OCR cycles

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Fixture available.

### Steps
1. Start OCR and cancel/retry repeatedly.

### Expected Results
- No duplicate purchases and no stuck processing state.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## STRESS-E2E-005 — Low-memory PDF import

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Test device memory pressure simulation.

### Steps
1. Process large PDF.

### Expected Results
- App fails gracefully or completes within bounds; no corrupted persistence.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## STRESS-E2E-006 — Background/foreground OCR repeatedly

**Priority:** P1  
**Test type:** E2E

### Preconditions
- OCR operation running.

### Steps
1. Background/foreground app several times.

### Expected Results
- No duplicated commits; UI state remains coherent.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## STRESS-E2E-007 — Orientation changes during OCR review stress

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Review populated.

### Steps
1. Rotate repeatedly.

### Expected Results
- No state corruption or duplicate persistence.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## STRESS-E2E-008 — Rapid consecutive PDF imports

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Several PDFs.

### Steps
1. Import/cancel/import repeatedly.

### Expected Results
- Each session isolated.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## STRESS-E2E-009 — Large purchase created from many OCR items

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Fixture with many line items.

### Steps
1. Parse/review/confirm.

### Expected Results
- All confirmed items persist exactly once.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## STRESS-E2E-010 — Stress finance queries after large data growth

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Thousands of purchases/sales.

### Steps
1. Open finance/history screens.

### Expected Results
- Queries complete within acceptable threshold and remain correct.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## STRESS-E2E-011 — Long-running session resource stability

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Test harness can loop core workflows.

### Steps
1. Run several core flows for extended session.

### Expected Results
- No unbounded memory/file descriptor growth.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## STRESS-E2E-012 — Storage nearly full during PDF generation

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Device storage constrained.

### Steps
1. Generate large PDF.

### Expected Results
- Clear failure; no corrupt success artifact.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Record the fixture/document used and the review/result screen.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Accessibility

## A11Y-E2E-001 — TalkBack labels on primary navigation

**Priority:** P0  
**Test type:** E2E

### Preconditions
- TalkBack/semantics inspection enabled.

### Steps
1. Navigate Home tabs/buttons.

### Expected Results
- All actionable controls have meaningful labels.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## A11Y-E2E-002 — TalkBack reads financial values with context

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Finance screen has data.

### Steps
1. Focus KPI values.

### Expected Results
- Screen reader conveys metric name and value, not isolated numbers only.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## A11Y-E2E-003 — OCR review controls accessible

**Priority:** P0  
**Test type:** E2E

### Preconditions
- OCR review populated.

### Steps
1. Navigate using accessibility focus.

### Expected Results
- Confirm/Edit/Ignore and item values are reachable and labeled.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## A11Y-E2E-004 — Error messages are announced

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Force validation/OCR error.

### Steps
1. Trigger error.

### Expected Results
- Accessibility service receives meaningful error announcement/focus.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## A11Y-E2E-005 — 200% font scaling on quote wizard

**Priority:** P0  
**Test type:** E2E

### Preconditions
- System font scale 200%.

### Steps
1. Complete quote wizard.

### Expected Results
- No essential field/CTA is clipped or unreachable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## A11Y-E2E-006 — 200% font scaling on cost summary

**Priority:** P0  
**Test type:** E2E

### Preconditions
- 200% font.

### Steps
1. Open summary.

### Expected Results
- Totals and actions remain readable/scrollable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## A11Y-E2E-007 — Color is not sole profit/loss indicator

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Finance has positive and negative results.

### Steps
1. Inspect UI/semantics.

### Expected Results
- Text/icon/sign conveys state in addition to color.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## A11Y-E2E-008 — Reduced motion preserves functionality

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Reduced motion enabled.

### Steps
1. Complete quote and production flow.

### Expected Results
- All transitions remain understandable and no action depends on animation.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## A11Y-E2E-009 — Touch targets meet minimum size

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Core screens open.

### Steps
1. Run accessibility bounds check.

### Expected Results
- Critical actions meet target-size policy.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## A11Y-E2E-010 — 130% font scaling core flows

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Font scale 130%.

### Steps
1. Run quote/OCR/production flows.

### Expected Results
- No blocking layout issue.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## A11Y-E2E-011 — 160% font scaling core flows

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Font scale 160%.

### Steps
1. Run quote/OCR/production flows.

### Expected Results
- No blocking layout issue.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## A11Y-E2E-012 — Keyboard/focus traversal where applicable

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Hardware keyboard or emulator input.

### Steps
1. Traverse dialogs/forms.

### Expected Results
- Focus order is logical and no trap occurs.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## A11Y-E2E-013 — Status chips have semantic text

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Quotes/orders with statuses.

### Steps
1. Focus status elements.

### Expected Results
- Status meaning is announced.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## A11Y-E2E-014 — Chart information has accessible alternative

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Finance chart populated.

### Steps
1. Inspect semantics/alternative list.

### Expected Results
- Key data can be understood without visual chart alone.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## A11Y-E2E-015 — Dark theme/contrast if supported

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Dark mode enabled.

### Steps
1. Open core screens.

### Expected Results
- Critical text and semantic statuses remain legible.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## A11Y-E2E-016 — Accessibility scanner core screen pass

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Accessibility test tooling available.

### Steps
1. Scan Home, quote wizard, OCR review, production, finance.

### Expected Results
- No critical accessibility issue remains.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Tablet and Adaptive Layouts

## TABLET-E2E-001 — Recipe list-detail on expanded width

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Tablet/expanded viewport.

### Steps
1. Open Recipes.
2. Select recipe.

### Expected Results
- List and detail use adaptive layout; no stretched phone-only presentation.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## TABLET-E2E-002 — Production list-detail on tablet

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Tablet viewport and orders exist.

### Steps
1. Open Production.
2. Select order.

### Expected Results
- List/detail layout is usable and preserves selection.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## TABLET-E2E-003 — OCR review adaptive layout

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Tablet viewport and OCR candidates.

### Steps
1. Open OCR review.

### Expected Results
- Candidate list and editor use available width without overlap.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## TABLET-E2E-004 — Orientation change preserves selected detail

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Tablet in landscape with selected item.

### Steps
1. Rotate to portrait and back.

### Expected Results
- Selection/state remains coherent.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## TABLET-E2E-005 — Navigation rail on appropriate width

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Expanded width.

### Steps
1. Open app.

### Expected Results
- Adaptive navigation appears according to design policy.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## TABLET-E2E-006 — Compact width still uses phone navigation

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Phone/compact viewport.

### Steps
1. Open app.

### Expected Results
- Phone navigation remains correct; tablet changes do not regress compact layout.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## TABLET-E2E-007 — Finance tablet layout uses space effectively

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Tablet and finance data.

### Steps
1. Open Finance.

### Expected Results
- Cards/charts arrange adaptively without giant empty/stretch artifacts.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## TABLET-E2E-008 — Quote wizard remains focused on tablet

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Tablet viewport.

### Steps
1. Run wizard.

### Expected Results
- Single-step UX remains clear; controls are not excessively stretched.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## TABLET-E2E-009 — Large-font tablet compatibility

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Tablet + large font.

### Steps
1. Open core screens.

### Expected Results
- Adaptive layout remains usable.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## TABLET-E2E-010 — Window resizing on resizable environment

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Resizable Android window.

### Steps
1. Resize across compact/medium/expanded.

### Expected Results
- Layout adapts without losing current business state.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## TABLET-E2E-011 — Multi-window mode

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Device supports multi-window.

### Steps
1. Enter split screen during quote/production.

### Expected Results
- UI remains functional or degrades gracefully.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## TABLET-E2E-012 — No duplicated business logic between phone/tablet flows

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Same dataset on compact and expanded tests.

### Steps
1. Perform same quote calculation on both.

### Expected Results
- Results are identical.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Macrobenchmark and Baseline Profiles

## PERF-E2E-001 — Cold start macrobenchmark

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Release-like benchmark build.

### Steps
1. Run repeated cold starts.

### Expected Results
- Metric report generated and within defined target for reference device.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PERF-E2E-002 — Warm start macrobenchmark

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Benchmark build.

### Steps
1. Run repeated warm starts.

### Expected Results
- Metric report generated and within target.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PERF-E2E-003 — Open recipe list benchmark

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Representative dataset.

### Steps
1. Benchmark launch→recipe list.

### Expected Results
- Timing/jank metrics recorded and under target.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PERF-E2E-004 — Quote calculation benchmark

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Canonical recipe data.

### Steps
1. Benchmark quote flow calculation/render.

### Expected Results
- Latency under target and result correct.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PERF-E2E-005 — Production queue benchmark

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Large queue dataset.

### Steps
1. Open/scroll queue.

### Expected Results
- Frame timing/jank under target.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PERF-E2E-006 — Finance screen benchmark

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Large finance dataset.

### Steps
1. Open Finance and render charts.

### Expected Results
- Timing/jank under target.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## PERF-E2E-007 — Baseline profile includes launch path

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Baseline profile task configured.

### Steps
1. Generate profile.
2. Inspect output.

### Expected Results
- Launch classes/methods for critical path are represented and profile artifact generated.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PERF-E2E-008 — Baseline profile includes quote path

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Profile generator configured.

### Steps
1. Exercise quote journey.

### Expected Results
- Generated profile includes exercised path.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PERF-E2E-009 — Baseline profile install benchmark comparison

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Profile and no-profile benchmark variants.

### Steps
1. Compare key journeys.

### Expected Results
- Profile variant does not regress and shows expected startup/path benefit or documented result.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PERF-E2E-010 — Large database query latency

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Thousands of records.

### Steps
1. Measure finance/history query-backed screen.

### Expected Results
- Latency within target.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PERF-E2E-011 — Performance report artifact

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Benchmark run completes.

### Steps
1. Collect CI/local output.

### Expected Results
- Metrics are saved in machine-readable/human-readable report.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## PERF-E2E-012 — Performance regression threshold gate in controlled pipeline

**Priority:** P2  
**Test type:** E2E

### Preconditions
- Stable benchmark environment.

### Steps
1. Run baseline and candidate.

### Expected Results
- Regression beyond configured threshold fails/report flags candidate.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# CI Pipeline and Quality Gates

## CI-E2E-001 — CI compile stage blocks broken code

**Priority:** P0  
**Test type:** E2E

### Preconditions
- CI pipeline configured.

### Steps
1. Push commit with intentional compile error in test branch.

### Expected Results
- Pipeline fails at compile; later release cannot pass.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CI-E2E-002 — CI lint stage blocks configured lint errors

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Blocking lint rule configured.

### Steps
1. Introduce known blocking lint violation.

### Expected Results
- Pipeline fails with lint report artifact.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CI-E2E-003 — CI unit-test failure blocks merge

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Mandatory checks enabled.

### Steps
1. Introduce failing unit test.

### Expected Results
- CI fails; branch protection prevents merge if configured.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CI-E2E-004 — CI Room migration failure blocks merge

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Migration tests included in CI.

### Steps
1. Break a required migration.

### Expected Results
- Migration stage fails and blocks success.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CI-E2E-005 — CI critical instrumentation failure blocks merge

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Critical device tests included.

### Steps
1. Break critical flow intentionally.

### Expected Results
- CI reports failure and captures logs/screenshots.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CI-E2E-006 — CI release build succeeds from clean environment

**Priority:** P0  
**Test type:** E2E

### Preconditions
- Clean runner/cache-independent setup.

### Steps
1. Run pipeline.

### Expected Results
- Release APK/AAB builds reproducibly.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CI-E2E-007 — P0 financial correctness test is mandatory

**Priority:** P0  
**Test type:** E2E

### Preconditions
- P0 test suite tagged/configured.

### Steps
1. Force one P0 financial assertion to fail.

### Expected Results
- CI overall status fails.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.
- Any P0 failure is release-blocking.

---

## CI-E2E-008 — Failed UI test uploads screenshot

**Priority:** P1  
**Test type:** E2E

### Preconditions
- UI test screenshot capture configured.

### Steps
1. Cause UI assertion failure.

### Expected Results
- Screenshot/log artifact uploaded.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CI-E2E-009 — CI uploads test reports

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Pipeline run.

### Steps
1. Complete tests.

### Expected Results
- Unit/instrumentation/migration reports are available as artifacts.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CI-E2E-010 — CI uploads APK/AAB artifact

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Successful build.

### Steps
1. Complete pipeline.

### Expected Results
- Expected release artifact is available.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CI-E2E-011 — OCR metric report generated in scheduled/manual workflow

**Priority:** P1  
**Test type:** E2E

### Preconditions
- OCR fixture job configured.

### Steps
1. Run job.

### Expected Results
- Metric report artifact is produced.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CI-E2E-012 — Large PDF suite runs in expensive/scheduled workflow

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Scheduled/manual workflow configured.

### Steps
1. Run job.

### Expected Results
- Large-PDF test report available and failures visible.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CI-E2E-013 — Macrobenchmark job produces artifact

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Controlled benchmark runner.

### Steps
1. Run benchmark workflow.

### Expected Results
- Benchmark output uploaded.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CI-E2E-014 — Baseline profile generation workflow succeeds

**Priority:** P1  
**Test type:** E2E

### Preconditions
- Profile task configured.

### Steps
1. Run workflow.

### Expected Results
- Profile artifact generated and stored/committed according to process.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CI-E2E-015 — Dependency/cache failure does not mask test result

**Priority:** P2  
**Test type:** E2E

### Preconditions
- CI cache disabled or miss simulated.

### Steps
1. Run pipeline.

### Expected Results
- Pipeline can restore dependencies and execute from clean state.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

## CI-E2E-016 — Artifact retention is configured

**Priority:** P2  
**Test type:** E2E

### Preconditions
- CI provider supports retention.

### Steps
1. Inspect completed run configuration/artifacts.

### Expected Results
- Reports/builds follow documented retention policy.
- No unrelated record is created, modified, or deleted.
- The app reaches a stable final state without indefinite loading or crash.

### Evidence
- Capture the final screen/state relevant to the assertion.
- Preserve the generated report/log/artifact.

### Failure Conditions
- Any expected result is not met.
- The flow produces silent data loss, duplicate persistence, corrupt output, or inconsistent historical values.

---

# Coverage Matrix

| Remaining Requirement | Covered By |
|---|---|
| Remove destructive migration fallback / safe migrations | `DB-E2E-*` |
| Missing Supplier/Purchase/Payment/Expense/CostResource/status-history model | `DOMAIN-E2E-*` |
| Real ML Kit Document Scanner | `SCAN-E2E-*` |
| Real ML Kit Text Recognition / review | `OCR-E2E-*` |
| Real PdfRenderer import | `PDFI-E2E-*` |
| Android PdfDocument generation | `PDFG-E2E-*` |
| CSV export | `CSV-E2E-*` |
| FileProvider / real sharing | `SHARE-E2E-*` |
| ViewModels / StateFlow / Hilt / Navigation 3 acceptance | `ARCH-E2E-*` |
| Vico charts | `CHART-E2E-*` |
| OCR fixture metrics | `OCRQ-E2E-*` |
| Large PDF / OCR stress | `STRESS-E2E-*` |
| Accessibility | `A11Y-E2E-*` |
| Tablet/adaptive layouts | `TABLET-E2E-*` |
| Macrobenchmark / Baseline Profiles | `PERF-E2E-*` |
| CI pipeline | `CI-E2E-*` |