# Error Handling

* Try to group dir specific exceptions together (could be in private class)
* Use Pos whenever possible

## Implementation Errors 
- Throw ImplementationError directly

## User Errors
- Use the Reporter object 
- Throw a new Reporter.ErrorUser [(example)](/app/src/main/java/ca/uwaterloo/watform/alloyast/paragraph/command/AlloyCmdPara.java)
- Catch it to control how much you want to trace back and continue [(example)](/app/src/main/java/ca/uwaterloo/watform/alloyast/AlloyFileParseVis.java) 
- Add the Reporter.ErrorUser into Reporter [(example)](/app/src/main/java/ca/uwaterloo/watform/alloyast/AlloyFileParseVis.java)
- Call exitIfHasErrors() if needed [(example)](/app/src/main/java/ca/uwaterloo/watform/utils/ParserUtil.java)




