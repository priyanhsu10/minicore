#!/bin/bash

pwd
cd parent-pom
mvn clean package
cd ..
cd minicore-framework
mvn clean package