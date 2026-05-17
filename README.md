# ResumeAI — Backend Monorepo

An AI‑powered resume builder backend built with **Spring Boot 3, Java 21, Maven, PostgreSQL, Spring Cloud, Spring Security 6, JWT, and OAuth2 (Google + GitHub)**.  

This monorepo follows a **microservices architecture**, orchestrated with Docker Compose, and designed for scalability, modularity, and real‑world deployment.

---

## 🚀 Features
- **Authentication & Security**: JWT, OAuth2 (Google/GitHub), role‑based access.
- **User Profiles**: Manage candidate information.
- **Resume Management**: CRUD operations, versioning, and template selection.
- **AI Service**: Resume suggestions, ATS scoring, powered by external AI APIs.
- **Export Service**: Generate PDF/DOCX resumes.
- **Job Matching**: Ingest job data and score resumes against requirements.
- **Notifications**: Email + in‑app alerts via Mailhog.
- **Billing**: Plans, quotas, Stripe integration.
- **Service Discovery & Gateway**: Eureka registry + API Gateway for routing and centralized auth.

---

## 🏗 Architecture

Frontend → API Gateway → [Auth, User, Resume, Template, AI, Export, JobMatch, Notification, Billing]
│
└── Eureka Server (Service Registry)


- **Gateway**: Single entry point, JWT validation, CORS handling.  
- **Eureka**: Service registry for dynamic routing.  
- **Microservices**: Each service independently deployable with its own DB.  
- **Postgres**: Dedicated database per service, managed via Flyway migrations.  
- **Docker Compose**: One command brings up all services + infra.  

---

## Project Structure

```text
AI-Powered-Resume-Builder/
├── api-gateway/
├── auth-service/
├── resume-service/
├── section-service/
├── ai-service/
├── template-service/
├── export-service/
├── notification-service/
├── jobmatch-service/
└── eureka-server/
```

## Microservices

| Service | Port | Description |
|---|---:|---|
| Eureka Server | `8761` | Service discovery server |
| API Gateway | `8080` | Main entry point for all frontend API requests |
| Auth Service | `8081` | Authentication, users, roles, OAuth, payments, subscriptions |
| Resume Service | `8082` | Resume creation and resume management |
| Section Service | `8083` | Resume section management |
| AI Service | `8084` | AI resume content generation and ATS tools |
| Template Service | `8085` | Resume templates and preview handling |
| Export Service | `8086` | Resume export and download handling |
| Notification Service | `8088` | App and email notification handling |
| Job Match Service | `8089` | Job matching and job-fit analysis |


---

## 🔑 Key Highlights
- **Microservices First**: Each service has its own Dockerfile, DB, and config.  
- **Scalable Design**: Independent deployment, easy horizontal scaling.  
- **Developer Friendly**: Run everything with `docker-compose up -d`.  
- **Testing Ready**: Mailhog for email testing, Swagger UI for APIs.  
- **Cloud Ready**: OAuth2 integration, Stripe billing, AI APIs.  

---

## 📊 Branching Strategy
- **main** → clean production branch.  
- **dev** → integration branch with root files + merged features.  
- **feature/** → per‑service branches (e.g., `feature/auth-service`, `feature/user-service`).  

---

## 🧪 Testing & Debugging
- **Swagger UI**: `http://localhost:808X/swagger-ui.html` (per service).  
- **Eureka Dashboard**: `http://localhost:8761`.  
- **Gateway Health**: `http://localhost:8080/actuator/health`.  
- **Mailhog Inbox**: `http://localhost:8025`.  

---

## Prerequisites

Install the following before running locally:

- Java 17
- Maven
- MySQL 8
- Redis
- RabbitMQ
- Git

## Local Setup

1. Clone the repository.

```bash
git clone https://github.com/Muskann05/AI-Powered-Resume-Builder.git
cd AI-Powered-Resume-Builder
```

2. Create databases in MySQL.

```sql
CREATE DATABASE resumeai_auth;
CREATE DATABASE resumeai_resume;
CREATE DATABASE resumeai_section;
CREATE DATABASE resumeai_template;
CREATE DATABASE resumeai_ai;
CREATE DATABASE resumeai_export;
CREATE DATABASE resumeai_jobmatch;
CREATE DATABASE resumeai_notification;
```

3. Update each service configuration.

Each service has its own `src/main/resources/application.properties`. For local development, configure:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
spring.datasource.username=root
spring.datasource.password=<your_password>
```

4. Start services in this order.

```bash
cd eureka-server
mvn spring-boot:run
```

Then start:

```bash
cd api-gateway
mvn spring-boot:run
```

Then start the required business services:

```bash
cd auth-service
mvn spring-boot:run
```

```bash
cd resume-service
mvn spring-boot:run
```

```bash
cd section-service
mvn spring-boot:run
```

```bash
cd template-service
mvn spring-boot:run
```

Run other services as needed:

```bash
cd ai-service
mvn spring-boot:run
```

```bash
cd export-service
mvn spring-boot:run
```

```bash
cd jobmatch-service
mvn spring-boot:run
```

```bash
cd notification-service
mvn spring-boot:run
```

## Important Environment Variables

Use environment variables in Render or production. Do not commit real secrets to GitHub.

```env
SPRING_DATASOURCE_URL=jdbc:mysql://<host>:<port>/<database>?ssl-mode=REQUIRED
SPRING_DATASOURCE_USERNAME=<database_user>
SPRING_DATASOURCE_PASSWORD=<database_password>

GOOGLE_CLIENT_ID=<google_oauth_client_id>
GOOGLE_CLIENT_SECRET=<google_oauth_client_secret>
LINKEDIN_CLIENT_ID=<linkedin_client_id_or_dummy_if_not_used>
LINKEDIN_CLIENT_SECRET=<linkedin_client_secret_or_dummy_if_not_used>

APP_JWT_SECRET=<strong_jwt_secret>
APP_OAUTH2_REDIRECT_URL=<frontend_oauth_success_url>
APP_FRONTEND_RESET_PASSWORD_URL=<frontend_reset_password_url>

SPRING_DATA_REDIS_HOST=<redis_host>
SPRING_DATA_REDIS_PORT=<redis_port>

SPRING_RABBITMQ_HOST=<rabbitmq_host>
SPRING_RABBITMQ_PORT=<rabbitmq_port>

GEMINI_API_KEY=<gemini_api_key>
RAZORPAY_KEY_ID=<razorpay_key_id>
RAZORPAY_KEY_SECRET=<razorpay_key_secret>
```

## Build

Build an individual service:

```bash
cd auth-service
mvn clean package
```

Skip tests during deployment build:

```bash
mvn clean package -DskipTests
```

## Swagger and Health Check

Each service exposes Swagger and actuator endpoints when enabled:

```text
http://localhost:<port>/swagger-ui.html
http://localhost:<port>/actuator/health
```

Example:

```text
http://localhost:8081/swagger-ui.html
http://localhost:8081/actuator/health
```

  
## Project Explanation for Evaluation

ResumeAI solves the problem of manual resume creation by providing a complete resume-building workflow. A user can register, select a template, create a resume, add sections, improve content using AI, check job fit, and export the final resume. The backend follows a microservice architecture, where each feature is developed as a separate service. This makes the system modular, maintainable, and easier to deploy independently.

## Author

Developed by Poonam Saraswat

