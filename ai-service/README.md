# ai-service
Port **8085**. CRUD for `AiSuggestion`. All endpoints require `Authorization: Bearer <jwt>` (validated at gateway, user id forwarded as `X-User-Id`).

## Endpoints
| Method | Path |
|---|---|
| GET    | `/api/ai` (list mine) |
| GET    | `/api/ai/{id}` |
| POST   | `/api/ai` |
| PUT    | `/api/ai/{id}` |
| DELETE | `/api/ai/{id}` |

## DB
`aidb` (PostgreSQL), Flyway migrations in `src/main/resources/db/migration`.
