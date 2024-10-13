#!/bin/bash

MYDIR="$(cd $(dirname $0) && pwd)"

podman volume create --ignore pgdata
podman run -d --rm --name postgres -p 5432:5432 -v pgdata:/var/lib/pgsql/data -e POSTGRESQL_USER=developer -e POSTGRESQL_PASSWORD=developer -e POSTGRESQL_DATABASE=items registry.access.redhat.com/rhel9/postgresql-16:latest 

sleep 3
cat ${MYDIR}/postgres-init.sql | podman exec -i postgres psql items

