# api-gateway
Single entry point for the frontend. Validates JWT, forwards `X-User-Id` to downstream services, handles CORS, and routes based on path.
- Port: **8080**
- Public paths: `/api/auth/register`, `/api/auth/login`, `/api/auth/refresh`, `/oauth2/**`, `/login/oauth2/**`
- Everything else requires `Authorization: Bearer <jwt>`
