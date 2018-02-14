#!/usr/bin/env bash


INPUT_FILE=$1
OUTPUT_FILE="$INPUT_FILE-result"
touch ${OUTPUT_FILE}

echo "Reading $INPUT_FILE"
echo "Writing results to $OUTPUT_FILE"
echo

IFS=$'\n'       # make newlines the only separator
set -f          # disable globbing
for LINE in $(cat < "$INPUT_FILE"); do

    LINE=$(echo ${LINE} | tr -d '\r\n')

    FULL_URL=$(echo ${LINE} | awk -F'\t' '{print $5}')

    # Extract the protocol (includes trailing "://").
    PARSED_PROTO="$(echo $FULL_URL | sed -nr 's,^(.*://).*,\1,p')"
    
    # Remove the protocol from the URL.
    PARSED_URL="$(echo ${FULL_URL/$PARSED_PROTO/})"
    
    # Extract the user (includes trailing "@").
    PARSED_USER="$(echo $PARSED_URL | sed -nr 's,^(.*@).*,\1,p')"
    
    # Remove the user from the URL.
    PARSED_URL="$(echo ${PARSED_URL/$PARSED_USER/})"
    
    # Extract the port (includes leading ":").
    PORT="$(echo $PARSED_URL | sed -nr 's,.*(:[0-9]+).*,\1,p'  | tr -d : )"
    
    # Remove the port from the URL.
    PARSED_URL="$(echo ${PARSED_URL/$PORT/})"
    
    # Extract the path (includes leading "/" or ":").
    PARSED_PATH="$(echo $PARSED_URL | sed -nr 's,[^/:]*([/:].*),\1,p')"
    
    # Remove the path from the URL.
    HOST="$(echo ${PARSED_URL/$PARSED_PATH/})"

    SUCCESS=0

    if [ -z "$PORT" ]; then
        if [ "$PARSED_PROTO" == "http://" ]; then
          PORT=80
        elif [ "$PARSED_PROTO" == "https://" ]; then
          PORT=443
        elif [ "$PARSED_PROTO" == "sftp://" ]; then
          PORT=22
        else
          RESULT_FRAGMENT="$RESULT_FRAGMENT\tFAIL (unhandled protocol $FULL_URL)"
          printf "FAIL (unhandled protocol $FULL_URL)\n"
          SUCCESS=-1
        fi
    fi

    RESULT_FRAGMENT="$HOST:$PORT"
    if [ ${SUCCESS} -eq 0 ]; then
        printf "Testing %s:%s ... " ${HOST} ${PORT}

        timeout 2 bash -c "cat < /dev/null > /dev/tcp/$HOST/$PORT"
        SUCCESS=$?
    fi

    if [ ${SUCCESS} -ne 0 ]; then
      RESULT_FRAGMENT="$RESULT_FRAGMENT\tFAIL"
      printf "FAIL\n"
    else
      RESULT_FRAGMENT="$RESULT_FRAGMENT\tSUCCESS"
      printf "SUCCESS\n"
    fi


    echo -e "$LINE\t$RESULT_FRAGMENT" >> ${OUTPUT_FILE}

done

echo Done
echo "Output in $OUTPUT_FILE"