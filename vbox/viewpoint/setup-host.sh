#!/bin/bash

# questo script può essera lanciata
# sulla macchina di sviluppo per aggiungere
# qualche configurazione relativa ad anarchia

# aggiunge mshell al path di sistema

export ANARCHIADIR=`pwd`/../..
if [ -d "/anarchia" ]
then
	ANARCHIADIR=/anarchia
else
	# ssh configs
	vagrant ssh-config --host viewpoint >> ~/.ssh/config

  echo to start local registry type: $ANARCHIADIR/bin/registry.local.sh 
fi

echo Anarchia home found at: $ANARCHIADIR

echo add link to mshell 
sudo ln -s $ANARCHIADIR/bin/mshell /usr/bin/

# aggiunge un alias per micro-cloud
# sudo bash -c "echo 192.168.50.4 anarchia.local >> /etc/hosts"
echo add names to /etc/hosts
sudo bash -c "echo 172.17.8.10 viewpoint >> /etc/hosts"
sudo bash -c "echo 172.17.8.101 ami-01 >> /etc/hosts"
sudo bash -c "echo 172.17.8.102 ami-02 >> /etc/hosts"
sudo bash -c "echo 172.17.8.103 ami-03 >> /etc/hosts"

#sudo bash -c "echo 172.17.8.10 docker-registry.anarchia.loc >> /etc/hosts"
sudo bash -c "echo 10.0.2.2 docker-registry.anarchia.loc >> /etc/hosts"


echo add ssh configs
cat >> ~/.ssh/config << EOM

Host ami-01
  HostName 172.17.8.101
  User core
  IdentityFile $ANARCHIADIR/certs/id_rsa_anarchia
EOM

cat >> ~/.ssh/config << EOM

Host ami-02
  HostName 172.17.8.102
  User core
  IdentityFile $ANARCHIADIR/certs/id_rsa_anarchia
EOM

cat >> ~/.ssh/config << EOM

Host ami-03
  HostName 172.17.8.103
  User core
  IdentityFile $ANARCHIADIR/certs/id_rsa_anarchia
EOM

