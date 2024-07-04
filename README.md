#### RabbitMQ for Refresh configurations at runtime using Spring Cloud Bus
sudo docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management

#### Refresh config at runtime using Spring Cloud Bus & Spring Cloud Config monitor
Go to : https://github.com/AkashBhuiyan/microservice-architecture-java-configs/settings/hooks/new and setup the payload URL as the configserver's monitor url. By hookdeck we can connect the hook with our local pc.
the url: https://console.hookdeck.com/ <br/>

> Configure Hookdeck into local pc
>> Download it from here https://github.com/hookdeck/hookdeck-cli/releases/tag/v0.10.1 <br/>
>> And follow the step's of hookdeck console for setup and login <br/>
>> Start the hookdeck by config server LISTEN PORT. Here I have run configserver in 8071 port. <br/>
>> run command: "sudo hookdeck listen 8071 Source" <br/>
>> forward webhook path: "/monitor" <br/>
>> connection label: "localhost" <br/>

It will create an event url, which will perfectly work in the github repo webhooks payload url
