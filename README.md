
# How to run the project using docker
Run in root folder to build all Java services
```bash
mvn clean package -DskipTests
```

Then deploy in docker
```bash
docker-compose up -d
```