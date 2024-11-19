# Stage 1: Build the common module using Maven with Java 21 runtime
FROM maven:3.9.7-eclipse-temurin-21 AS build-common

# Information around who maintains the image
LABEL maintainer="akash"

# Set the working directory for the common module
WORKDIR /build/common

# Copy the common module's pom.xml and source code
COPY common/pom.xml .
COPY common/src ./src

# Build the common module
RUN mvn clean install -DskipTests

# Stage 2: Build the accounts microservice
FROM maven:3.9.7-eclipse-temurin-21 AS build-accounts

# Set the working directory for the accounts module
WORKDIR /build/accounts

# Copy the common module JAR to the local Maven repository
COPY --from=build-common /root/.m2/repository/com/akash/common/ /root/.m2/repository/com/akash/common/

# Copy the accounts pom.xml and source code
COPY accounts/pom.xml .
COPY accounts/src ./src

# Build the accounts microservice
RUN mvn clean package -DskipTests

# Copy an external library (e.g., an OpenTelemetry agent JAR)
RUN mkdir -p /accounts/libs && \
    cp /root/.m2/repository/io/opentelemetry/javaagent/opentelemetry-javaagent/1.33.5/opentelemetry-javaagent-1.33.5.jar /accounts/libs/

# Stage 3: Create the runtime image using Java 21 JDK
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Copy the application's jar from the build stage to the runtime stage
COPY --from=build-accounts /build/accounts/target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# Copy the external library to the runtime stage
COPY --from=build-accounts /accounts/libs/opentelemetry-javaagent-1.33.5.jar /app/libs/opentelemetry-javaagent-1.33.5.jar

# Execute the application with the agent
ENTRYPOINT ["java", "-javaagent:/app/libs/opentelemetry-javaagent-1.33.5.jar", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]
