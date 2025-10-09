# Catalyst Corpus Test
1) cd to dashplus/app/src/test/resources/antlr/catalyst
2) run download-corpus.sh, which will download the zip (github release), unzip it, and move problematic files
3) change the application.mainClass in app/build.gradle to 'ca.uwaterloo.watform.test.Main'
4) ./gradlew run

- Should be able to parse everything within 10 mins with no timeouts
- Note: It may run out of memory

