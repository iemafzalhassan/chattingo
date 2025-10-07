FROM maven:3.9-eclipse-temurin-17 AS deps
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -B -DskipTests dependency:go-offline

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY --from=deps /root/.m2 /root/.m2
COPY . .
RUN mvn -q -e -B -DskipTests package

FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
