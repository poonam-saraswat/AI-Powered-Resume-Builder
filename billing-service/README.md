# billing-service
Port **8089**. CRUD for `Subscription`. All endpoints require `Authorization: Bearer <jwt>` (validated at gateway, user id forwarded as `X-User-Id`).

## Endpoints
| Method | Path |
|---|---|
| GET    | `/api/billing` (list mine) |
| GET    | `/api/billing/{id}` |
| POST   | `/api/billing` |
| PUT    | `/api/billing/{id}` |
| DELETE | `/api/billing/{id}` |

## DB
`billingdb` (PostgreSQL), Flyway migrations in `src/main/resources/db/migration`.
