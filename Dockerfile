# Use an openjdk base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built Spring Boot JAR file into the container
COPY target/property-0.0.1-SNAPSHOT.jar property.jar

# Set the entrypoint for the container to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/property.jar"]
