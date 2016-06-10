#!/bin/bash

while true; do
	curl localhost:8080/localSlow &
	sleep 0.01
done
