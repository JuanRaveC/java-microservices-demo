#!/bin/bash
docker run --restart=unless-stopped ^
        --hostname graylog.cedesistemas.local ^
        --net cedesistemas_network ^
        --name graylog.cedesistemas.local ^
        --memory=512m ^
        -e "TZ=America/Bogota" ^
        -e "GRAYLOG_HTTP_EXTERNAL_URI=http://graylog.cedesistemas.local:9000/" ^
        -e "GRAYLOG_PASSWORD_SECRET=4orA3qXbqd1KDeOR" ^
        -e "GRAYLOG_ROOT_PASSWORD_SHA2=1c9e2846648a1e29d4c2d52fc4a78e1e0c884e858385029b5dada3ba1bd034a5" ^
        -e "GRAYLOG_ELASTICSEARCH_HOSTS=http://elasticsearch.cedesistemas.local:9200" ^
        -e "GRAYLOG_ELASTICSEARCH_SHARDS=1" ^
        -e "GRAYLOG_ELASTICSEARCH_REPLICAS=0" ^
        -e "GRAYLOG_MONGODB_URI=mongodb://mongodb.cedesistemas.local:27017/graylog" ^
        -p 9000:9000 ^
        -p 12201:12201 ^
        -p 12202:12202 ^
        -p 1514:1514 ^
        -p 5555:5555/udp ^
        -d graylog/graylog:3.3
