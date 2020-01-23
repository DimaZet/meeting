### Run application

1. Run postgres in docker from project directory:
   ```
   docker run --rm -d -v $(PWD)/src/main/resources/postgres/:/docker-entrypoint-initdb.d/ \
        -e POSTGRES_DB=meeting -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
        --name psql -p 5432:5432 postgres
   ```
2. Configure `Run/Debug Configurations` Intellij IDEA window:
    * Environment variables:
        * Add `DOCKER_IP` equals your ip address from `$ docker-machine ip`
        * Add `POSTGRES_CLUSTER=${DOCKER_IP}:5432`

### Use application api

1. Register user:
   ```
   POST /api/users/register HTTP/1.1
   Host: localhost:8080
   Content-Type: application/json
   
   {
       "username": "YOUR_LOGIN",
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
   	"username": "YOUR_LOGIN",
   	"password": "YOUR_PASWORD"
   }
   ```
