#!/usr/bin/env bash

# Nicola Santi 2013
# installa quanto serve alla macchina viewpoint
# su una ubuntu 14.04 nuova fiammante
#
# Go cd, Git repository, Docker Registry
# Rabbit MQ Manager

#vagrant plugin install vagrant-vbguest


# aggiungo un alias per il nostro repository svn
bash -c "echo 137.204.25.142  deposito >> /etc/hosts"

#repo for maven e java 8
add-apt-repository -y ppa:andrei-pozolotin/maven3

#to silent accept Oracle license and instalo jdk 8
add-apt-repository -y ppa:webupd8team/java

# installo  java 8, maven3 e subversion
apt-get update
apt-get install -y unzip

echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | sudo debconf-set-selections
apt-get install -y oracle-java8-installer
apt-get install -y maven3 subversion apache2

#docker
wget -qO- https://get.docker.com/ | sh
usermod -aG docker vagrant

# predispongo per la configurazione di node.js 
# che sarà poi completata utilizzando il file node-setup.sh
# lanciato senza privilegi di amministratore
apt-get install -y git

#go cd  /var/lib/go-server
dpkg -i /anarchia/lib/go/go-server-15.3.1-2777.deb
dpkg -i /anarchia/lib/go/go-agent-15.3.1-2777.deb
/etc/init.d/go-agent start
/etc/init.d/go-server start

#add fleetd tunnel
#ssh -i certs/insecure_private_key  core@172.17.8.101
eval `ssh-agent -s`
ssh-add /anarchia/certs/insecure_private_key 
#/anarchia/bin/fleetctl --tunnel 172.17.8.101 list-units

# aggiungo le chiavi per il docker registry
mkdir -p /etc/docker/certs.d/anarchia-registry:5000
cp /anarchia/certs/domain.crt /etc/docker/certs.d/anarchia-registry:5000/ca.crt

#pagina di cortesia
cp /anarchia/vbox/viewpoint/index.html /var/www/html