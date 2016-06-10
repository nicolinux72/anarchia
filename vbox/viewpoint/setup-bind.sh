
sudo apt-get install bind9 bind9utils bind9-doc


sudo cp bind/bind9 /etc/default/bind9
sudo cp bind/named.conf.options /etc/bind/named.conf.options
sudo cp bind/named.conf /etc/bind/named.conf

sudo mkdir /etc/bind/zones

sudo cp bind/db.* /etc/bind/zones/
sudo service bind9 restart

#sudo vi /etc/resolvconf/resolv.conf.d/head
#sudo resolvconf -u

#search anarchia.it  # your private domain
#nameserver 172.17.8.10 # ns1 private IP address
