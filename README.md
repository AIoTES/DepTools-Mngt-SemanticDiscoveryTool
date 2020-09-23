# Semantic auto-discovery platform 

This application is a web application that allows the access and discovery of devices registered to AIoTES, and specifically into the SIL. It is a portal that was created using [Liferay 6.1.1-ce-ga2](https://www.liferay.com/), Java 7 and Tomcat 7. The back-end of the application is implemented in Java. For the user interface the application uses [JSF](https://en.wikipedia.org/wiki/JavaServer_Faces) + [Primefaces](https://www.primefaces.org/) + JavaScript.

The application communicates through HTTP calls with the SIL. That means that before deploying this application you have to ensure that the SIL is up and running.

## How to deploy it

- Use an operating system with Java 7
- Download and install the following Liferay Portal version: liferay-portal-tomcat-6.1
- Download Liferay SDK and the provided source code. Import the project and create the corresponding WAR file
- Copy the created WAR file in the deploy folder of Liferay Portal and initialise the Tomcat

## Docker deployment

You can use this application by running the corresponding docker image available on the [Activage Docker Registry](https://docker-registry-activage.satrd.es/). You can run it using the following instructions: https://git.activageproject.eu/Deployment/DT-AIOTES_docker/src/master/Semantic_auto_discovery%20platform 

Download ``Semantic_auto_discovery platform`` from [DT-AIOTES_docker](https://git.activageproject.eu/Deployment/DT-AIOTES_docker) repo.

In order to deploy the Semantic auto-discovery platform using Docker, download the `docker-compose.yml` in a local directory. Modify the environment variables and ports to reflect your configuration and then run the following command from the same directory:

```
docker-compose up -d
```

The application communicates with the SIL API through the *AIOTES_API* environmental variable. Please modify this variable accordingly Finally, when the application initializes, a client is registered using as an id the value of the environmental variable *CLIENT_ID*

## Usage

By default, the application will be available from a web browser at the following URL: http://localhost:9082/web/activage/semantic-auto-discovery-platform. In case you use a Windows machine, you may have to replace "localhost" with 192.168.99.100.

For instructions of how to use it, please check the Poliformat platform and the corresponding course

## License

EUPL v1.2