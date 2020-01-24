### Run application

1. Run elasticsearch in docker:
   ```
   docker run --rm -d --name elastic -p 9200:9200 -p 9300:9300 \
        -e "discovery.type=single-node" elasticsearch:7.5.1
   ```
2. Run redis in docker:
   ```
   docker run --rm -d --name redis -p 6379:6379 redis
   ```
3. Configure `Run/Debug Configurations` Intellij IDEA window:
    * Environment variables:
        * Add `DOCKER_IP` equals your ip address from `$ docker-machine ip`
