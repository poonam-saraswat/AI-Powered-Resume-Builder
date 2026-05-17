# template-service
Port **8084**. CRUD for `Template`. All endpoints require `Authorization: Bearer <jwt>` (validated at gateway, user id forwarded as `X-User-Id`).

## Endpoints
| Method | Path |
|---|---|
| GET    | `/api/templates` (list mine) |
| GET    | `/api/templates/{id}` |
| POST   | `/api/templates` |
| PUT    | `/api/templates/{id}` |
| DELETE | `/api/templates/{id}` |

## DB
`templatedb` (PostgreSQL), Flyway migrations in `src/main/resources/db/migration`.
