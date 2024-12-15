# Stage 1: Build the application
FROM openjdk:8-jdk-slim AS build

WORKDIR /app

# Copy build files and build the application (use your build tool, e.g., Maven or Gradle)
COPY webapp/ ./
RUN ./gradlew build  # or `mvn clean install` for Maven

# Stage 2: Run the application
FROM openjdk:8-jre-slim

WORKDIR /app

# Copy only the built JAR from the previous stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port for the application
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]

