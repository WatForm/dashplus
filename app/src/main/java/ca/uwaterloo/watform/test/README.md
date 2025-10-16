# Catalyst Corpus Test
1) cd app/src/main/java/ca/uwaterloo/watform/test/catalyst
2) ./download-corpus.sh, which will download the zip (github release), unzip it, and move problematic files
3) cd back to dashplus/
4) ./gradlew build
5) java -jar app/build/libs/app.jar --test 

- Should be able to parse everything within 10 mins with no timeouts (excpet the onces we moved)
- Note: Testing may run out of memory

