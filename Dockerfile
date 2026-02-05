# Use a base image with JDK 21 (since your compiler is Java 21)
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY ./target/truwiz-backend.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
