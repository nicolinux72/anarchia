cd /home/vagrant
curl -L -O https://download.elastic.co/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/2.2.0/elasticsearch-2.2.0.tar.gz
tar -xvf elasticsearch-2.2.0.tar.gz

cp  /anarchia/vbox/viewpoint/elasticsearch.yml /home/vagrant/elasticsearch-2.2.0/config/
/home/vagrant/elasticsearch-2.2.0/bin/elasticsearch &

wget -qO - https://packages.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -
echo "deb http://packages.elastic.co/logstash/2.2/debian stable main" | sudo tee -a /etc/apt/sources.list
echo "deb http://packages.elastic.co/kibana/4.4/debian stable main" | sudo tee -a /etc/apt/sources.list
sudo apt-get update
#sudo apt-get install -y logstash
sudo apt-get install -y kibana

sudo cp /anarchia/vbox/viewpoint/kibana.yml /opt/kibana/config/
sudo update-rc.d kibana defaults 95 10
sudo /etc/init.d/kibana start
