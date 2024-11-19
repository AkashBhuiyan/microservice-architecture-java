# Stage 1: Build the application using Maven with Java 21 runtime
FROM maven:3.9.7-eclipse-temurin-21 AS build

# Information around who maintains the image
LABEL maintainer="akash"

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download the project dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code to the container
COPY src ./src

# Build the application without running tests
RUN mvn clean package -DskipTests

# Copy an external library (e.g., an OpenTelemetry agent JAR)
RUN mkdir -p /app/libs && \
    cp /root/.m2/repository/io/opentelemetry/javaagent/opentelemetry-javaagent/1.33.5/opentelemetry-javaagent-1.33.5.jar /app/libs/

# Stage 2: Create the runtime image using Java 21 JDK
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the application's jar from the build stage to the runtime stage
COPY --from=build /app/target/eurekaserver-0.0.1-SNAPSHOT.jar /app/eurekaserver-0.0.1-SNAPSHOT.jar

# Copy the external library to the runtime stage
COPY --from=build /app/libs/opentelemetry-javaagent-1.33.5.jar /app/libs/opentelemetry-javaagent-1.33.5.jar

# Set the entry point to include the agent if needed
ENTRYPOINT ["java", "-jar", "/app/eurekaserver-0.0.1-SNAPSHOT.jar"]
