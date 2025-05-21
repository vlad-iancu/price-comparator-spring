### Architecture
A Domain-Driven architecture—where packages and files are organized by business functionality—is often the best choice for large-scale applications due to its clarity and maintainability. However, given the relatively small scope of this application, a technical-driven project structure was chosen instead.

### Database
For database initialization, Java Hibernate ORM was chosen over raw SQL scripts due to the small scale of the application and the availability of data in CSV format. Populating the database using SQL would have been more cumbersome in this context. While SQL scripts are typically favored in large-scale projects for their greater control over constraints and indexing, ORM provides a faster and more convenient approach during early development.