### Architecture
A Domain-Driven architecture—where packages and files are organized by business functionality—is often the best choice for large-scale applications due to its clarity and maintainability. However, given the relatively small scope of this application, a technical-driven project structure was chosen instead.

### Authentication and Authorization
This project uses Spring Security's OAuth2 Resource Server with JWT-based API authentication and authorization, integrated with a custom user database for identity management and access control.

### Database
I chose PostgreSQL database over working with plain CSV files in  order to use Java ORM tools like JPA/Hibernate, allowing for more robust data modeling, relationships, and integration with Spring’s data access layer

### Database Initialization
For database initialization, Java Hibernate ORM was chosen over raw SQL scripts due to the small scale of the application and the availability of data in CSV format. Populating the database using SQL would have been more cumbersome in this context. While SQL scripts are typically favored in large-scale projects for their greater control over constraints and indexing, ORM provides a faster and more convenient approach during early development.