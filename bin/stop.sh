#!/bin/bash
PID=$(ps -ef | grep blog-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{ print $2 }')
if [ ${PID} ]; 
then
 echo 'Application is stpping...'
 echo kill $PID DONE
 kill $PID
else
 echo 'Application is already stopped...'
fi