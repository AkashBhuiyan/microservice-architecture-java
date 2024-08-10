#!/bin/bash

# Get the current directory
current_dir=$(pwd)

# Define the paths to the common and accounts projects
COMMON_PROJECT_PATH="$current_dir/../common"
ACCOUNTS_PROJECT_PATH="$current_dir"

# Create a temporary build directory
mkdir -p docker-build/common docker-build/accounts

# Copy the common project files
cp -r $COMMON_PROJECT_PATH/pom.xml docker-build/common/
cp -r $COMMON_PROJECT_PATH/src docker-build/common/

# Copy the accounts project files
cp -r $ACCOUNTS_PROJECT_PATH/pom.xml docker-build/accounts/
cp -r $ACCOUNTS_PROJECT_PATH/src docker-build/accounts/
cp $ACCOUNTS_PROJECT_PATH/Dockerfile docker-build/

# Build the Docker image
docker build -t accounts-app docker-build

# Clean up the temporary build directory
rm -rf docker-build

