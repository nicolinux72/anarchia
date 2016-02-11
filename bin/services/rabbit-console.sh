docker run -it --rm --link rabbitmq:ami-01 --add-host ami-01:172.17.8.101  --add-host ami-02:172.17.8.102  -e RABBITMQ_ERLANG_COOKIE='tokensupersegreto' -e RABBITMQ_NODENAME='rabbit@ami-01' rabbitmq:3 bash


#docker run -d --hostname ami-01 --name some-rabbit -p 8080:15672 rabbitmq:3-management
