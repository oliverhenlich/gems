# Linux

## General

### Infinite loops
~~~~
while true; do echo "[hit CTRL+C to stop]"; sleep 1; done
~~~~

~~~~
for (( ; ; ))
do
   echo "infinite loops [ hit CTRL+C to stop]"
done
~~~~


## File filtering and searching

### Recursive grep with file filtering
~~~~
grep -R foo --include '*.txt' *
~~~~

### Log lines with a time of 3 digits or more
~~~~
tail -100000 unimarket.performance.log | egrep "time=[[:digit:]]{3,}"
~~~~


## Monitoring
### Print response code of http request
~~~~
curl -k -s -o /dev/null -w "%{http_code}" https://admin.nz.unimarket-staging.com/app/monitor/unimarket-status
~~~~

### Monitor a url for response code
~~~~
#!/bin/bash
for (( ; ; ))
do
   utcDate=`date -u +"%F %T"`
   if curl -k -I -X GET https://admin.nz.unimarket-staging.com/app/monitor/unimarket-status 2>/dev/null | head -n 1 | cut -d$' ' -f2 | grep --quiet "200" ; then

     echo "$utcDate - OK"
   else
     echo "$utcDate - ERROR"
   fi
   sleep 1
done
~~~~

### Prefix ping with date
~~~~
ping admin.nz.unimarket-staging.com | while read result; do echo $(date -u +"%F %T"): $result; done
~~~~