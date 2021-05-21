#Default

ORIGIN=http://localhost:4200
AVATARS_PATH=/home/dmitrii/avatars
DB_URL=jdbc:postgresql://localhost:5432/messenger
DB_USERNAME=postgres
DB_PASSWORD=password
PORT=8087

.PHONY: all build run

all: build

build:
	mvn clean install

run:
	java -jar target/messenger-0.0.1-SNAPSHOT.jar --origin=$(ORIGIN) --avatars.path=$(AVATARS_PATH) \
        --spring.datasource.url=$(DB_URL) \
        --spring.datasource.username=$(DB_USERNAME) \
        --spring.datasource.password=$(DB_PASSWORD) \
        --server.port=$(PORT)