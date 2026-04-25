# helloword

This repo now includes a first-page POC landing page for the Backyard Pokédex concept.

## Run locally

Open `index.html` directly in your browser, or run:

```bash
python3 -m http.server 8080
```

Then visit `http://localhost:8080`.

## Deploy online

A GitHub Actions workflow is included at `.github/workflows/deploy-pages.yml` to deploy this site to GitHub Pages on pushes to `main` or `work`.

After the workflow runs, your site URL will be:

- `https://<your-github-username>.github.io/<repository-name>/`

Planning documentation for the full app remains available at:

- `docs/backyard-pokedex-app-plan.md`
