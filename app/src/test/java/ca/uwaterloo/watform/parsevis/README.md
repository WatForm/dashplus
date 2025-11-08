# Purpose
- These tests are designed to test .g4 grammar, parser visitors
- The input files are expected to not contain WFF errors
- To test WFF, see alloyast/ and alloymodel/

# CLI arguments
./gradlew test -PstopOnFirstFail=false (true by default)

# Jar Failed, ANTLR Passed
- CompUtil.parseEverything_fromString will do more than parsing
- For example, it will raise exception for name not found
- It will not throw for duplicate sigs
- So tesing ANTLR's parser against this is not so meaningful

