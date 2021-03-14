# Messenger-back
This project is backend for the [Messenger](https://kanutan93-messenger-front.herokuapp.com/)

### Building

Building the Messenger requires the following tools:

- Git (obviously)
- JDK 11 (Oracle's JDK or OpenJDK recommended)
- Apache Maven 3.1.1+ (3.2+ recommended)

To build application, run:
```shell
mvn clean install
```
### Run
```shell
java -jar target/messenger-0.0.1-SNAPSHOT.jar --origin=$ORIGIN --avatars.path=$AVATARS_PATH \
    --spring.datasource.url=$DB_URL \
    --spring.datasource.username=$DB_USERNAME \
    --spring.datasource.password=$DB_PASSWORD \
    --server.port=$PORT
```

### Docker
You can build docker image using Dockerfile in the root of this project:
```shell
docker build -t messenger-back .
```
And run container:
```shell
docker run  -d messenger-back 
```
or use it in k8s, docker-compose and other tools that you prefer.

### About frontend
Angular 11 application is in my private repository.