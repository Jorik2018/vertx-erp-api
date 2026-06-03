# Etapa de build
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

# Copia primero archivos de dependencias para aprovechar cache
COPY pom.xml .
COPY src ./src

# Compila y empaqueta
RUN mvn clean package -DskipTests

# Etapa runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia el jar generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
