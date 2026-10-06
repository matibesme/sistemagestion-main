# Etapa 1: Compilación usando Maven y Amazon Corretto 17
FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Etapa 2: Ejecución usando únicamente el JRE de Amazon Corretto 17
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
