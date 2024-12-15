# Stage 1: Build the application
FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and configuration files
COPY gradlew ./
COPY gradle ./gradle
COPY webapp/ ./

# Grant execute permissions to the Gradle wrapper
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build --no-daemon  # Avoids creating a Gradle daemon for improved container build consistency

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the default port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
