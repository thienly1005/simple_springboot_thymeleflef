# Stage 1: Build ứng dụng
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/StudentManagement-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
