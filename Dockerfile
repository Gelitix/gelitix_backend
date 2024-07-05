FROM maven:3.9.8-sapmachine-22 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:22-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Set the startup command to run your application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]