# Используем OpenJDK 17 базовый образ
FROM openjdk:17-jdk-slim as build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY . .

# Собираем проект с помощью Maven, пропуская тесты
RUN ./mvnw clean install -DskipTests

# Создаем новый образ для выполнения приложения
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл из стадии сборки
COPY --from=build /app/target/task-mgt-system-0.0.1-SNAPSHOT.jar /app/task-mgt-system-0.0.1-SNAPSHOT.jar

# Запускаем приложение
#ENTRYPOINT ["java", "-jar", "/app/task-mgt-system-0.0.1-SNAPSHOT.jar"]
# Копируем скрипт ожидания из папки docker в образ
COPY docker/wait-and-start.sh /wait-and-start.sh

# Делаем скрипт исполняемым
RUN chmod +x /wait-and-start.sh

# Запускаем приложение
ENTRYPOINT ["/wait-and-start.sh"]