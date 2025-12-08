# Dash+

## Building from source:

Building from source requires the java version to be >= 25. The java version can be verified by running `java --version`. 

To build the .jar file, run the following:

```
git clone https://github.com/WatForm/dashplus
cd ./dashplus
./gradlew releaseJar
```

The generated .jar file is located at `dashplus/app/build/libs/watform-dashplus.jar`

The build process uses gradle version 9.1.0. However, installing this globally is unnecessary, since it is downloaded as part of the build process.

The build process is the same for Linux, MacOS and Windows powershell. On Windows cmd, `./gradlew releaseJar` is replaced by `gradlew releaseJar`.

Run with the desired entry point: 
- Execute command in Dash/Alloy file: `java -jar app/build/libs/watform-dashplus.jar <args>`
- TLA Translation:                    `java -cp app/build/libs/watform-dashplus.jar ca.uwaterloo.watform.dashtotla.Main <args>`
- Predicate Abstraction:              `java -cp app/build/libs/watform-dashplus.jar ca.uwaterloo.watform.predabstraction.Main <args>`

## Instructions for Contributors:

- If an IDE is used, please ensure that IDE-generated files are not present in any of the commits. This can be done by including such files in the .gitignore

- See [Coding Standards](/CodingStandards.md)

- See [Error Handling Strategy](/app/src/main/java/ca/uwaterloo/watform/utils/ErrorHandling.md)

## Before Committing
- `./gradlew test` for unit testing
- `./gradlew spotlessApply` to format code with google-java-format
- `./gradlew build` will check for formatting issues as a dependent gradle task

## Integration Testing:
- `git submodule update --init --recursive` to download [dash-testing](https://github.com/WatForm/dash-testing#)

- For testing using a JAR, the main entry point is: [`app/src/main/java/ca/uwaterloo/watform/test/Main.java`](app/src/main/java/ca/uwaterloo/watform/test/Main.java)
    > **Note:** If the `Dash.g4` grammar is changed, you must test it with the catalyst-corpus. 
    See the instructions in [`app/src/main/java/ca/uwaterloo/watform/test/README.md`](app/src/main/java/ca/uwaterloo/watform/test/README.md).



