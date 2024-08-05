## Building the Docker Image with a Specific Profile


# For dev profile if ARG use
docker build --build-arg PROFILE=dev -t akash9229/configserver:v1 .

# for build
docker build -t akash9229/configserver:v1 .


# Run containers
docker run -d -p 8071:8071 akash9229/configserver:v1