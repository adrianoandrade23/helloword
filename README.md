# helloword

This repo now includes a **Salesforce Solution Discovery Wizard (Solution v1 MVP)** implemented as a static web app.

## What it does

- Lets users select in-scope discovery modules.
- Runs a guided interview with adaptive visibility/required logic.
- Tracks statuses including answered, missing required, and not applicable.
- Supports marking modules as architect-review-required.
- Exports JSON and Markdown outputs for SSD drafting.

## Run locally

```bash
python3 -m http.server 8080
```

Then visit `http://localhost:8080`.

## Files

- `index.html`: UI shell for module selection, guided questions, and review/export panel.
- `app.js`: module catalog, branching logic, state tracking, completeness summary, and exports.
- `styles.css`: layout and visual styling.
