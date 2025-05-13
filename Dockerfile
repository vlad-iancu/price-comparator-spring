# Stage 1: Build using OpenJDK 17 + Maven
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY . .

# Make sure mvnw is executable and run the build
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Run with minimal OpenJDK 17 runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
