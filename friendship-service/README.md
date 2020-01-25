### Run application

1. Run postgres in docker from project directory:
   ```
   docker run --rm -d -v $(PWD)/src/main/resources/postgres/:/docker-entrypoint-initdb.d/ \
        -e POSTGRES_DB=meeting -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
        --name psql -p 5432:5432 postgres
   ```
2. Run neo4j in docker:
   ```
   docker run --rm -d \
       --publish=7474:7474 --publish=7687:7687 \
       --name neo4j neo4j
   ```
3. Configure `Run/Debug Configurations` Intellij IDEA window:
    * Environment variables:
        * Add `DOCKER_IP` equals your ip address from `$ docker-machine ip`
