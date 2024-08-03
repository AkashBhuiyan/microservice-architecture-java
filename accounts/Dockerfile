# Start with a Maven image that contains Java 21 runtime
FROM maven:3.9.7-eclipse-temurin-21 AS build

# Information around who maintains the image
MAINTAINER akash

# Set the working directory inside the container
WORKDIR /app

# Set environment variable for profile (default to dev)
#ARG PROFILE=dev

# Copy the pom.xml and any other required files
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline

# Copy the source code to the container
COPY src ./src

# Clean and package the application using the specified profile
#RUN mvn clean package -DskipTests -P ${PROFILE}
RUN mvn clean package -DskipTests

# Start a new stage from a Java runtime image
FROM eclipse-temurin:21-jdk

# Information around who maintains the image
MAINTAINER akash

# Copy the application's jar from the build stage to the runtime stage
COPY --from=build /app/target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# Execute the application
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]
