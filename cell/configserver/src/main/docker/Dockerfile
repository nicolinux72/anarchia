# anarchia µ-services nicola santi 2013

FROM java:8
MAINTAINER Nicola Santi "rusco@nicolasanti.it"

EXPOSE 8888
VOLUME /tmp /log
VOLUME /target


ENV TARGET_DIR /opt/app
ENV logging.file /log/microservice.log

COPY  *.jar $TARGET_DIR/app.jar
#COPY  application.properties $TARGET_DIR/application.properties

WORKDIR $TARGET_DIR
RUN touch app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]