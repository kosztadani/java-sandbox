# Java Sandbox

This is a sandbox repository, mostly for experimentation in Java.

It can also serve as a baseline for other projects.

## Examples, modules

### Hello World application example

To run the "Hello World" application:
```bash
./gradlew :app:run
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
