# EnduranceTrio Tracker database

This file documents the process to manage the database used with **EnduranceTrio Tracker** REST API.

## Database Naming Conventions

This project follows consistent naming conventions for database objects to ensure clarity,
maintainability, and cross-database compatibility.

### Table and Column Names

| Element                 | Naming Convention                                               | Example                                                    |
|-------------------------|-----------------------------------------------------------------|------------------------------------------------------------|
| Table                   | Use snake_case and plural nouns                                 | tracking_data, owners                                      |
| Column                  | Use snake_case with descriptive names                           | device, created_at                                         |

### Constraints

| Element                 | Naming Convention                                               | Example                                                    |
|-------------------------|-----------------------------------------------------------------|------------------------------------------------------------|
| Primary Key             | pk_{table}                                                      | pk\_tracking\_data                                         |
| Foreign Key             | fk\_{table}\_{referenced\_table}\_{referenced_column}           | fk_tracking_data_owners_id                                 |
| Unique Key              | uk\_{table}\_{column}                                           | uk\_owners\_alias                                          |
| Check Constraint        | chk\_{table}\_{description}                                     | chk\_tracking\_data\_valid\_latitude                       |

### Indexes

| Element                 | Naming Convention                                                | Example                                                   |
|-------------------------|------------------------------------------------------------------|-----------------------------------------------------------|
| Standard Index          | idx\_{table}\_{column}                                           | idx\_tracking\_data\_device                               |
| Composite Index         | idx\_{table}\_{column}[_{column}...] with columns in query order | idx\_tracking\_data\_device\_owner                        |
| Unique Index            | uk\_{table}\_{column}[_{column}...]                              | uk\_tracking\_data\_device\_record\_time                  |

### Sequences

| Element                 | Naming Convention                                                | Example                                                   |
|-------------------------|------------------------------------------------------------------|-----------------------------------------------------------|
| Sequence                | seq\_{table}\_{column}                                           | seq\_tracking\_data_id                                    |

### Naming Limitations

- **Length Limit** - Keep names under 50 characters to ensure compatibility across H2 and PostgreSQL
- **Readability** - Use clear and descriptive names and prioritize clarity over brevity
- **Ordering** - For composite indexes, list columns in the order they are most frequently queried
- **Case** - Use lowercase letters to avoid case-sensitivity issues
- **Character Set** - Use only alphanumeric characters and underscores
- **Avoid** - Database-specific keywords and special characters

## Database Migration

The [database migrations](https://en.wikipedia.org/wiki/Schema_migration) are made using
[Flyway](https://www.red-gate.com/products/flyway/).

The migration scripts are separated by type and database:

* **[DDL](https://en.wikipedia.org/wiki/Data_definition_language)** - These scripts are
  database-specific and contain schema definition (e.g., `CREATE TABLE`, `ALTER TABLE`)
  and are stored in:
    * [`migration/ddl/h2`](migration/ddl/h2): DDL scripts for the **H2** database
      (local/dev environments).
    * [`migration/ddl/postgres`](migration/ddl/postgres): DDL scripts for the **PostgreSQL**
      database (production environment).
* **[DML](https://en.wikipedia.org/wiki/Data_manipulation_language)** - These scripts contain
  data manipulation (e.g., `INSERT`, `UPDATE`) and are stored in:
    * [`migration/dml`](migration/dml)

The [migration scripts](https://documentation.red-gate.com/flyway/quickstart-how-flyway-works/why-database-migrations)
must respect the following naming convention (which is compatible with the
[Flyway naming patterns](https://www.red-gate.com/blog/database-devops/flyway-naming-patterns-matter)):

    Vxxx.yyy.zzz.nnn__free-description.sql

> ***xxx*** : Major version number at the time of the script creation
>
> ***yyy*** : Minor version number at the time of the script creation
>
> ***zzz*** : Patch version number at the time of the script creation
>
> ***nnn*** : Order number for version number at the time of the script creation
>
> ***free-description*** : A short free description of the scripts actions with words separated
> with dashes

### Spring Boot Configuration

To make this structure work, the `spring.flyway.locations` property is set with the adequate values
in the `application-{profile}.yaml` files, enabling Flyway to use the correct folders
for each database.

#### H2 Database (local and dev profiles)

```yaml
spring:
  flyway:
    locations:
      - classpath:db/migration/ddl/h2
      - classpath:db/migration/dml
```

#### PostgreSQL Database (prod profile)

```yaml
spring:
  flyway:
    locations:
      - classpath:db/migration/ddl/postgres
      - classpath:db/migration/dml
```

## Migration Scripts

The DDL scripts are duplicated for each supported database (H2 and PostgreSQL) to ensure full
compatibility with the targeted databases. That is not necessary for the DML scripts, which are
database-agnostic.

1. Creates the **EnduranceTrio Tracker** REST API database tables:
   - [V000.000.001.001__create-tables-h2.sql](migration/ddl/h2/V000.000.001.001__create-tables-h2.sql)
   - [V000.000.001.001__create-tables-postgres.sql](migration/ddl/postgres/V000.000.001.001__create-tables-postgres.sql)
2. Inserts test data into **EnduranceTrio Tracker** REST API database tables:
    - [V000.000.001.002__insert-test-data.sql](migration/dml/V000.000.001.002__insert-test-data.sql)
