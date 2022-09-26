This will create an image on Ubuntu and install JAVA8, MAVEN, SBT


```
#Build a docker image: Run the below command where Dockerfile is available
docker build -t buildscalacode .

#Create and run Docker container
docker run buildscalacode

#How to get into a docker container
docker run -it buildscalacode /bin/bash

```

```
# To push images to Docker Hub
docker images
docker tag cd_code_builder:latest rajesh920352/runtime_environment_images:0.0.1-snapshot
docker push rajesh920352/runtime_environment_images:0.0.1-snapshot

```

```
#Use images from personal Docker Hub/Local Image

#Create Docker File with below content

#FROM rajesh920352/runtime_environment_images:0.0.1-snapshot  --> This will from docker hub 
FROM buildscalacode:latest  #--> This will use local image. check docker images

WORKDIR /data
COPY pom.xml ./

RUN mvn clean compile

#----


docker build -t imagefromdockerhub .
docker run imagefromdockerhub

docker run -it imagefromdockerhub /bin/bash

```


```
#Commit Container:
CONTAINER_ID=`docker ps | grep buildscalacode | awk '{print $1}'`
docker commit $CONTAINER_ID  rajesh920352/runtime_environment_images:buildscalacodemvnsnapshot

docker push rajesh920352/runtime_environment_images:buildscalacodemvnsnapshot
```


```
Copy from Local to Docker
CONTAINER_ID=`docker ps | grep buildscalacode | awk '{print $1}'`
 
docker cp <SRC_FILE/SRC_DIR> ${CONTAINER_ID}:/data/

```

```
Copy from Docker to Local

CONTAINER_ID=`docker ps | grep buildscalacode | awk '{print $1}'`
docker cp ${CONTAINER_ID}:/data/Customer-Domain/build/customerDomain.zip .

```
