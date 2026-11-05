FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

COPY target/userService-0.0.1-SNAPSHOT.jar app.jar
RUN unzip -p app.jar application-docker.properties > /dev/null 2>&1 && \
    zip -d app.jar application-docker.properties || true

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]