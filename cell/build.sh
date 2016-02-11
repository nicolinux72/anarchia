# build docker image for
# permanent services
# and push them on the
# docker registry

echo build configserver 
cd configserver
./gradlew clean build buildImage

echo build eureka 
cd ../eureka
./gradlew clean build buildImage

echo build zuul 
cd ../zuul
./gradlew clean build buildImage

echo build turbine 
cd ../turbine
./gradlew clean build buildImage

echo build hystrix-dashboard 
cd ../hystrix-dashboard
./gradlew clean build buildImage

cd ..