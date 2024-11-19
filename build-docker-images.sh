#!/bin/bash

# Check if a version argument is provided
if [ -z "$1" ]; then
  echo "Usage: $0 <version>"
  exit 1
fi

# Set the version from the command-line argument
VERSION=$1

# Build the accounts image from the root directory
echo "Building Docker image for accounts from root..."
docker build -t "akash9229/accounts:$VERSION" -f accounts/Dockerfile .

if [ $? -eq 0 ]; then
  echo "Successfully built akash9229/accounts:$VERSION"
else
  echo "Failed to build akash9229/accounts:$VERSION" >&2
  exit 1
fi

# Array of remaining services to build within their directories
services=(
  "cards:akash9229/cards"
  "loan:akash9229/loan"
  "configserver:akash9229/configserver"
  "eurekaserver:akash9229/eurekaserver"
  "gatewayserver:akash9229/gatewayserver"
)

# Loop through each service, change to its directory, and build the image
for service in "${services[@]}"; do
  IFS=":" read -r dir image <<< "$service"
  echo "Building Docker image for $dir..."
  (cd "$dir" && docker build . -t "$image:$VERSION")

  if [ $? -eq 0 ]; then
    echo "Successfully built $image:$VERSION"
  else
    echo "Failed to build $image:$VERSION" >&2
    exit 1
  fi
done

echo "All images built successfully."
