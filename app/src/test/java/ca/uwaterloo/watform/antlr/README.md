# CLI arguments
./gradlew test -PstopOnFirstFail=false (true by default)

# Jar Failed, ANTLR Passed
- CompUtil.parseEverything_fromString will do more than parsing
- For example, it will raise exception for name not found
- It will not throw for duplicate sigs
- So tesing ANTLR's parser against this is not so meaningful

