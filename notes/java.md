# Java notes

## JDB - Command line debugger
```
jdb -connect com.sun.jdi.SocketAttach:hostname=localhost,port=9003

stop in com.unimarket.invoicing.domain.AggregateInvoiceState.getName
stop at com.unimarket.core.web.page.LoginForm:104

clear

<now go to login page an login, breakpoint will fire>

print user
dump user
where
step
next
next
cont

clear com.unimarket.core.web.page.LoginForm:104

catch org.springframework.security.AuthenticationException

<now try login with invalid username/password, debugger will catch exception>

next
print cacheWasUsed
dump this.preAuthenticationChecks
ignore org.springframework.security.AuthenticationException

cont
```
