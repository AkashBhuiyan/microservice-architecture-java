# Start with a Maven image that contains Java 21 runtime
FROM maven:3.9.7-eclipse-temurin-21 AS build-common

# Information around who maintains the image
MAINTAINER akash

# Set the working directory for the common module
WORKDIR /build/common

# Copy the common module's pom.xml and source code
COPY common/pom.xml .
COPY common/src ./src

# Build the common module
RUN mvn clean install -DskipTests

# Start a new stage for building the accounts microservice
FROM maven:3.9.7-eclipse-temurin-21 AS build-accounts

# Set the working directory for the accounts module
WORKDIR /build/accounts

# Set environment variable for profile (default to dev)
#ARG PROFILE=dev

# Copy the common module JAR to the local Maven repository
COPY --from=build-common /root/.m2/repository/com/akash/common/ /root/.m2/repository/com/akash/common/

# Copy the accounts pom.xml and source code
COPY accounts/pom.xml .
COPY accounts/src ./src

# Download the project dependencies
#RUN mvn dependency:go-offline

# Build the accounts microservice
#RUN mvn clean package -DskipTests -P ${PROFILE}
RUN mvn clean package -DskipTests

# Start a new stage from a Java runtime image
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Copy the application's jar from the build stage to the runtime stage
COPY --from=build-accounts /build/accounts/target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# Execute the application
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]
