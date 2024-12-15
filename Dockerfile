FROM openjdk:8-jdk-slim

# Stage 1: Build the application
WORKDIR /app

# Copy gradle wrapper files and project files
COPY gradlew ./
COPY gradle ./gradle
COPY webapp/ ./

# Grant execute permissions to gradlew
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build  # or `mvn clean install` for Maven

# Stage 2: Run the application
FROM openjdk:8-jre-slim

WORKDIR /app

# Copy the jar file generated in the build stage
COPY --from=0 /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]


