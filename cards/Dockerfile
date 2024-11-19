# Start with a Maven image that contains Java 21 runtime
FROM maven:3.9.7-eclipse-temurin-21 AS build

# Information around who maintains the image
LABEL maintainer="akash"

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

# Copy an external library (e.g., an OpenTelemetry agent JAR)
RUN mkdir -p /app/libs && \
    cp /root/.m2/repository/io/opentelemetry/javaagent/opentelemetry-javaagent/1.33.5/opentelemetry-javaagent-1.33.5.jar /app/libs/

# Start a new stage from a Java runtime image
FROM eclipse-temurin:21-jdk

# Copy the application's jar from the build stage to the runtime stage
COPY --from=build /app/target/cards-0.0.1-SNAPSHOT.jar cards-0.0.1-SNAPSHOT.jar

# Copy the external library to the runtime stage
COPY --from=build /app/libs/opentelemetry-javaagent-1.33.5.jar /app/libs/opentelemetry-javaagent-1.33.5.jar

# Execute the application
ENTRYPOINT ["java", "-jar", "cards-0.0.1-SNAPSHOT.jar"]
