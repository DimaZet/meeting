1. Run mongo in docker:
    ```
    # To configure your shell with docker:
    eval $(docker-machine env)

    export DOCKER_IP=*** # your ip address from `docker-machine ip`
    export MONGO_CLUSTER=$(DOCKER_IP):27017
    
    docker run --rm --name mongodb -p 27017:27017 mongo
    ```
2. Run postgres in docker:
   ```
   docker run --rm -v $(PWD)/src/main/resources/postgres/:/docker-entrypoint-initdb.d/ \
        -e POSTGRES_DB=meeting -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
        --name psql -p 5432:5432 -d postgres:alpine
   ```
2. IDEA: in `Run/Debug Configurations` window
    * Add `MONGO_CLUSTER` into "Environment variables" (the same values as at step 1) 
    * Put `development` into "Active profiles"
