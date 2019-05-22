#!/bin/bash

docker run --name zipkin -p 9411:9411 -d --rm openzipkin/zipkin
