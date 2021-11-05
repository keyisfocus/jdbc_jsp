fish shell commands to run db container with schema and no data
1. `cd db` from project root
2. `docker run --name postgres_jdbc_jsp -p 5432:5432 -e POSTGRES_USER=root -e POSTGRES_PASSWORD=5131 -e POSTGRES_DB=jdbc_jsp -d -v (pwd)"/postgres_init/":/docker-entrypoint-initdb.d postgres:13`

Run tomcat-maven plugin locally

`mvn tomcat7:run`