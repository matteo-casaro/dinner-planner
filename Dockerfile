
# Use a multi-stage build for smaller image size
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only essential files first to optimize caching
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Use a lightweight JDK image for runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Start the application, allowing Render to inject env vars at runtime
ENTRYPOINT ["java", "-jar", "app.jar"]
