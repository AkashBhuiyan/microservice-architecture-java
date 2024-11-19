# Start with a Maven image containing Java 21 runtime
FROM maven:3.9.7-eclipse-temurin-21 AS build

# Information around who maintains the image
LABEL maintainer="akash"

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and any other required files
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline

# Copy the source code to the container
COPY src ./src

# Clean and package the application
RUN mvn clean package -DskipTests

# Copy the JAR to the desired location directly
RUN mkdir -p /app/libs && \
    cp /root/.m2/repository/io/opentelemetry/javaagent/opentelemetry-javaagent/1.33.5/opentelemetry-javaagent-1.33.5.jar /app/libs/

# Runtime image with Java
FROM eclipse-temurin:21-jre

# Copy the application JAR from the build stage
COPY --from=build /app/target/configserver-0.0.1-SNAPSHOT.jar /app/configserver-0.0.1-SNAPSHOT.jar

# Copy the agent JAR to the runtime stage
COPY --from=build /app/libs/opentelemetry-javaagent-1.33.5.jar /app/libs/opentelemetry-javaagent-1.33.5.jar

# Execute the application
ENTRYPOINT ["java", "-jar", "/app/configserver-0.0.1-SNAPSHOT.jar"]
