FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

RUN chmod +x ./gradlew
COPY src ./src

RUN ./gradlew build -x test
EXPOSE 8080
CMD ["java", "-jar", "build/libs/percentaje-calculator-0.0.1-SNAPSHOT.jar"]
