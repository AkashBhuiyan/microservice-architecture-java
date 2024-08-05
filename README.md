# Spring Cloud Config Server for real time configuration Setup Guide

## Prerequisites
- Ensure you have Docker installed.
- Install Hookdeck CLI for managing webhooks.

## RabbitMQ for Refresh configurations at runtime using Spring Cloud Bus
To refresh configurations at runtime using Spring Cloud Bus, you'll need RabbitMQ running. You can set up RabbitMQ using Docker:

```sh
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
```

## Refresh Configurations at Runtime Using Spring Cloud Bus & Spring Cloud Config Monitor

### Step 1: Set Up GitHub Webhook
1. Navigate to your GitHub repository settings: [GitHub Webhooks](https://github.com/AkashBhuiyan/microservice-architecture-java-configs/settings/hooks/new).
2. Set the payload URL as your config server's monitor URL. Use Hookdeck to connect the webhook with your local PC. More information can be found at [Hookdeck Console](https://console.hookdeck.com/).

### Step 2: Install and Configure Hookdeck
1. **Download Hookdeck CLI**: Download the Hookdeck CLI from [here](https://github.com/hookdeck/hookdeck-cli/releases/tag/v0.10.1).
2. **Setup and Login**: Follow the steps provided in the Hookdeck console for setup and login.
3. **Start Hookdeck**: Start Hookdeck by listening to the config server's port (in this example, port 8071):

    ```sh
    sudo hookdeck listen 8071 Source
    ```

4. **Forward Webhook Path**: Forward the webhook path to `/monitor`.
5. **Set Connection Label**: Set the connection label to `localhost`.

This setup will create an event URL, which can be used as the payload URL in the GitHub repository webhooks settings.


## Viewing Configuration

You can view the configurations for different services at the following URLs:

- **Accounts Service**: [localhost:8071/accounts/dev](http://localhost:8071/accounts/dev)
- **Loan Service**: [localhost:8071/loan/dev](http://localhost:8071/loan/dev)
- **Cards Service**: [localhost:8071/cards/dev](http://localhost:8071/cards/dev)

## Viewing Health Status (Actuator)

To check the health status of your application, use the following URL:

- **Health Check**: [http://localhost:8071/actuator/health](http://localhost:8071/actuator/health)

## Viewing Health Status Liveness and Readiness (Actuator)

To check the Liveness and Readiness status of your application, use the following URL:

- **Liveness Check**: [http://localhost:8071/actuator/health/liveness](http://localhost:8071/actuator/health/liveness)
- **Readiness Check**: [http://localhost:8071/actuator/health/readiness](http://localhost:8071/actuator/health/readiness)

> **Note**: If the status is down, it indicates that the RabbitMQ server is not running. Ensure RabbitMQ is started using the Docker run command mentioned above.


## Mysql DB for microservices

```sh
docker run -p 3306:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accountsdb -d mysql
```
```sh
docker run -p 3307:3306 --name loandb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=loandb -d mysql
```
```sh
docker run -p 3308:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb -d mysql
```