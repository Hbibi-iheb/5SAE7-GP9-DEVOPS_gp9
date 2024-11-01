FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

COPY target/gestion-station-ski-1.0.jar app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/app.jar"]
