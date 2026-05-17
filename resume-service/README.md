# resume-service
Port **8083**. CRUD for `Resume`. All endpoints require `Authorization: Bearer <jwt>` (validated at gateway, user id forwarded as `X-User-Id`).

## Endpoints
| Method | Path |
|---|---|
| GET    | `/api/resumes` (list mine) |
| GET    | `/api/resumes/{id}` |
| POST   | `/api/resumes` |
| PUT    | `/api/resumes/{id}` |
| DELETE | `/api/resumes/{id}` |

## DB
`resumedb` (PostgreSQL), Flyway migrations in `src/main/resources/db/migration`.
