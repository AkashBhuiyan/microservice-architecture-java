# for build
docker build -t akash9229/eurekaserver:v1 .


# Run containers
docker run -d -p 8072:8072 akash9229/eurekaserver:v1