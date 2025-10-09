# Gradle and Antlr
- The ANTLR plugin is applied in app/build.gradle
- The ANTLR plugin adds 3 tasks to the project, as shown below.
    1) generateGrammarSource 
        - Generates the source files for all production ANTLR grammars.
        - Default path: src/main/antlr 
        - compileJava's dependency

    2) generateTestGrammarSource
        - Generates the source files for all test ANTLR grammars.
        - Default path: src/test/antlr
        - compileTestJava's dependency
        - Note: see app/src/test/java/org/antlr for JUnit tests for testing ANTLR; not using generateTestGrammarSource

    3) generateSourceSetGrammarSource
        - Generates the source files for all ANTLR grammars for the given source set.
        - Default path: src/sourceSet/antlr
        - compileSourceSetJava's dependency
- So if you run ./gradlew build (or ./gradlew compileJava) or ./gradlew test (see app/build.gradlew), Gradle will first execute the generateGrammarSource task
- You can find ANTLR generated files in app/build/generated-src/antlr/main/
- This header in Alloy.g4 specifies the package where you can import antlr generated classes like AlloyLexer
    ```
    @header {
        package antlr.generated;
    }
    ```
- Then compileJava compiles those generated .java files together with src/main/java sources
- Don't put the antlr generated files in src/
    - https://stackoverflow.com/questions/36469546/what-is-minimal-sample-gradle-project-for-antlr4-with-antlr-plugin

- Reference: https://docs.gradle.org/current/userguide/antlr_plugin.html

