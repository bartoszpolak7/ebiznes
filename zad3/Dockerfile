FROM dhi.io/gradle:9-jdk25-alpine3.23-dev AS build
WORKDIR /app
COPY . .
RUN gradle buildFatJar --no-daemon

FROM dhi.io/eclipse-temurin:25-alpine3.22-dev
WORKDIR /app
COPY --from=build /app/build/libs/*-all.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]