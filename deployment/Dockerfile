#  Build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test
RUN cp ./applications/app-service/build/libs/Request.jar Request.jar

#  Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/Request.jar .
RUN adduser -D request
ENV JAVA_OPTS="  -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
USER request
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar Request.jar" ]