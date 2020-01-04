1. Run mongo in docker:
   ```
   docker run --rm --name mongodb -p 27017:27017 -d mongo
   ```
2. Run postgres in docker:
   ```
   docker run --rm -v $(PWD)/src/main/resources/postgres/:/docker-entrypoint-initdb.d/ \
        -e POSTGRES_DB=meeting -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
        --name psql -p 5432:5432 -d postgres:alpine
   ```
3. Configure `Run/Debug Configurations` Intellij IDEA window:
    * Add `DOCKER_IP` equals your ip address from `$ docker-machine ip`
    * Add `MONGO_CLUSTER=${DOCKER_IP}:27017`
    * Add `POSTGRES_CLUSTER${DOCKER_IP}:5432` 
    * Put `development` into `Active profiles`
