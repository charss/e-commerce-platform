FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/payment-svc-1.0.0.jar app.jar
COPY config/application-docker.yml ./config/
EXPOSE 8006
ENTRYPOINT ["java", "-jar", "app.jar"]