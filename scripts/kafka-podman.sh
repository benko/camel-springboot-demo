#!/bin/bash

podman volume create --ignore zklogs
podman run --rm -d --name zookeeper -p 2181:2181 -v zklogs:/opt/kafka/logs --network kafka registry.redhat.io/amq-streams/kafka-36-rhel9:2.7.0 ./bin/zookeeper-server-start.sh ./config/zookeeper.properties

podman volume create --ignore kafkalogs
podman secret create --replace kafkaconfig ./server.properties
podman run --rm -d --name broker -p 9092:9092 -v kafkalogs:/opt/kafka/logs --secret kafkaconfig,type=mount,target=/opt/kafka/config/server.properties --network kafka registry.redhat.io/amq-streams/kafka-36-rhel9:2.7.0 ./bin/kafka-server-start.sh ./config/server.properties

