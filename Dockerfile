# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim as builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем только файлы, необходимые для сборки
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Запускаем сборку проекта
RUN chmod +x gradlew
RUN ./gradlew build -x test

# Финальный образ
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR из builder-образа
COPY --from=builder /app/build/libs/Coursework_5sem-1.0-SNAPSHOT.jar app.jar

# Открываем порт, который использует приложение
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]