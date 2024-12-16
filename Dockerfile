# Stage 1: Build the application using OpenJDK 17
FROM openjdk:17-jdk AS build

WORKDIR /app

# Copy gradle wrapper files and project files
COPY gradlew ./
COPY gradle ./gradle
COPY webapp/ ./

# Grant execute permissions to gradlew
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build

# Stage 2: Run the application using OpenJDK 17
FROM openjdk:17-jre

WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the default port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
