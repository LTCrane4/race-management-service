FROM mysql:8.0.20
RUN export $(cat .env | xargs)

ENV MYSQL_DATABASE=${MYSQL_DATABASE} \
    MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
    MYSQL_USER=${DB_USER} \
    MYSQL_PASSWORD=${DB_PASSWORD}

# We have to specifiy containers since the compose build context is the root directory
ADD ./containers/init-db.sh /docker-entrypoint-initdb.d/

EXPOSE 3306
