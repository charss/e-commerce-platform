FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/product-svc-1.0.0.jar app.jar
COPY config/application-docker.yml ./config/
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "app.jar"]