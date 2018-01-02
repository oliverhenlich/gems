# OpenSSL

## Download certificate from site
```
echo | openssl s_client -connect www.google.com:443 2>&1 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > olitmp.cert
```

## Inspect certificate
```
openssl x509 -text -in olitmp.cert -fingerprint
```