#!/bin/bash

# Generiert bei ChatGPT
# 1️⃣ Erstelle ein Self-Signed Zertifikat mit SAN
openssl req -x509 -nodes -days 365 \
  -newkey rsa:2048 \
  -keyout server.key \
  -out server.crt \
  -subj "/CN=localhost" \
  -addext "subjectAltName=DNS:localhost,IP:127.0.0.1"

# 2️⃣ Exportiere in PKCS12-Keystore mit Alias 'kwikserver'
openssl pkcs12 -export \
  -in server.crt \
  -inkey server.key \
  -name kwikserver \
  -out keystore.p12 \
  -passout pass:keystorepass

# 3️⃣ Optional: Prüfe Inhalt des Keystores
echo "Folgende Einträge befinden sich im Keystore:"
keytool -list -keystore keystore.p12 -storetype PKCS12 -storepass keystorepass

