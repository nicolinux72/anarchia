#!/bin/bash

while true; do
	curl localhost:8080/do &
	sleep 0.001
done