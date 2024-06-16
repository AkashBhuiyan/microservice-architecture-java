# Docker Commands

docker build . -t akash9229/accounts:v1 <br/>
docker images <br/>
docker inspect image <IMAGE ID> <br/>
docker run -p 8080:8080 akash9229/accounts:v1 <br/>

### Detach Mode
docker run -d -p 8080:8080 akash9229/accounts:v1 <br/>

#### Existing docker container right now in my system
docker ps <br/>
> stop container status
>> docker ps -a <br/>

>Start by container id
>> docker start "CONTAINER ID"