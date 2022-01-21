FROM openjdk:17-oracle AS image

ENV APPLICATION_ROOT=/app
WORKDIR $APPLICATION_ROOT

FROM image AS source
USER root
COPY ../gradlew $APPLICATION_ROOT
COPY ../gradle $APPLICATION_ROOT/gradle
LABEL image=SOURCE
ENV APP_HOME=/app/
WORKDIR $APPLICATION_ROOT
COPY ../build.gradle settings.gradle gradle.properties .env $APPLICATION_ROOT
RUN export $(cat .env | xargs)
COPY .. .

FROM source AS dev
LABEL image=DEV
ENV APP_HOME=/app/
WORKDIR $APPLICATION_ROOT
EXPOSE 8080

FROM source AS build
LABEL image=TEST
ENV APP_HOME=/app/
WORKDIR $APPLICATION_ROOT
RUN ./gradlew build

FROM base AS release
LABEL image=release
ENV APP_HOME=/app/
WORKDIR $APPLICATION_ROOT
COPY --from=BUILD $APPLICATION_ROOT/build/libs/racemanagement-0.0.1-SNAPSHOT.jar ./application.jar
EXPOSE 8080

CMD java -jar application.jar
