#!/bin/bash
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
$JAVA_HOME/bin/java -DPORT=9800 -jar build/libs/payment-gateway-0.0.1-SNAPSHOT.jar
