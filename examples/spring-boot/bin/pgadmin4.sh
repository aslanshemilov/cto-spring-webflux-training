#!/bin/bash

docker run --name pgadmin -p 80:80 -e "PGADMIN_DEFAULT_EMAIL=admin@example.com" -e "PGADMIN_DEFAULT_PASSWORD=Abcd1234.-" -d --rm dpage/pgadmin4
