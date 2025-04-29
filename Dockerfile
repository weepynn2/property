# Use an official OpenJDK image as the base
FROM eclipse-temurin:17-jdk-jammy

# Set application directory
WORKDIR /app

# Copy built JAR file from target folder to the container
COPY target/target/property-0.0.1-SNAPSHOT.jar app.jar

# Default environment variable fallback (can be overridden at runtime)
ENV JAVA_OPTS=""

# Heroku will set PORT and JDBC_DATABASE_URL etc., we just pass them through
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=prod -jar app.jar"]
