### Run application

1. Run mongo in docker:
   ```
   docker run --rm --name mongodb -p 27017:27017 -d mongo
   ```
2. Run postgres in docker from project directory:
   ```
   docker run --rm -v $(PWD)/src/main/resources/postgres/:/docker-entrypoint-initdb.d/ \
        -e POSTGRES_DB=meeting -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
        --name psql -p 5432:5432 -d postgres
   ```
3. Configure `Run/Debug Configurations` Intellij IDEA window:
    * Add `DOCKER_IP` equals your ip address from `$ docker-machine ip`
    * Add `MONGO_CLUSTER=${DOCKER_IP}:27017`
    * Add `POSTGRES_CLUSTER${DOCKER_IP}:5432` 
    * Put `development` into `Active profiles`

### Use application api

1. Register user:
   ```
   POST /api/users/register HTTP/1.1
   Host: localhost:8080
   Content-Type: application/json
   
   {
       "login": "YOUR_LOGIN",
       "password": "YOUR_PASWORD",
       "firstName": "YOUR_FNAME",
       "lastName": "YOUR_SNAME"
   }
   ```
2. Get JWT token:
   ```
   GET /tokens HTTP/1.1
   Host: localhost:8080
   Content-Type: application/json
   
   {
   	"username": "dima",
   	"password": "dima"
   }
   ```
3. Use token on step 2 in headers like example:
   ```
   curl --location --request GET 'http://localhost:8080/api/events' \
   --header 'Authorization: Bearer YOUR_TOKEN_FROM_STEP_2'
   ```
