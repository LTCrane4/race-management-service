#!/env/bin/bash
# Init script for mysql database

echo "***********************"
echo "Initializing database"
echo $MYSQL_DATABASE

# Grant privileges to user
mysql -u root -p$MYSQL_ROOT_PASSWORD -e \
"CREATE DATABASE IF NOT EXISTS $MYSQL_DATABASE;
GRANT ALL PRIVILEGES ON $MYSQL_DATABASE.* TO '$DB_USER'@'%' IDENTIFIED BY '$DB_PASSWORD';
FLUSH PRIVILEGES;"

## Create users table (if not exists)
#mysql -u $DB_USER -p$DB_PASSWORD -e \
#"USE $MYSQL_DATABASE;
#CREATE TABLE IF NOT EXISTS user
# (
# id varchar(255) not null primary key,
# email varchar(255) null,
# first_name varchar(255) null,
# last_name varchar(255) null,
# password varchar(255) null,
# user_type varchar(255) null,
# username varchar(255) null);"
