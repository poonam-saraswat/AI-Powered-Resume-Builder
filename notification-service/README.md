# notification-service
Port **8088**. CRUD for `Notification`. All endpoints require `Authorization: Bearer <jwt>` (validated at gateway, user id forwarded as `X-User-Id`).

## Endpoints
| Method | Path |
|---|---|
| GET    | `/api/notifications` (list mine) |
| GET    | `/api/notifications/{id}` |
| POST   | `/api/notifications` |
| PUT    | `/api/notifications/{id}` |
| DELETE | `/api/notifications/{id}` |

## DB
`notificationdb` (PostgreSQL), Flyway migrations in `src/main/resources/db/migration`.
