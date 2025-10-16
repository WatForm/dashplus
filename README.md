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


## Instructions for Contributors:

- If an IDE is used, please ensure that IDE-generated files are not present in any of the commits. This can be done by including such files in the .gitignore

- If a code formatter is used, please use [google-java-format](https://github.com/google/google-java-format)


