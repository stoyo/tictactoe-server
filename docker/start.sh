#!/bin/sh -e

SERVICE_HOME=/opt/service

status=0
set +e

java "$@" -jar $SERVICE_HOME/tictactoe.jar

status=$?
set -e

exit ${status}
