# Price comparator
This is an application that compares grocery prices from different providers
### Configuration
This application was build using [OpenJDK 17](https://openjdk.org/projects/jdk/17/)
```env
#.env

PGADMIN_DEFAULT_EMAIL=admin@email.com
PGADMIN_DEFAULT_PASSWORD=adminpassword
POSTGRES_USER=database_user
POSTGRES_PASSWORD=database_password
POSTGRES_DB=db_name
# Generated using "openssl rand -base64 32"
JWT_SECRET_KEY=<your_generated_jwt_secret_key>
```

### Running inside Docker

To build and run the application, use the following command:

```bash
docker compose up
```
### Running the application in development profile
Create the file `application-dev.properties` in `src/main/resources` with the
same properties as `src/main/resources/application-docker.properties`

```bash
./mvnw compile package -DskipTests

JWT_SECRET_KEY=<your_generated_jwt_secret_key> java -jar target/price_comparator-X.X.X-SNAPSHOT.jar --spring.profiles.active=dev
```
