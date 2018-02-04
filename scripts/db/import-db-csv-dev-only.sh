#!/usr/bin/env bash

# import-db-csv-dev-only.sh
# Restore MySQL tables from separate SQL files for a specified database.


[ $# -lt 4 ] && echo "Usage: $(basename $0) <DB_HOST> <DB_USER> <DB_NAME> <DIR>" && exit 1

DB_HOST=$1
DB_USER=$2
DB=$3
DIR=$4

echo -n "DB password: "
read -s DB_PASW
echo
echo "Restoring tables from separate SQL files for database '$DB' from dir=$DIR"


echo "Loading table definitions..."
for filename in $DIR/*.sql
do
  tablename=`basename $filename`
  mysql -u$DB_USER -p$DB_PASW --host=$DB_HOST < $filename
done
echo "Loaded table definitions."

echo "Importing data..."

# This is why this script is only suitable for dev environments
mysql -u$DB_USER -p$DB_PASW --host=$DB_HOST -e "SET GLOBAL foreign_key_checks=0"

time mysqlimport -u$DB_USER -p$DB_PASW --host=$DB_HOST $DB --use-threads=8 --verbose --local --fields-terminated-by=',' --fields-optionally-enclosed-by='\"' --fields-escaped-by='\"' --default-character-set='utf8' `ls --sort=size  $DIR/*.txt | tr "\n" " "`

mysql -u$DB_USER -p$DB_PASW --host=$DB_HOST -e "SET GLOBAL foreign_key_checks=1"


echo "Imported data."