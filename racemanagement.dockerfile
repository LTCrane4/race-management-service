FROM openjdk:17-oracle

WORKDIR /spring-practice
COPY . .
RUN export $(cat .env | xargs)
# RUN ./ci-scripts/docker-build.sh
RUN ./gradlew build

CMD ["java", "-jar", "./build/libs/racemanagement-0.0.1-SNAPSHOT.jar"]
# CMD ["bash", "./ci-scripts/docker-build.sh"]
