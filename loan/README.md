# Buildpacks approach
> configure it on pom.xml first
> >link: https://buildpacks.io/

mvn spring-boot:build-image

#### Pull image
docker pull akash9229/loan:v1

docker build --build-arg PROFILE=dev -t akash9229/accounts:v1 .