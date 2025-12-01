# Catalyst Corpus Test
1) cd app/src/main/java/ca/uwaterloo/watform/test/catalyst
2) ./download-corpus.sh, which will download the zip (github release), unzip it, and move problematic files
3) cd back to dashplus/
4) ./gradlew releaseJar
5) java -cp app/build/libs/watform-dashplus.jar ca.uwaterloo.watform.test.Main

- It also tests for the generation of our Alloy AST objects, AlloyModel constructor, and the toString does not throw exceptions. 
- It also checkes the toString return from AlloyModel.toString() will parse again
- But it DOES NOT check if AlloyFile.toString() produces the same string

