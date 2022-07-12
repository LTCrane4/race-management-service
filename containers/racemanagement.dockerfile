FROM gradle:jdk17 AS baseimage

ENV APPLICATION_ROOT=/app
WORKDIR $APPLICATION_ROOT

FROM baseimage AS source
USER root
COPY gradlew $APPLICATION_ROOT
COPY gradle $APPLICATION_ROOT/gradle
LABEL image=SOURCE
ENV APPLICATION_ROOT=/app/
WORKDIR $APPLICATION_ROOT
COPY build.gradle settings.gradle gradle.properties .env $APPLICATION_ROOT
RUN export $(cat .env | xargs)
RUN ./gradlew resolveDependencies --no-daemon
COPY . .

FROM source AS dev
LABEL image=DEV
ENV APPPLICATION_ROOT=/app/
WORKDIR $APPLICATION_ROOT
EXPOSE 8080

CMD java -jar application.jar
