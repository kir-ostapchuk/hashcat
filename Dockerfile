FROM openjdk:16-jdk-alpine AS builder
WORKDIR /app
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./
RUN ./gradlew --no-daemon dependencies
COPY src/ src/
RUN ./gradlew --no-daemon build

FROM openjdk:16-jdk-alpine
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "/app/hashcat.jar"]
COPY --from=builder /app/build/libs/hashcat-*.jar hashcat.jar
