# Backyard Pokédex App Plan

## 1) Product Vision
A family-friendly "home Pokédex" app where kids and parents can discover, identify, and catalog animals and insects found around their backyard. The app combines playful collection mechanics with real-world learning, safety guidance, and family collaboration.

## 2) Goals and Non-Goals

### Goals
- Make nature exploration fun for kids (collection, badges, streaks).
- Support parent oversight and account safety.
- Build a high-quality "field journal" with photos, notes, and observations.
- Teach species facts, habitat basics, and safe behavior around wildlife.

### Non-Goals (v1)
- Professional scientific validation workflow for researchers.
- Open public social feeds with unmoderated messaging.
- Full AR game mechanics (can be explored in later phases).

## 3) Primary Users
- **Kids (6–12):** Discover and log backyard creatures with simple, playful flows.
- **Parents/Guardians:** Create household, review submissions, set privacy rules.
- **Teachers (optional v2):** Run class challenges and guided observation assignments.

## 4) Core Value Propositions
- **Family learning:** Shared nature exploration activity.
- **Safe by design:** Parent controls and child privacy protection.
- **Real-world engagement:** Encourages outdoor observation.
- **Progress motivation:** Collection goals, badges, and seasonal challenges.

## 5) Feature Set (Full App Scope)

## 5.1 Account and Family Management
- Household account with parent owner role.
- Child sub-profiles with nicknames/avatars.
- Optional invite for second parent/guardian.
- PIN/biometric lock for parent settings.
- Consent capture and age-aware onboarding.

## 5.2 Observation Capture
- Quick add flow: photo/video, date/time, location (optional), habitat.
- Creature type selector: insect, bird, mammal, reptile, amphibian, arachnid, unknown.
- Multi-photo support for one observation.
- "Behavior tags" (flying, crawling, feeding, resting).
- Notes and voice memo support.
- Offline-first capture with sync queue.

## 5.3 Species Identification
- AI image recognition (confidence score).
- Candidate species list with visual matching tips.
- Parent-confirmed final species selection.
- "Unknown creature" bucket for unresolved observations.
- Region-aware suggestions based on country/state/ecosystem.

## 5.4 Digital Pokédex Collection
- Species cards with image, fun facts, scientific info, safety tips.
- Seen count by child and family.
- "First seen" date and seasonality chart.
- Rarity indicators (common/uncommon/rare in local area).
- Collection completion dashboard by category.

## 5.5 Learning and Gamification
- Daily/weekly missions (e.g., find 3 pollinators).
- Achievement badges (Night Explorer, Bird Watcher, etc.).
- XP and level progression for kids.
- Mini quizzes tied to discovered species.
- Family challenge mode and celebration animations.

## 5.6 Map and Journal
- Backyard map pinboard (optional precise location).
- Timeline journal of discoveries.
- Filter by child, date range, species group, tag.
- Export to PDF "Nature Journal" for school projects.

## 5.7 Safety and Parent Controls
- Wildlife safety warnings (do not touch unknown insects, etc.).
- Parent approval for sharing/export.
- No public location by default.
- Report inaccurate species predictions.
- Configurable screen-time reminders.

## 5.8 Accessibility and Localization
- Large touch targets and simplified kid mode.
- Dyslexia-friendly font option.
- Voice read-aloud for species facts.
- High-contrast theme.
- Multi-language support (start with EN/ES).

## 5.9 Notifications and Engagement
- Reminder nudges for nature walks.
- Seasonal challenge launch notifications.
- "New species nearby" educational prompts.
- Parent weekly summary email.

## 5.10 Admin and Operations
- Content management for species facts and missions.
- Moderation and abuse-report dashboard.
- Feature flags and A/B testing framework.
- Analytics dashboard for activation/retention/conversion.

## 6) Functional Requirements (by Module)

### A. Authentication and Roles
- FR-A1: Parent can create family account with verified email.
- FR-A2: Parent can add/remove child profiles.
- FR-A3: Child cannot access privacy/payment settings without PIN.

### B. Observation Lifecycle
- FR-B1: User can create observation within 10 seconds in quick mode.
- FR-B2: Observation supports media + notes + tags.
- FR-B3: Observation status: draft → identified → verified.

### C. Identification
- FR-C1: System returns top-5 likely species predictions.
- FR-C2: Show confidence percentage and visual comparison.
- FR-C3: Parent can override AI selection.

### D. Pokédex and Progress
- FR-D1: Species card unlocks after first confirmed observation.
- FR-D2: Collection stats update in real time after sync.
- FR-D3: Child profile has progress meter and badge shelf.

### E. Learning Features
- FR-E1: Missions are personalized by age and recent activity.
- FR-E2: Quiz unlocks for species already discovered.

### F. Privacy and Compliance
- FR-F1: Child profile data defaults to private.
- FR-F2: Data retention and deletion available in parent settings.
- FR-F3: Exportable parental consent records.

## 7) Non-Functional Requirements
- **Performance:** Camera capture flow opens in < 1.5 seconds on mid-range phones.
- **Reliability:** 99.5% monthly uptime for backend APIs.
- **Scalability:** Support 100k monthly active families.
- **Security:** Encryption in transit and at rest; role-based access control.
- **Compliance:** COPPA/GDPR-K style child data controls (region dependent legal review).
- **Observability:** Tracing, structured logs, crash reporting.

## 8) Recommended Tech Stack

### Mobile App
- React Native (iOS + Android single codebase).
- Expo or bare workflow depending on camera/ML requirements.

### Backend
- Node.js + TypeScript (NestJS or Express).
- PostgreSQL for relational data.
- Redis for queues/caching.
- Object storage (S3-compatible) for media uploads.

### AI/ML
- External species classification API for MVP.
- Optional future custom model for local fauna specialization.

### Infrastructure
- Dockerized services, deployed on AWS/GCP.
- CDN for media delivery.
- Background jobs for notifications, exports, and reprocessing.


## 8.1) Mobile Runtime Plan (How It Runs on Phones)

### Development Setup (Free)
- Use **React Native + Expo** so one codebase runs on Android and iOS.
- Install tools: Node.js LTS, Expo CLI, Android Studio emulator (free), Xcode simulator on macOS (free).
- Use `expo start` for local development and instant preview.
- For real devices during development, use **Expo Go** (no paid account required).

### Mobile App Architecture
- **State:** local cache-first approach (SQLite/AsyncStorage) + server sync.
- **Offline mode:** observations save locally first, then sync when internet returns.
- **Media handling:** compress photos on device before upload to reduce bandwidth.
- **Background sync:** queue unsent items with retry policy.

### Build and Release Strategy
- **Alpha testing:** share Expo development builds with family testers.
- **Android production:** generate Android App Bundle and publish to Play Store.
- **iOS production:** generate iOS build and publish to App Store (Apple developer account may still be required by Apple).
- **Web companion (optional):** Expo web build for a lightweight browser dashboard.

### Device Support Targets
- Android 10+ and iOS 16+.
- 2 GB RAM minimum target profile.
- Camera permission required; location permission optional.

## 8.2) Zero-Cost Online Deployment + Persistent Data Plan

> Objective: keep running costs at **$0** by staying within free-tier limits.

### Recommended Free Stack
- **Frontend hosting (optional web dashboard):** Cloudflare Pages (Free plan).
- **Backend + database + auth + storage:** Supabase Free plan.
- **Push notifications:** Expo notifications (free usage for typical small MVP traffic).
- **Source control + CI:** GitHub + GitHub Actions free minutes for small repos.

### Persistent Data Design
- Use Supabase Postgres for all structured data:
  - families, child profiles, observations, species, badges, missions.
- Use Supabase Storage buckets for photos/audio.
- Enable daily automatic backups export script (run via GitHub Actions on schedule) to another free private repository or object bucket.
- Keep media metadata in Postgres so files can be restored if moved.

### Security and Access (No Extra Cost)
- Supabase Auth for parent login.
- Row Level Security (RLS) so each family can only access their own records.
- Signed URLs for private media access.
- Parent role enforced in API policies.

### Zero-Cost Deployment Workflow
1. Push code to GitHub.
2. Cloudflare Pages auto-deploys web frontend from `main` branch.
3. Mobile app fetches API/data from Supabase project URL.
4. Supabase persists all records/files.
5. GitHub Action runs scheduled backup/export.

### Free-Tier Guardrails (Important)
- Add usage monitoring dashboard for DB size, storage, and request volume.
- Auto-prune oversized raw uploads after compression.
- Set max photo resolution and max uploads/day per child profile.
- Archive old logs/analytics monthly.
- If usage nears limits, enable "read-only weekend mode" toggle to avoid service pause.

### Known Constraints of Fully Free Approach
- Free tiers can throttle, pause, or limit throughput.
- Performance may vary at peak times.
- Not ideal for large-scale public launch without paid upgrades.
- Keep architecture portable so you can migrate to paid infra later without rewriting the app.

## 9) Data Model (Initial)
- **Family** (id, name, owner_user_id, locale, created_at)
- **User** (id, email, role[parent|guardian], auth_provider)
- **ChildProfile** (id, family_id, nickname, age_range, avatar)
- **Observation** (id, child_id, captured_at, habitat, lat, lng, notes, status)
- **ObservationMedia** (id, observation_id, url, type, metadata)
- **Species** (id, common_name, scientific_name, category, danger_level)
- **IdentificationResult** (id, observation_id, species_id, confidence, source_model)
- **Badge** (id, code, name, criteria)
- **ChildBadge** (child_id, badge_id, awarded_at)
- **Mission** (id, title, rules, start_at, end_at)

## 10) Key User Flows
1. Parent signs up → creates family → adds child profile.
2. Child opens quick capture → photos an insect → saves observation.
3. AI suggests species → parent confirms/corrects.
4. Species card unlocks → badge progress updates.
5. Family reviews weekly journal and exports to PDF.

## 11) Screen Map
- Splash / Onboarding
- Parent Sign-Up / Login
- Family Setup
- Child Home Dashboard
- Capture Camera
- Observation Detail
- Species Suggestion + Confirm
- Pokédex Collection Grid
- Species Card
- Missions / Achievements
- Map / Timeline Journal
- Parent Controls & Privacy
- Settings & Accessibility

## 12) MVP Definition (Phase 1)
Include:
- Parent + child accounts.
- Observation capture with photo + note.
- AI top-3 species suggestion.
- Basic species cards and collection grid.
- Weekly mission and 5 starter badges.
- Parent privacy controls and export CSV.

Exclude for MVP:
- Classroom mode.
- AR experiences.
- Advanced social sharing.

## 13) Delivery Roadmap

### Phase 0 (2–3 weeks): Discovery and Design
- User interviews (parents + kids).
- Wireframes and design system.
- Legal/privacy requirement definition.

### Phase 1 (6–8 weeks): MVP Build
- Auth, family profiles, capture, AI suggestions, Pokédex, missions.
- Basic analytics and crash reporting.
- Closed alpha with 20–50 families.

### Phase 2 (4–6 weeks): Beta
- Offline sync improvements.
- Journal export enhancements.
- Multi-language and accessibility pass.

### Phase 3 (6+ weeks): Growth
- Seasonal events, classroom mode, stronger personalization.
- Experimentation framework and engagement optimization.

## 14) Team and Ownership
- Product Manager
- Mobile Engineer (x2)
- Backend Engineer (x2)
- ML/AI Engineer (x1)
- Designer (x1)
- QA Engineer (x1)
- Content/Nature Education Specialist (part-time)

## 15) Success Metrics
- Activation: % families who log first observation in 24h.
- Retention: D7 and D30 active families.
- Learning: average quizzes completed per child/month.
- Quality: AI suggestion acceptance rate.
- Safety: % accounts with parent controls enabled.

## 16) Risks and Mitigations
- **Misidentification risk:** Always show confidence + parent confirmation.
- **Privacy risk:** Private-by-default architecture and location obfuscation.
- **Low retention:** Mission cadence, seasonal content, and family reminders.
- **Content accuracy:** Vet facts through trusted wildlife sources.

## 17) Implementation Plan for a Free Launch

### Week 1: Foundations
- Create Supabase project, schema, RLS policies, and storage buckets.
- Scaffold Expo app with auth, navigation, and local persistence.
- Configure environments (`dev`, `staging`) with `.env` strategy.

### Week 2: Core Capture + Sync
- Build observation capture screen (photo, notes, tags).
- Implement offline queue and sync engine.
- Upload media to Supabase Storage and persist metadata.

### Week 3: Pokédex + Family Features
- Add family/child profile management.
- Add collection grid and species detail card.
- Add parent controls and privacy defaults.

### Week 4: Deploy + Test
- Deploy optional web dashboard to Cloudflare Pages.
- Set up GitHub Actions for backup exports and lint/test checks.
- Conduct closed pilot with 10 families and monitor free-tier usage.

## 18) Next Execution Steps
1. Approve MVP scope and prioritized backlog.
2. Create UX wireframes for onboarding, capture, and species confirmation.
3. Define API contracts and event analytics schema.
4. Build clickable prototype for 5-family usability testing.
5. Start sprint planning for Phase 1 implementation.
