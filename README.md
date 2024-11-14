# Spring Cloud Config Server for real time configuration Setup Guide

## Build all the docker images

```sh
chmod +x build-docker-images.sh
```

```sh
./build-docker-images.sh v1
```
If no version is provided, it will default to version 1.

## Prerequisites For Hookdeck
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


## Circuit Breaker & Events

A Circuit Breaker helps prevent overloading a failing service by automatically stopping requests when the service is down and resuming requests once it's back online. Spring Boot's Actuator exposes various endpoints to monitor and manage Circuit Breakers, including retrieving Circuit Breaker events like errors, successes, and state changes. <br/>

This project includes two key Actuator endpoints: <br/>

- **/actuator/circuitbreakers**: Lists all available Circuit Breakers. (http://localhost:8072/actuator/circuitbreakers)
- **/actuator/circuitbreakerevents**: Shows the events for a specific Circuit Breaker. (http://localhost:8072/actuator/circuitbreakerevents?name=accountsCircuitBreaker)


## Redis Server for Redis Rate Limiter

To start a Redis server using Docker, run the following command:

```shell
docker run -p 6379:6379 --name bankredis -d redis
```

## Load Testing with Apache Benchmark

To install Apache Benchmark (ab), use the following command:

```shell
sudo apt install apache2-utils
```

```shell
ab -n 10 -c 2 -v 3 http://localhost:8072/bank/card/api/contact-info
```

### Command Breakdown

- `-n 10`: Specifies the total number of requests to perform (10 requests).
- `-c 2`: Sets the number of concurrent requests (2 users).
- `-v 3`: Defines the verbosity level for detailed output (3 for maximum detail).


# Integrating Grafana, Loki, and MinIO for Log Management

## Overview
This section outlines the integration of **Grafana**, **Loki**, and **MinIO** for an efficient log management and visualization system. **Grafana Loki** is a log aggregation tool that integrates seamlessly with **Grafana** to display logs alongside metrics and dashboards. **MinIO** serves as a scalable object storage backend for storing large amounts of unstructured data, such as log files.

## Components
- **Grafana Loki**: A log aggregation system designed for simplicity and high performance, with minimal indexing requirements. It allows you to collect, search, and visualize log data in **Grafana**.
- **MinIO**: A high-performance, distributed object storage service that can be used to store unstructured data such as log files.
- **AlloyDB**: Primarily a relational database from Google. While powerful, it is not typically involved in log collection or management and is mentioned here for context.

## How It Works
1. **Loki** aggregates logs from various sources (e.g., applications, servers) and stores them efficiently.
2. **MinIO** can be configured as the storage backend for **Loki** to handle large-scale log data, providing durability and easy data access.
3. **Grafana** connects to **Loki** as a data source, allowing you to visualize logs in dashboards alongside other metrics from sources like **Prometheus**.

## Benefits
- **Unified Monitoring**: View logs and metrics in a single Grafana dashboard for a comprehensive monitoring solution.
- **Scalable Storage**: Use **MinIO** to handle large volumes of log data securely and reliably.
- **Simple Integration**: **Loki** integrates seamlessly with **Grafana**, making it easy to set up and use for log visualization and analysis.

## Example Use Cases
- **Troubleshooting**: Quickly correlate logs with metric spikes to identify the root cause of issues.
- **Log Management**: Efficiently store and query logs using **MinIO** as the backend for **Loki**.
- **Unified Dashboards**: Combine logs and application metrics in a single Grafana dashboard for better observability.

## Resources
- [Grafana Loki Documentation](https://grafana.com/docs/loki/latest/)
- [MinIO Documentation](https://min.io/docs/minio)
- [Grafana Documentation](https://grafana.com/docs/grafana/latest/)

Integrating **Grafana**, **Loki**, and **MinIO** creates a robust solution for log management and visualization, ensuring that logs are easily accessible and can be viewed alongside performance metrics.



# Monitoring and Visualization with Grafana and Prometheus

## Overview
This project integrates **Grafana** and **Prometheus** to create a powerful monitoring and alerting system. **Grafana** is used as a visualization tool that connects to **Prometheus** as a data source for displaying and analyzing application and infrastructure metrics.

## Features
- **Detailed Dashboards**: Grafana can utilize Prometheus as a data source, allowing the creation of comprehensive dashboards to visualize collected metrics.
- **Custom Metric Visualization**: Metrics monitored by Prometheus, such as CPU usage, memory consumption, and application-specific metrics, can be displayed in Grafana dashboards through charts, graphs, and tables.
- **Alerting System**: Alerts configured in Grafana can leverage Prometheus metrics as triggers, enabling real-time notifications and a robust monitoring system.

## How It Works
1. **Prometheus** collects and stores metrics as time series data from various endpoints and applications.
2. **Grafana** reads these metrics from **Prometheus** using PromQL (Prometheus Query Language) and displays them in user-configured dashboards.
3. **Alerts** can be set up in **Grafana** to trigger based on specific thresholds or conditions, using Prometheus metrics as the underlying data.

## Getting Started
1. **Set up Prometheus**: Configure Prometheus to collect metrics from your application and infrastructure.
2. **Install Grafana**: Connect Grafana to Prometheus as a data source.
3. **Create Dashboards**: Build and customize dashboards in Grafana to visualize metrics.
4. **Configure Alerts**: Set up alerting rules in Grafana based on Prometheus metrics for timely notifications.

## Example Use Cases
- **System Monitoring**: Visualize CPU, memory, and disk usage of servers.
- **Application Metrics**: Track application performance and custom metrics such as API response times.
- **Alerting**: Receive alerts when a metric crosses a defined threshold (e.g., CPU usage exceeds 80%).

## Resources

- [Prometheus Documentation](https://prometheus.io/docs/introduction/overview/)

This setup provides a seamless way to monitor applications and infrastructure, ensuring that issues can be detected and resolved efficiently.

# Integrating Micrometer for Application Metrics

## Overview
**Micrometer** is an application metrics library that acts as a bridge between your application and various monitoring systems such as **Prometheus**, **Graphite**, and others. It provides a unified API to define and collect metrics, enabling seamless integration with monitoring tools for visualization and analysis.

## Features
- **Unified API**: Simplifies collecting and defining metrics within your application.
- **Integration with Monitoring Systems**: Exports collected metrics to popular monitoring systems, such as **Prometheus**.
- **Visualization**: Metrics exported to **Prometheus** can be visualized in **Grafana**, providing comprehensive insights into your application's performance.

## How It Works
1. **Micrometer** is embedded within your application to collect various types of metrics (e.g., counters, timers, gauges).
2. These metrics are exported to **Prometheus**, which scrapes and stores them as time series data.
3. **Grafana** reads the metrics from **Prometheus** to create visual dashboards for monitoring application performance.

## Example Workflow
1. **Add Micrometer to Your Application**: Include Micrometer dependencies in your project and instrument your code with metrics.
2. **Configure Export to Prometheus**: Set up Micrometer to export metrics to a Prometheus endpoint.
3. **Visualize in Grafana**: Connect Grafana to Prometheus as a data source to build dashboards and visualize your application metrics.

## Benefits
- **Simplified Metric Collection**: Easy to collect and manage metrics within your application.
- **Flexible Integration**: Works with multiple monitoring systems, ensuring adaptability.
- **Enhanced Monitoring**: When combined with **Prometheus** and **Grafana**, creates a powerful monitoring stack for both system and application metrics.

## Resources
- [Micrometer Documentation](https://micrometer.io/docs)

By integrating **Micrometer** with **Prometheus** and **Grafana**, you can monitor your application's performance, track key metrics, and quickly identify potential issues.
