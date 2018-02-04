#!/bin/bash

####
# Split MySQL dump SQL file into one file per table
# based on https://gist.github.com/jasny/1608062
####

#adjust this to your case:
START="/-- Table structure for table/"
# or 
#START="/DROP TABLE IF EXISTS/"

DIR="split-dump"
mkdir $DIR

if [ $# -lt 1 ] || [[ $1 == "--help" ]] || [[ $1 == "-h" ]] ; then
        echo "USAGE: extract all tables:"
        echo " $0 DUMP_FILE"
        echo "extract one table:"
        echo " $0 DUMP_FILE [TABLE]"
        exit
fi

echo "Splitting dump"
if [ $# -ge 2 ] ; then
        #extract one table $2
        csplit -s -f$DIR/_table $1 "/-- Table structure for table/" "%-- Table structure for table \`$2\`%" "/-- Table structure for table/" "%40103 SET TIME_ZONE=@OLD_TIME_ZONE%1"
else
        #extract all tables
        csplit -s -f$DIR/_table $1 "$START" {*}
fi
echo "Split dump"

[ $? -eq 0 ] || exit

mv $DIR/_table00 $DIR/_head

#FILE=`ls -1 $DIR/_table* | tail -n 1`
#if [ $# -ge 2 ] ; then
#        mv $FILE $DIR/_foot
#else
#        csplit -b '%d' -s -f$FILE $FILE "/40103 SET TIME_ZONE=@OLD_TIME_ZONE/" {*}
#        mv ${FILE}1 $DIR/_foot
#fi

for FILE in `ls -1 $DIR/_table*`; do
        NAME=`head -n1 $FILE | cut -d$'\x60' -f2`
        #cat $DIR/_head $FILE $DIR/_foot > "$NAME.sql"
        cat $DIR/_head $FILE > "$DIR/$NAME.sql"
        echo "Wrote $NAME.sql"
done

#rm $DIR/_head $DIR/_foot $DIR/_table*
rm $DIR/_head $DIR/_table*
echo "Done"