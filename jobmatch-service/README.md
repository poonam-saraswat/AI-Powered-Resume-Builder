# jobmatch-service
Port **8087**. CRUD for `Job`. All endpoints require `Authorization: Bearer <jwt>` (validated at gateway, user id forwarded as `X-User-Id`).

## Endpoints
| Method | Path |
|---|---|
| GET    | `/api/jobs` (list mine) |
| GET    | `/api/jobs/{id}` |
| POST   | `/api/jobs` |
| PUT    | `/api/jobs/{id}` |
| DELETE | `/api/jobs/{id}` |

## DB
`jobmatchdb` (PostgreSQL), Flyway migrations in `src/main/resources/db/migration`.
