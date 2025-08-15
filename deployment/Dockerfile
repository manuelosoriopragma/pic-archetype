FROM gradle:8.13-jdk21@sha256:67b8c4bfd2b064e58a7307e2da1fc3881bc03ecc7a57cf61d8b570a02ebfaea2 AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build
USER 1000:1000
FROM eclipse-temurin:21-jre-alpine@sha256:8728e354e012e18310faa7f364d00185277dec741f4f6d593af6c61fc0eb15fd
ARG arg_api_port
ENV ENV_API_PORT=$arg_api_port
EXPOSE $ENV_API_PORT

RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/America/Bogota /etc/localtime && \
    echo "America/Bogota" > /etc/timezone

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

VOLUME /tmp
COPY --from=build /home/gradle/src/applications/app-service/build/libs/*.jar app.jar
ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]