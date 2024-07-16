# transfer-service
RESTful API that performs funds transfers between accounts with currency exchange.

## Service structure
The application was designed with some key driver requirements in mind:
- **Maintainability**: the ability to extend the functionality, refactor the existing one and fix possible issues that may arise.
- **Testability**: possibility of easily creating unit tests for service and controller layer mostly.
- **Concern separation**: separation of responsibilities between business logic and underlying implementations.

<div style="text-align: justify"> 

The architectural design chosen is a very popular one, known as clean architecture or layered architecture, very similar to an MVC pattern.
The idea is to have an internal layer of _entities_ representing business data, in this case:
[Accounts](src/main/java/com/somecompany/transferservice/model/Account.java),
[Owners](src/main/java/com/somecompany/transferservice/model/Owner.java) and
[Transfers](src/main/java/com/somecompany/transferservice/model/Transfer.java).
These entities are persisted in a postgres db, using profile [dev](src/main/resources/application-dev.properties), or if setting active profile [_local_](src/main/resources/application-local.properties), we can test the functionalities of the app using an H2 auto-initializing in-mem db.
When using postgres, tables have to be initialized using the [script](src/main/resources/scripts/db-table-creation-script.sql) provided in resources folder.
Entities are inserted, updated and queried using [repository layer](src/main/java/com/somecompany/transferservice/repository)
These interfaces are injected in the necessary classes from the [service layer](src/main/java/com/somecompany/transferservice/service), that follows a strategy pattern by implementing the [UseCase](src/main/java/com/somecompany/transferservice/service/UseCase.java) interface.
In a similar way, the services are injected in the [controllers](src/main/java/com/somecompany/transferservice/controller) and these use [mappers](src/main/java/com/somecompany/transferservice/mapper) to get the final [dtos](src/main/java/com/somecompany/transferservice/dto).

</div>

<figure style="display: block; margin: auto;">
    <img alt="Clean architecture diagram" src="src/main/resources/static/clean-arch.png" width="400"/>
    <figcaption>Clean architecture diagram. Retrieved from <a href="https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html">R. C. Martin’s Clean Architecture blog</a></figcaption>
</figure>

<br>

<div style="text-align: justify"> 
<h3>Some additional remarks:</h3>
<ul>
    <li>Implemented an ExceptionHandler to standardize the returned dtos and http status.</li>
    <li>Getting latest exchange rates from Open Exchange Rates latest <a href="https://docs.openexchangerates.org/reference/latest-json">endpoint</a>.
        In order to use this API, an api key is needed, that can be easily obtained just by creating a free account.</li>
    <li>Application functionality is analog when using dev, or local profiles.</li>
    <li>If necessary, environment <a href="file://environment-props.env">file</a> can be used to set up props.</li>
</ul>
</div>