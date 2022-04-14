FROM postgres:latest
RUN export $(cat .env | xargs)

ENV POSTGRES_USER=${DB_USER} \
    POSTGRES_PASSWORD=${DB_PASSWORD} \
    POSTGRES_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}

EXPOSE 5432
