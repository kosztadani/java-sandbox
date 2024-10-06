# Java Sandbox

This is a sandbox repository, mostly for experimentation in Java.

It can also serve as a baseline for other projects.

## Examples, modules

### Hello World application example

To run the "Hello World" application:
```bash
./gradlew :app:run
```

### Counter example

This is an example library containing thread-safe counters.

#### Stress test

To run the stress test:

```bash
./gradlew :counter-example:installJcstressDist
java -jar \
  counter-example/build/install/counter-example-jcstress/counter-example-jcstress.jar \
 -m sanity
```

Note the `-m sanity` option, which decreases the run time.

#### Benchmarking

To run the benchmarks:

```bash
./gradlew :counter-example:installJmhDist
java -jar \
  counter-example/build/install/counter-example-jmh/counter-example-jmh.jar
```

### RMI example

To run the server application:
```bash
./gradlew :rmi-example:server:run
```

The server application must be running for the client to be able to connect.

To run the client application:
```bash
./gradlew :rmi-example:client:run
```

### TCP example

To run the server application:
```bash
./gradlew :tcp-example:server:installDist
tcp-example/server/build/install/server/bin/server 9999
```

The server application must be running for the client to be able to connect.

To run the client application:
```bash
./gradlew :tcp-example:client:installDist
tcp-example/client/build/install/client/bin/client localhost 9999
```

Note that gracious error handling is not implemented; the user is confronted with stack traces instead.
You can crash the server by dividing by zero.

#### Benchmarking

To run the benchmarks:

```bash
./gradlew :tcp-example:integration:installJmhDist
java -jar \
  tcp-example/integration/build/install/integration-jmh/integration-jmh.jar
```

## Common maintenance operations, checklists

I try to list some common development steps here, for reference.

### Check repository

To build modules and run unit tests:
```bash
./gradlew test
```

To verify that there are no Checkstyle warnings:
```bash
./gradlew check
```

To build Javadoc and verify that there are no warnings and errors:
```bash
./gradlew javadoc
```

### Adding a new Gradle module

- Initialize directory structure of the new module.
- Create a `build.gradle.kts` in the module.
- List the module in `settings.gradle.kts`.
- Add entry to `.gitignore` to ignore build files.
- Document the module in this README.

### Upgrade dependency locks

To upgrade locked dependencies:

```bash
./gradlew resolveAndLockAll --write-locks
```
