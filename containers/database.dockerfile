FROM mysql:8.0.20
RUN export $(cat .env | xargs)

ENV MYSQL_DATABASE=${MYSQL_DATABASE} \ 
    MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
    MYSQL_USER=${DB_USER} \
    MYSQL_PASSWORD=${DB_PASSWORD}

# ADD spring.sql /docker-entrypoint-initdb.d

EXPOSE 3306
