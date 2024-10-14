# Camel Spring Boot Demo Routes

This is a collection of demonstrational routes to accompany Apache Camel training.

In order to choose which problem you want to explore and test, start the Spring Boot application with the system property `camel.problem` set to one of the available values:

* `intro`
* `database`
* `jms`
* `kafka`
* `rest-service`
* `rest-client`
* `http-client`
* `health`
* `bookstore`

See more on individual problems (and the requirements to run them) below.

The main class, `Application`, is your classic Spring Boot application starter.

The class that performs the problem configuration by loading specific routes is `CamelContextConfig`.

For a Maven plug-in-assisted start-up, you can select the problem as follows:

    $ mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dcamel.problem=FOO"

After packaging the application, you can run a problem as follows:

    $ mvn spring-boot:repackage
    ...
    $ java -Dcamel.problem=FOO -jar ./target/demo-1.0-SNAPSHOT.jar

## Bookstore

Bookstore has three routes loading data from `data/bookstore/` directory:

* `books.xml` (overridable by `books.file` property)
* `users.csv` (can be overriden by app property `users.file`)
* `orders.json` (property `orders.file` can override this)

The three file consumer routes are configured with `noop` option, so the files are never removed.

### Book Catalog (`bookLoader`)

The `books.xml` file represents a bookstore catalog, which is loaded, split into individual books, then unmarshaled from XML into instances of `Book` object.
Two filters then remove any books older than 2004 and any books written by "George R. R. Martin" (supposing we can not sell those due to contractual obligations).
The last processor in the route adds each book to an internal singleton bean where it can be further accessed by other routes in need of enrichment.

### User Catalog (`userLoader`)

The `users.csv` file is a user catalog, loaded, split into individual records by newline, and the first row containing column headers is ignored.
The remaining records are unmarshaled from CSV into instances of `User` object.
There is a spin-off step where we invoke the `userAggregator` route, which is discussed below.

Lastly, we try to add the user to the singleton bean which, like with books, represents the user catalog and is available to other routes for enrichment.
Note that the bean invocation is not specifying the exact method, unlike with the book catalog - this is just to demonstrate Camel's automatic method detection.

The seeming complication in unmarshaling is there because one of the rows in the CSV file is deliberately corrupt and will not parse.
Without splitting first and then unmarshaling, the entire file would be read multiple times and valid records would cause duplicates in the user catalog, not to mention needless overhead.

### Team Aggregation (`userAggregator`)

The route consuming from `direct:aggregateByTeam` is showcasing aggregation on a field of the body object, and a simple aggregation strategy which has a limit of 10 users per team and will complete if no new users have been seen for over 1.5s in each aggregation group.

After finishing the aggregation it marshals the team into JSON and writes it into `data/out/teams.json` file for later inspection.

### Order Processor (`orderLoader`)

The order processor loads from `orders.json` file, unmarshals the contents into a `List` of `Order` objects, and then converts each order into an instance of a fully-blown `FulfillmentOrder` class which is suitable for fulfillment.

Because the object is initially empty (with the exception of user ID and book title), we need to enrich the data and this is performed using the `enrich()` EIP. The `orderEnrichment` route is discussed below.

After `FulfillmentOrder` objects are enriched, they are marshaled back into JSON and written to `data/out/orders.json`.

### Order Enrichment (`orderEnrichment`)

The route invokes two different methods in `FulfillmentEnrichment` class - `enrichUser` and `enrichBook`, each of which contacts its catalog in order to obtain data about the user (identified by the user ID number) or the book (identified by its title), respectively.

Because order enrichment may occur before data is available, the route is configured with an exception handler for the two `NotFoundException` exceptions that may occur, which logs the error, sets redelivery delay to 1.5s, and configures up to 10 redelivery attempts.
