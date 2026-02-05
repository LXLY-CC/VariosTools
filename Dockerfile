FROM node:20-alpine AS frontend-builder
WORKDIR /build/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

FROM maven:3.9.9-eclipse-temurin-21 AS backend-builder
WORKDIR /build/backend
COPY backend/pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline
COPY backend/src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC"
COPY --from=backend-builder /build/backend/target/tools-hub-0.0.1-SNAPSHOT.jar app.jar
COPY --from=frontend-builder /build/frontend/dist /app/public
EXPOSE 8080
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
