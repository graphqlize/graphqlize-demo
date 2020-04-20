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

## Build an API on top of your own data

The Docker setup uses the sakila sample databse, however you can use your own very easily. This way you can explore very fast how grapqhlize will look on top of your existing database structure.

Please use this feature and contribute with feedback. If you are a developer and you find the project useful but in need of improvement approach the community for code contributions.

The steps to use your date are as follows:

* Export your data from your existing databse or take a copy from backup.
* Copy your data to `docker-entrypoint-initdb.d`. We are using the import features provided by the docker image. See image docs for help.
* Update the databse configureation in `config/docker-application.properties` to match your database name, etc
* Start your containers `docker-compose up -d && docker-compose logs -f`
* Explore your Graphqlize API

The docker images for both PostgreSQL and MySQL/MariaDB offer ways to load data before the container is started.
We are going to use that feature to load another database instead of sakiladb.

**Note**: Database initialization happens only once during the initial docker start. You may want to remove your volumes with `docker-compose down -v` to force initialization.

### Using your own data with PostgreSQL

For PostgreSQL we are going to use the functionality described in the [Initialization scripts](https://hub.docker.com/_/postgres) section of the image documentation.

> If you would like to do additional initialization in an image derived from this one, add one or more *.sql, *.sql.gz, or *.sh scripts under /docker-entrypoint-initdb.d (creating the directory if necessary).
> After the entrypoint calls initdb to create the default postgres user and database, it will run any *.sql files, run any executable *.sh scripts, and source any non-executable *.sh scripts found in that directory to do further initialization before starting the service.

If you are on a Linux system you can run the following commands:

```sh
    # Export your data with pg_dump or just take it from your backups.
    sudo -u postgres pg_dump my_db | gzip > my_db.sql.gz
    # Copy your data to docker-entrypoint-initdb.d
    # Update config/docker-application.properties with your database name and other credentials
    # Start the container
    docker-compose up -d && docker-compose logs -f
```

In my personal setup I took a database dump generated by `autopostgresqlbackup`. I had to customize postgresql image locale and also add an initialization script to create the roles that my database dump was expecting.