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

#### Docker image push
docker image push docker.io/akash9229/accounts:v1

##### Docker image Pull
docker pull akash9229/accounts:v1

#### Docker account login
> docker login -u "akash9229" docker.io

#### Docker compose commands
> docker compose up -d <br/>
> docker compose down <br/>
> docker compose stop <br/>
> docker compose start