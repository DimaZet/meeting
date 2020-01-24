# Monorepo meeting application

### There are some modules in application

1. Gateway-service:

    Secure and redirect requests to other modules.

2. Meeting-service:

    Management meeting event and publish them to redis for search-service.

3. Search-service

    Full-text searching.

### Running local guide

Each module has README with step-by-step guide how to run it.

All configurations in a nutshell:
    
1. Run databases:
   ```
   docker run --rm -d -v $(PWD)/gateway-service/src/main/resources/postgres/:/docker-entrypoint-initdb.d/ \
           -e POSTGRES_DB=meeting -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
           --name psql -p 5432:5432 postgres
   
   docker run --rm -d --name mongodb -p 27017:27017 mongo
   
   docker run --rm -d --name redis -p 6379:6379 redis
   
   docker run --rm -d --name elastic -p 9200:9200 -p 9300:9300 \
           -e "discovery.type=single-node" elasticsearch:7.5.1
   ```
2. Configure `Run/Debug Configurations` Intellij IDEA window for each `*Application.java`:
   * Environment variables:
       * Add `DOCKER_IP` equals your ip address from `$ docker-machine ip`
