# Dash+

## Building from source:

Building from source requires the java version to be >= 17. The java version can be verified by running `java --version`. 

To build the .jar file, run the following:

```
git clone https://github.com/WatForm/dashplus
cd ./dashplus
./gradlew build
```

The generated .jar file is located at `dashplus/app/build/libs/app.jar`

The build process uses gradle version 9.1.0. However, installing this globally is unnecessary, since it is downloaded as part of the build process.

The build process is the same for Linux, MacOS and Windows powershell. On Windows cmd, `./gradlew build` is replaced by `gradlew.bat`

Run with the desired entry point (see list of args taken in app/src/main/java/ca/uwaterloo/watform/Main.java): 
`java -cp app/build/libs/app.jar ca.uwaterloo.watform.Main --test`

## Instructions for Contributors:
- Directory names: All lowercase and avoid underscores

- Class and .java file names: UpperCamelCase

- Method and variable names: lowerCamelCase 

- If a code formatter is used, please use [google-java-format](https://github.com/google/google-java-format)

- If an IDE is used, please ensure that IDE-generated files are not present in any of the commits. This can be done by including such files in the .gitignore

- See Error Handling Strategy in [`app/src/main/java/ca/uwaterloo/watform/utils/ErrorHandling.md`](/app/src/main/java/ca/uwaterloo/watform/utils/ErrorHandling.md)

## Before Committing
- `./gradlew spotlessApply` to format code with google-java-format
- `./gradlew build` will check for formatting issues as a dependent gradle task

## Testing:
- `git submodule update --init --recursive` to download [dash-testing](https://github.com/WatForm/dash-testing#)

- `./gradlew test` for unit testing

- For testing using a JAR, the main entry point is: [`app/src/main/java/ca/uwaterloo/watform/test/Main.java`](app/src/main/java/ca/uwaterloo/watform/test/Main.java)
    > **Note:** If the `Dash.g4` grammar is changed, you must test it with the catalyst-corpus. 
    See the instructions in [`app/src/main/java/ca/uwaterloo/watform/test/README.md`](app/src/main/java/ca/uwaterloo/watform/test/README.md).



