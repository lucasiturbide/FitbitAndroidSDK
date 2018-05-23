#!/usr/bin/env bash

#rm -rf gen
#rm -rf src/main/java/com/mindbodyonline/clients/connect/v1/{model,api,service}
mkdir gen
cp ./.swagger-codegen-ignore gen/

./swagger-codegen generate \
    -i fitbit-swagger.json \
    -l java \
    -c fitbit.json \
    -o gen

#cp -r gen/src/main/java/com src/main/java
