# Stage 1: build
# Start with a Maven image that includes JDK 21
FROM maven:3.9.12-amazoncorretto-21-debian AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY src ./src

# Build source code with maven
RUN ./mvnw --batch-mode --no-transfer-progress package -DskipTests

#Stage 2: create image
# Start with Amazon Correto JDK 21
FROM amazoncorretto:21-alpine

RUN apk add --no-cache libgcc libstdc++ gcompat \
    && addgroup -S spring \
    && adduser -S spring -G spring

# Set working folder to App and copy complied file from above step
WORKDIR /app
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

USER spring:spring
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
