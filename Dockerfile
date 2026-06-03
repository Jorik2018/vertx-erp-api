# Etapa de build
FROM gradle:8.14.4-jdk21 AS build

WORKDIR /app

COPY . .

RUN gradle clean shadowJar -x test

# Etapa runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*-fat.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
