docker run -it --rm --link rabbitmq:ami-02 \
       --add-host ami-01:172.17.8.101  \
       --add-host ami-03:172.17.8.103  \
       -e RABBITMQ_ERLANG_COOKIE='tokensupersegreto' \
       -e RABBITMQ_NODENAME='rabbit@ami-02' rabbitmq:3 bash


#docker run -d --hostname ami-01 --name some-rabbit -p 8080:15672 rabbitmq:3-management
