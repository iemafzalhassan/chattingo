### Stage 1: Maven Build Environment ###
FROM maven:3.9.4-eclipse-temurin-17 AS maven-deps
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B


### Stage 2: Build Spring Boot Application ###
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy dependencies from previous stage
COPY --from=maven-deps /root/.m2 /root/.m2

# Copy source code
COPY src ./src
COPY pom.xml .

# Build the JAR file
RUN mvn clean package -DskipTests


### Stage 3: JRE Runtime ###
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose backend port
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

