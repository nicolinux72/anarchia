
sudo apt-get install bind9 bind9utils bind9-doc

sudo cp /vagrant/bind/bind9 /etc/default/bind9
sudo cp /vagrant/bind/named.conf.options /etc/bind/named.conf.options
sudo cp /vagrant/bind/named.conf.local /etc/bind/named.conf.local
sudo cp /vagrant/bind/named.conf /etc/bind/named.conf

sudo mkdir /etc/bind/zones

sudo cp /vagrant/bind/db.* /etc/bind/zones/
sudo service bind9 restart

#sudo vi /etc/resolvconf/resolv.conf.d/head
#sudo resolvconf -u

#search anarchia.loc  # your private domain
#nameserver 172.17.8.10 # ns1 private IP address
