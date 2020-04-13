# Exploring the project

It's easiest to get started and play around if you have Docker and docker-compose installed.

The project uses the sakiladb database from https://github.com/sakiladb/postgres .

Important: Since the database takes some time to initialize the first time, you will need to start the application a second time.

```sh
    # This is a guide on how to start graphqlize demo with docker
    # Build the application first
    ./gradlew clean build

    # Then start docker-compose that builds the docker image
    docker-compose up --build -d

    # View the logs and wait for the system to initialize
    docker-compose logs -f

    # Once you are done stop docker compose and delete the database volumes
    docker-compose down
```