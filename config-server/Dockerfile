
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY config-server/pom.xml .
RUN mvn dependency:go-offline -B

COPY config-server/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN apt-get update && apt-get install -y git curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

COPY config-repo /app/config-repo

RUN cd /app/config-repo && \
    git config --global init.defaultBranch main && \
    git config --global user.email "config@ecommerce.com" && \
    git config --global user.name "Config Server" && \
    (git status 2>/dev/null || (git init && git add . && git commit -m "Initial config" 2>/dev/null || true))

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
