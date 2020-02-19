#!/bin/bash

WORKSPACE=/Users/sgenchev
SCRIPT_DIR=/Users/sgenchev/tictactoe
POSTGRES_PORT=5432

( set -x
  mkdir -p ${WORKSPACE}/sandbox/postgresql/init
  cp ${SCRIPT_DIR}/createdb.sql ${WORKSPACE}/sandbox/postgresql/init
)

( set -x
  rm -r ${WORKSPACE}/sandbox/postgresql/data
  mkdir -p ${WORKSPACE}/sandbox/postgresql/data
  docker run -d --name postgres \
    -p ${POSTGRES_PORT}:5432 \
    --env POSTGRES_PASSWORD=password \
    -v ${WORKSPACE}/sandbox/postgresql/data:/var/lib/postgresql/data \
    -v ${WORKSPACE}/sandbox/postgresql/init:/docker-entrypoint-initdb.d \
    postgres:latest
)
