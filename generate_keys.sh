!# bin/bash

openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout server.key -out server.crt -config openssl_config.cnf -extensions v3_ca

# This command bundles your PEM files into a PKCS#12 file
openssl pkcs12 -export \
    -in server.crt \
    -inkey server.key \
    -name kwikserver \
    -out keystore.p12
