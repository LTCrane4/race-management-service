FROM openjdk:17-oracle

WORKDIR /spring-practice
COPY . .
RUN export $(cat .env | xargs)
# RUN ./ci-scripts/docker-build.sh
RUN ./gradlew build

CMD ["./gradlew", "bootRun"]
# CMD ["bash", "./ci-scripts/docker-build.sh"]
