# ResumeAI — Backend Monorepo

A complete microservices backend for an AI-powered resume builder.
**Spring Boot 3 · Java 21 · Maven · PostgreSQL · Spring Cloud (Eureka + Gateway) · Spring Security 6 + JWT + OAuth2 (Google + GitHub)**

Everything runs with **one command**: `docker-compose up -d`.

---

## 1. What's inside

```
resumeai-backend/
├── pom.xml                    # Maven parent (declares all 11 modules)
├── docker-compose.yml         # Brings up everything: 11 services + 9 Postgres DBs + Mailhog
├── .env.example               # Copy to .env and fill secrets
├── Makefile                   # Shortcuts: make up / make logs svc=auth-service
├── eureka-server/             # Service registry (port 8761)
├── api-gateway/               # Single entry point + JWT validation (port 8080)
├── auth-service/              # Register / login / OAuth2 (Google, GitHub) (port 8081)
├── user-service/              # Profiles                 (port 8082)
├── resume-service/            # Resume CRUD + versions   (port 8083)
├── template-service/          # Template catalog         (port 8084)
├── ai-service/                # AI suggestions, ATS score (port 8085)
├── export-service/            # PDF / DOCX export jobs   (port 8086)
├── jobmatch-service/          # Job ingestion + scoring  (port 8087)
├── notification-service/      # Email + in-app notifs    (port 8088)
└── billing-service/           # Plans, quotas, Stripe    (port 8089)
```

### Why each piece exists

| File / service | What it does | Why you need it |
|---|---|---|
| **`pom.xml` (root)** | Maven multi-module parent. Declares Spring Boot version, Java 21, and all 11 child modules. | One source of truth for dependency versions. `mvn package` from root builds everything. |
| **`Dockerfile` (per service)** | Multi-stage build: Maven image compiles the jar, Alpine JRE runs it. | Each service ships as its own ~150 MB image — independently deployable. |
| **`docker-compose.yml`** | Orchestrates all containers, networks, and volumes. | Lets you run the whole platform locally with one command — no manual setup. |
| **`eureka-server`** | Service registry. Every service registers on startup; gateway looks up services by name. | So `api-gateway` can send a request to `lb://auth-service` instead of hardcoding `http://auth-service:8081`. Also enables zero-downtime deploys. |
| **`api-gateway`** | Single public entry point (port 8080). Validates JWTs, applies CORS, forwards `X-User-Id` to downstream services. | Frontend only knows ONE URL. Auth is centralized. |
| **`application.yml`** (per service) | Spring config: DB URL, port, Eureka URL, JWT secret. Reads env vars with `${VAR:default}`. | Lets the same jar run differently in dev/prod by changing env. |
| **`Flyway` migrations** (`db/migration/V1__init.sql`) | Database schema, version-controlled. | Schema changes ship with code. No manual `psql` ever needed. |
| **`Mailhog`** | Fake SMTP server with web UI at http://localhost:8025. | Test password-reset / verification emails without a real Gmail account. |

---

## 2. Prerequisites

- **Docker Desktop** (or Docker Engine + Compose v2) — that's literally all you need to run it
- (Optional) **JDK 21** + **Maven 3.9+** if you want to run services from your IDE
- (Optional) Postman / curl for testing

---

## 3. Files YOU must edit before first run

> These are the ONLY files where you'll need to put your own values.

### A. `.env` — REQUIRED
Copy and edit:
```bash
cp .env.example .env
```
Then fill in:

| Variable | Where to get it | Required? |
|---|---|---|
| `JWT_SECRET` | Any random string ≥32 chars. Generate with `openssl rand -hex 32` | **Yes** |
| `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` | See **Section 5 — Google OAuth setup** below | Only if you want Google login |
| `GITHUB_CLIENT_ID` / `GITHUB_CLIENT_SECRET` | See **Section 6 — GitHub OAuth setup** below | Only if you want GitHub login |
| `LOVABLE_API_KEY` *or* `OPENAI_API_KEY` | Lovable workspace settings, or platform.openai.com | Only if you want real AI responses |
| `STRIPE_SECRET_KEY` | dashboard.stripe.com → Developers → API keys | Only if you want billing |
| `FRONTEND_REDIRECT_URI` | Where your frontend lives. Default `http://localhost:4200/oauth/callback`. **Change if your frontend isn't on port 4200.** | Only if frontend is elsewhere |

### B. `docker-compose.yml` — only if ports clash
The compose file uses these host ports: **8080, 8761, 8081–8089, 1025, 8025**, plus the Postgres containers (NOT exposed to host by default — safe).
If any are already in use on your machine, change the left side of the `ports:` mapping (e.g. `"9080:8080"`).

### C. `api-gateway/src/main/resources/application.yml` — only if frontend is on a non-default port
The default CORS origins are `http://localhost:4200,http://localhost:3000,http://localhost:5173`. Either add your frontend URL to `CORS_ORIGINS` in `.env`, or edit the YAML directly.

### D. Nothing else.
DB usernames/passwords default to `resumeai/resumeai` — fine for local dev. For production, change `POSTGRES_USER`/`POSTGRES_PASSWORD` in `.env`.

---

## 4. Run it

```bash
cp .env.example .env
# edit .env and put your secrets / OAuth client IDs

make up           # or:  docker-compose up -d --build
make ps           # check everything is healthy
make logs svc=auth-service
```

First build takes 5–10 minutes (Maven downloads dependencies inside each Dockerfile). Subsequent builds are cached.

Open:
- **Eureka dashboard**: http://localhost:8761 — should show all 9 services registered
- **Gateway health**: http://localhost:8080/actuator/health
- **Mailhog (test inbox)**: http://localhost:8025
- **Swagger** for any service: http://localhost:808X/swagger-ui.html (X = service port)

### Smoke test
```bash
# 1. Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"a@b.com","password":"password123","fullName":"Alice"}'

# Save the accessToken from the response, then:
TOKEN=...

# 2. Get my profile
curl http://localhost:8080/api/auth/me -H "Authorization: Bearer $TOKEN"

# 3. Create a resume
curl -X POST http://localhost:8080/api/resumes \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"title":"My CV","contentJson":"{}","templateId":"modern","version":1}'
```

---

## 5. Google OAuth setup

1. Go to https://console.cloud.google.com → create or pick a project.
2. **APIs & Services → OAuth consent screen** → External → fill app name, support email, save.
3. **APIs & Services → Credentials → Create Credentials → OAuth client ID**.
   - Application type: **Web application**
   - Authorized redirect URIs:
     ```
     http://localhost:8080/login/oauth2/code/google
     ```
4. Copy the **Client ID** and **Client secret** into `.env` as `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET`.
5. `make rebuild svc=auth-service`.
6. Test: open http://localhost:8080/oauth2/authorization/google in your browser. You'll be redirected to Google, then back to `${FRONTEND_REDIRECT_URI}?accessToken=...&refreshToken=...`.

---

## 6. GitHub OAuth setup

1. Go to https://github.com/settings/developers → **OAuth Apps → New OAuth App**.
2. Fields:
   - Homepage URL: `http://localhost:4200`
   - Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
3. Copy **Client ID** + generate a **Client secret** → put in `.env` as `GITHUB_CLIENT_ID` / `GITHUB_CLIENT_SECRET`.
4. `make rebuild svc=auth-service`.
5. Test: http://localhost:8080/oauth2/authorization/github

---

## 7. Service communication map

```
Frontend ──► api-gateway (8080) ──► auth-service       (login, OAuth, JWT)
                                ├──► user-service      (profiles)
                                ├──► resume-service    (CRUD resumes)
                                ├──► template-service  (templates)
                                ├──► ai-service        (suggestions, ATS) ──► Lovable AI Gateway
                                ├──► export-service    (PDF/DOCX)
                                ├──► jobmatch-service  (job match scores)
                                ├──► notification-service (email, in-app)
                                └──► billing-service   (plans, quotas)
                                          │
                              all register with ▼
                                  eureka-server (8761)
```

- **Sync calls** between services use **OpenFeign** + Eureka (e.g., ai-service can call `resume-service` by name).
- **Auth context** is propagated via `X-User-Id` header that the gateway adds after JWT validation.

---

## 8. Branch-wise pushing

Each service is self-contained under its own folder. Suggested branch order:

```
main              ← infra (pom, compose, env, Makefile)
└─ feat/eureka              eureka-server/
   └─ feat/gateway          api-gateway/
      └─ feat/auth          auth-service/         ← biggest; Google + GitHub OAuth
         └─ feat/user       user-service/
            └─ feat/resume  resume-service/
               └─ feat/template  template-service/
                  └─ feat/ai     ai-service/
                     └─ feat/export   export-service/
                        └─ feat/jobmatch  jobmatch-service/
                           └─ feat/notif    notification-service/
                              └─ feat/billing  billing-service/
```

Each service has its own `README.md` you can use as the PR description.

---

## 9. Troubleshooting

| Symptom | Fix |
|---|---|
| `Port 8080 already in use` | Change host port in `docker-compose.yml` |
| `Eureka dashboard empty` | Wait 30–60s after `make up`; services register lazily. Check `make logs svc=auth-service`. |
| `401 Unauthorized` on `/api/resumes` | Header missing or expired. Hit `/api/auth/refresh` with your refresh token. |
| OAuth redirect mismatch | The redirect URI in Google/GitHub console MUST be exactly `http://localhost:8080/login/oauth2/code/{provider}` |
| AI returns `[AI disabled ...]` | Set `LOVABLE_API_KEY` (or `OPENAI_API_KEY`) in `.env`, then `make rebuild svc=ai-service` |
| Flyway error on second run | Schema mismatch. Nuke and start fresh: `make clean && make up` |
| Build fails: `Cannot find pom.xml` | You ran a Dockerfile from inside its own folder. Build context must be the repo root — that's what `docker-compose` does for you. |

---

## 10. Run individual services from your IDE (optional)

If you prefer running a service from IntelliJ for debugging while the rest stay in Docker:

1. `docker-compose up -d eureka-server pg-auth` (only infra)
2. In IntelliJ, run `AuthServiceApplication.main` with env vars:
   ```
   DB_URL=jdbc:postgresql://localhost:5432/authdb   # but you'll need to expose pg-auth port first
   EUREKA_URL=http://localhost:8761/eureka
   JWT_SECRET=...
   ```
3. The IDE-run service will register with Eureka in Docker and be reachable via the gateway just like the Dockerized ones.

To expose a Postgres port to the host, add to its compose entry: `ports: ["5432:5432"]`.

---

## 11. License

MIT — do whatever, no warranty.
