# Mysql / Mariadb 

## Mariadb slow query log


~~~~
SET GLOBAL long_query_time = 2; 
SET GLOBAL LOG_QUERIES_NOT_USING_INDEXES = ON;
SET GLOBAL log_output = 'FILE,TABLE';

SET GLOBAL slow_query_log = 'ON';

FLUSH SLOW LOGS ;
FLUSH TABLES; 

SHOW GLOBAL VARIABLES LIKE '%slow%';
SHOW GLOBAL VARIABLES LIKE 'log_queries_not_using_indexes';
SHOW GLOBAL VARIABLES LIKE 'long_query_time';
~~~~

Query it with
~~~~
SELECT * FROM mysql.slow_log;
~~~~

Remember to turn it off again with
~~~~
SET GLOBAL slow_query_log = 'OFF';
FLUSH SLOW LOGS ;
~~~~


## Mariadb general log

**DO NOT LEAVE THIS ON!**

~~~~
SET GLOBAL general_log = 'ON';
SHOW VARIABLES LIKE "general_log%";

# DON'T FORGET
SET GLOBAL general_log = 'OFF';
~~~~

Query it with
~~~~
SELECT * FROM mysql.general_log;
~~~~
