# PasswdService
The idea of this challenge is to create a minimal HTTP service that exposes the user and group information on a UNIX-like
system that is usually locked away in the UNIX /etc/passwd and /etc/groups files.

## Design
This application is built using Java 11 and the Spring Boot web framework.  It follows the Spring convention by implementing 
`controller`, `service`, and `repository` layers.  The user and group data are read from a file, and stored in an in-memory
database and the data is re-loaded on each REST request.  It also follows spring conventions for handling error cases, for
example when an entity is not found in the database, or the `passwd` or `group` files cannot be read.  A docker image can
be built for the service using the `Dockerfile`.

## Testing
Unit tests for the controller layer are found in `com.brain.passwd.controller`, and do not require a running server instance.
Additional tests for the `service` and `repository` layers have not yet been implemented.  End-to-end tests are located in
the `e2e-tests` sub-module, and require a running server instance.  The end-to-end tests utilize a swagger-generated SDK
to perform the tests.  Information on how to use the SDK can be found in `e2e-tests/build/generated/docs`.

## TODOs
- Include a CI/CD pipeline definition (i.e. Jenkinsfile), to build the project, run tests, create/publish a docker image
- Generate a code-coverage report
- Add JMeter load testing scripts

### Building the project
```bash
./gradlew classes
```

### Running tests
```bash
./gradlew test -x :e2e-tests:test
```
Test results are written to `build/test-results` and a report is generated at `build/reports/tests/test/index.html'

### Building the JAR file
```bash
./gradlew bootJar
```
The JAR file is written to `build/libs`

### Starting the service

To start the service with default values:
```bash
./gradlew bootRun
```
A swagger page can be found at http://localhost:8080/swagger-ui.html

To start the service and modify any property:
```bash
./gradlew bootRun -PjvmArgs="-Dpasswd.file.path=/etc/passwd -Dgroup.file.path=/etc/group"
```

### Building a docker image
```bash
docker build -t passwd-service .
```

### Starting the service as a docker container
```bash
docker run -it -p 8080:8080 passwd-service
```

### Building the end-to-end tests
The end-to-end tests use a swagger-generated SDK, to build the SDK and tests:
```bash
./gradlew :e2e-tests:openApiGenerate
./gradlew :e2e-tests:testClasses
```

### Running end-to-end tests against a locally-deployed service
The end-to-end tests rely on pre-defined user/group data.  Volume mount the test data to a container running the service:
```bash
docker run -p 8080:8080 -e passwd.file.path=/var/test-data/passwd -e group.file.path=/var/test-data/group -v $(pwd)/e2e-tests/src/test/resources/test-data/:/var/test-data passwd-service
```
And in a separate terminal, run the end-to-end tests
```bash
./gradlew :e2e-tests:test
```
Test results are written to `e2e-tests/build/test-results` and a report is generated at `e2e-tests/build/reports/tests/test/index.html`

### Running end-to-end tests against a remotely-deployed service
```bash
./gradlew :e2e-tests:test -PjvmArgs="-Dpasswd.service.url=http://remote-host:port"
```