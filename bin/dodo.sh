freePorts=(`netstat -ant | sed -e '/^tcp/ !d' -e 's/^[^ ]* *[^ ]* *[^ ]* *.*[\.:]\([0-9]*\) .*$/\1/' | sort -g | uniq`)

lowerPort=32768
upperPort=61000

#echo "Used ports: ${freePorts[*]}" 
while :; do
    for (( port = lowerPort ; port <= upperPort ; port++ )); do
        if [[ " ${freePorts[*]} " != *" $port "* ]]; then
    		break 2
		fi
    done
done
echo "Next free port: $port"

echo fleetctl stop $1@${port}
fleetctl stop $1@${port}
sleep 3
echo fleetctl start $1@${port}
fleetctl start $1@${port}

#fleetctl list-unit-files
#fleetctl list-units

journalctl -f -u $1@${port}