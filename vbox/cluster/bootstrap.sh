#!/usr/bin/env bash

echo config anarchia coreos node


# aggiungo un alias per il nostro registro docker
touch /etc/hosts

#docker registry gira sull'host
bash -c "echo 10.0.2.2 anarchia-registry >> /etc/hosts"

#docker registry gira su viewport
#bash -c "echo 172.17.8.10 anarchia-registry >> /etc/hosts"


sudo mkdir -p /etc/docker/certs.d/anarchia-registry:5000
sudo cp /anarchia/certs/domain.crt /etc/docker/certs.d/anarchia-registry:5000/ca.crt

mkdir -p /home/core/log
                 

#fleetctl start /anarchia/script/registry.service

# predispone il registro di docker 2.0 
#openssl req -newkey rsa:2048 -nodes -keyout certs/domain.key -x509 -days 3065 -out certs/domain.crt
#docker run -d -p 5000:5000 --restart=always --name registry -v /anarchia/certs:/certs   -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt   -e REGISTRY_HTTP_TLSEY=/certs/domain.key registry:2

# questo per la macchina di sviluppo, per usare il registro privato
#sudo mkdir -p /etc/docker/certs.d/anarchia-registry:5000
#sudo cp /anarchia/certs/domain.crt /etc/docker/certs.d/anarchia-registry:5000/ca.crt

# aggiungo un alias per il registry
#bash -c "echo  192.168.50.1 anarchia-registry >> /etc/hosts"

