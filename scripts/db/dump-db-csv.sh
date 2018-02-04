#!/usr/bin/env bash

# dump-db-csv.sh
# Dump MySQL table data into separate SQL files for a specified database.

[ $# -lt 4 ] && echo "Usage: $(basename $0) <DB_HOST> <DB_USER> <DB_NAME> <DIR>" && exit 1

DB_HOST=$1
DB_USER=$2
DB=$3
DIR=$4

echo -n "DB password: "
read -s DB_PASW
echo
echo "Dumping tables into separate files for database '$DB' into dir=$DIR"

COUNT=0


for TABLE in $(mysql -NBA -h ${DB_HOST} -u ${DB_USER} -p${DB_PASW} -D ${DB} -e " select table_name from INFORMATION_SCHEMA.TABLES where table_schema=schema();")
do
    echo "DUMPING TABLE: $DB.$TABLE schema"
    mysqldump -h ${DB_HOST} -u ${DB_USER} -p${DB_PASW} ${DB} ${TABLE} --no-data --tab ${DIR} --fields-terminated-by=',' --fields-optionally-enclosed-by='\"' --fields-escaped-by='\"' --default-character-set='utf8'

    echo "DUMPING TABLE: $DB.$TABLE data"
    mysqldump -h ${DB_HOST} -u ${DB_USER} -p${DB_PASW} ${DB} ${TABLE} --no-create-info --tab ${DIR} --fields-terminated-by=',' --fields-optionally-enclosed-by='\"' --fields-escaped-by='\"' --default-character-set='utf8'

    gzip ${DIR}/${TABLE}.txt &

    COUNT=$(( COUNT + 1 ))
done

echo "$COUNT tables dumped from database '$DB' into dir=$DIR"
echo
echo "Wait for gzipping background processes to complete"
jobs