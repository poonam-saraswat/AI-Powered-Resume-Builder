# user-service
Port **8082**. CRUD for `Profile`. All endpoints require `Authorization: Bearer <jwt>` (validated at gateway, user id forwarded as `X-User-Id`).

## Endpoints
| Method | Path |
|---|---|
| GET    | `/api/users` (list mine) |
| GET    | `/api/users/{id}` |
| POST   | `/api/users` |
| PUT    | `/api/users/{id}` |
| DELETE | `/api/users/{id}` |

## DB
`userdb` (PostgreSQL), Flyway migrations in `src/main/resources/db/migration`.
