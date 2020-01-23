### Run application

1. Run mongo in docker:
   ```
   docker run --rm -d --name mongodb -p 27017:27017 mongo
   ```
2. Configure `Run/Debug Configurations` Intellij IDEA window:
    * Environment variables:
        * Add `DOCKER_IP` equals your ip address from `$ docker-machine ip`
        * Add `MONGO_CLUSTER=${DOCKER_IP}:27017`
