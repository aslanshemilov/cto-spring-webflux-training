#!/bin/bash

docker run --name postgres -e "POSTGRES_PASSWORD=Abcd1234.-" -d --rm postgres
