docker run -it --rm --link rabbitmq:ami-01.anarchia.loc    -e RABBITMQ_ERLANG_COOKIE='tokensupersegreto' -e RABBITMQ_NODENAME='rabbit@ami-01.anarchia.loc' rabbitmq:3 bash


#docker run -d --hostname ami-01 --name some-rabbit -p 8080:15672 rabbitmq:3-management
