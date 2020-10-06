##Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

###Explicit requirements:
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

###Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

---
##Implementation considerations driven by requirements

###Explicit requirements decisions:
1. Java implementation has been chosen (OpenJDK 11)
2.4. Group of requirements regarding the tools/frameworks to be used and not used (spring) give a wide variation 
of possible items for usage: Play, Quarkus, Micronaut, Ktor (if Kotlin), Vert.x etc. All of the
variants listed have smaller footprint than classical Spring, have usually quicker startup time and simplicity
targeted to microservice environment. The implementation in 'Pure java' without any tools used was abandoned 
as 'reinventing the wheel'. So the only 'strict constraint' for the choice was avoiding Spring and avoiding
Hibernate (due to heavy final JAR and long startup time). As the result Micronaut framework was chosen for 
the implementation.
3. Assumption regarding API for multi system invocation was taken into account as NFR to be able to work in parallel
without any of known issues with data integrity. All considerations about AAA (authentication, authorization, accounting)
was abandoned according to point 2.
5. Default in-memory database solution is chosen - H2 database.
6. Application is packed as standalone 'single JAR' application (prerequisites - only java 11 installed).
7. Integration tests against REST API are written.

###General implementation points:
1. Code was structured according to usual practices with package separation by application 'layer/domain'.
2. Simplicity principle was used as general rule: simple namings (commonly adopted), classes separation, 
no extra interfaces (if not specially needed), etc.
3. Maven 3.x was taken as default build tool.
4. Classical synchronous implementation was done with a simplicity principle. However all of the frameworks 
(listed in 2.4.) support async implementations (RX Java, etc.) of database operations, service and REST layers. 
The async option will be more efficient and modern but less readable and obvious.
5. Integration tests were implemented as the most important aspect of system sustainable development. 
HttpClient was used for implementation against the application RESTs to cover obvious corner and usual scenarios.
Low level unit tests was abandoned as 'code waste' thing for the current system complexity.

#Application build and run
###Build
Install latest available maven 3 on the system path.
Install latest available open jdk 11 on the system path.

(project root dir)

mvn clean install

###Run
cd [project root dir]/target

java -jar transfers-0.1.jar

###Usage
Utility REST endpoints:

POST /account {} - open new account with a zero balance

GET  /account/<id> - get account state by id (state represented by balance)

PUT  /account/<id> { "balance": 123 } - update account with a new balance value

Primary REST endpoint:

POST /account/operation/transfer { "sourceAccountId": 123, "targetAccountId": 321, "amount": 1000 } - transfer the
specified amount from the source account to the target account.


