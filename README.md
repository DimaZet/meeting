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
   docker run  --rm --name psql -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres:alpine  
   ```
2. IDEA: in `Run/Debug Configurations` window
    * Add `MONGO_CLUSTER` into "Environment variables" (the same values as at step 1) 
    * Put `development` into "Active profiles"
