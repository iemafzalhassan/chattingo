# ---

# Backend Dockerfile - 3-stage build

# ---

# Stage 1: Dependencies

# Base Image
FROM maven:3.9.4-eclipse-temurin-17 AS deps

# Work Dir
WORKDIR /app

# Copy
COPY pom.xml ./

# Run
RUN mvn dependency:go-offline -B

# ---

# Stage 2: Build

# Base Image
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Work Dir
WORKDIR /app

# Copy
COPY --from=deps /root/.m2 /root/.m2
COPY pom.xml ./
COPY src ./src

# Run
RUN mvn clean package -DskipTests -B

# ---

# Stage 3: Runtime

# Base Image
FROM eclipse-temurin:17-jre-alpine

# Work Dir
WORKDIR /app

# Run
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

# Copy
COPY --from=builder /app/target/*.jar app.jar
RUN chown appuser:appuser app.jar

# User
USER appuser

# Expose Port
EXPOSE 8080

# Environment
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# Final Run
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
