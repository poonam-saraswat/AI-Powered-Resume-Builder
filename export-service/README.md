# export-service
Port **8086**. CRUD for `ExportJob`. All endpoints require `Authorization: Bearer <jwt>` (validated at gateway, user id forwarded as `X-User-Id`).

## Endpoints
| Method | Path |
|---|---|
| GET    | `/api/export` (list mine) |
| GET    | `/api/export/{id}` |
| POST   | `/api/export` |
| PUT    | `/api/export/{id}` |
| DELETE | `/api/export/{id}` |

## DB
`exportdb` (PostgreSQL), Flyway migrations in `src/main/resources/db/migration`.
