## Building the Docker Image with a Specific Profile


# For dev profile
sudo docker build --build-arg PROFILE=dev -t akash9229/configserver:dev .

# For prod profile
sudo docker build --build-arg PROFILE=prod -t akash9229/configserver:prod .

# For qa profile
sudo docker build --build-arg PROFILE=qa -t akash9229/configserver:qa .


# Run containers
sudo docker run -d -p 8071:8071 akash9229/configserver:dev
sudo docker run -d -p 8071:8071 akash9229/configserver:prod
sudo docker run -d -p 8071:8071 akash9229/configserver:qa