# Exploring the project

It's easiest to get started and play around if you have Docker and docker-compose installed.

The project uses the sakiladb database from https://github.com/sakiladb/postgres .

```sh
    # Build the application first
    ./gradlew clean build

    # Then start docker-compose
    docker-compose up --build -d && docker-logs -f

    # Once you are done stop docker compose and delete the database volumes
    docker-compose down -v
```