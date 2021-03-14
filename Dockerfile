FROM openjdk:11-jre-slim

COPY target/messenger-0.0.1-SNAPSHOT.jar /messenger-0.0.1-SNAPSHOT.jar
CMD java -jar /messenger-0.0.1-SNAPSHOT.jar --origin=$ORIGIN --avatars.path=$AVATARS_PATH \
    --spring.datasource.url=$DB_URL \
    --spring.datasource.username=$DB_USERNAME \
    --spring.datasource.password=$DB_PASSWORD \
    --server.port=$PORT

EXPOSE $PORT