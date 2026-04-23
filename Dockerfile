FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p uploads/documentos
COPY --from=build /app/uploads/documentos/documento_prueba.pdf uploads/documentos/documento_prueba.pdf
ENTRYPOINT ["java","-jar","/app/app.jar"]
EXPOSE 8080