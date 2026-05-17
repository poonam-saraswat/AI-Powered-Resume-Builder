# syntax=docker/dockerfile:1
FROM maven:3.9-eclipse-temurin-21 AS build
ARG SERVICE_NAME=template-service
WORKDIR /workspace

# Copy parent pom and ALL module poms first (better Docker cache)
COPY pom.xml ./pom.xml
COPY auth-service/pom.xml          auth-service/pom.xml
COPY user-service/pom.xml          user-service/pom.xml
COPY resume-service/pom.xml        resume-service/pom.xml
COPY template-service/pom.xml      template-service/pom.xml
COPY ai-service/pom.xml            ai-service/pom.xml
COPY export-service/pom.xml        export-service/pom.xml
COPY jobmatch-service/pom.xml      jobmatch-service/pom.xml
COPY billing-service/pom.xml       billing-service/pom.xml
COPY notification-service/pom.xml  notification-service/pom.xml
COPY eureka-server/pom.xml         eureka-server/pom.xml
COPY api-gateway/pom.xml           api-gateway/pom.xml

# Copy only this service's source
COPY ${SERVICE_NAME}/src ${SERVICE_NAME}/src

RUN mvn -q -pl ${SERVICE_NAME} -am -DskipTests package

FROM eclipse-temurin:21-jre-alpine
ARG SERVICE_NAME=template-service
WORKDIR /app
COPY --from=build /workspace/${SERVICE_NAME}/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
