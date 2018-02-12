#!/usr/bin/env bash


INPUT_FILE=$1
OUTPUT_FILE="$INPUT_FILE-result"
touch ${OUTPUT_FILE}

echo "Reading $INPUT_FILE"
echo "Writing results to $OUTPUT_FILE"
echo

IFS=$'\n'       # make newlines the only separator
set -f          # disable globbing
for line in $(cat < "$INPUT_FILE"); do
    HOST=$(echo ${line} | awk '{print $1}')
    PORT=$(echo ${line} | awk '{print $2}')
    printf "Testing %s:%s ... " ${HOST} ${PORT}
    RESULT_LINE="$HOST:$PORT"

    timeout 2 bash -c "cat < /dev/null > /dev/tcp/$HOST/$PORT"
    RESULT=$?

    if [ ${RESULT} -ne 0 ]; then
      RESULT_LINE="$RESULT_LINE,FAIL"
      printf "FAIL\n"
    else
      RESULT_LINE="$RESULT_LINE,SUCCESS"
      printf "SUCCESS\n"
    fi

    echo ${RESULT_LINE} >> ${OUTPUT_FILE}

done