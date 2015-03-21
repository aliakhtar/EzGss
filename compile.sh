#!/usr/bin/env bash

#A convenience script that compiles, and copies the resulting .jar to the home directory
# for easier usage in future. Its not necessary to run this script, mvn clean install
# works directlry as well

mvn clean install
cp ./target/ez*.jar ~/ezGss.jar
