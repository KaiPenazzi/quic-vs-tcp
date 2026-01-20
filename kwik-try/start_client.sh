#!/bin/bash

./gradlew client:shadowJar
java -jar client/build/libs/client-all.jar
