# Error Handling

* Try to group dir specific exceptions together (could be in class)
* Use Pos whenever possible

## Implementation Errors 
- Throw ImplementationError directly
- ImplementationError reflect an error in code (not user input)
- These should not be caught and the program should be allowed to crash

## User Errors
- Use the Reporter object 
- There are a few places where UserErrors can be thrown: AST construction, AlloyModel, DashModel, and maybe more in the future
- It is up to the higher level caller to decide how to interpret them 

#### AST Ctor Errors (AlloyCtorError)
- These are well-formedness errors that are thrown at construction time of ASTs
- During parsing, these should be caught and treated as user feedback by adding them into the Reporter
- AST construction can also occur during Dash to Alloy translation
    - AlloyCtorError are not caught and allowed to propagate to crash the program
    - Because this reflects erroneous implementation, not bad user input

- Below is an example of how to handle a syntax error and exit somewhat gracefully via the Reporter
- Throw AlloyCtorError [(example)](/app/src/main/java/ca/uwaterloo/watform/alloyast/AlloyFile.java)
- Choose where to catch it to control how far to trace back and continue if wanted [(example)](/app/src/main/java/ca/uwaterloo/watform/utils/ParserUtil.java) 
- Add into Reporter [(example)](/app/src/main/java/ca/uwaterloo/watform/utils/ParserUtil.java)
- Call exitIfHasErrors() if needed [(example)](/app/src/main/java/ca/uwaterloo/watform/utils/ParserUtil.java)

#### AlloyModelError
- similar to AlloyCtorError
- Throw AlloyModelError [(example)](/app/src/main/java/ca/uwaterloo/watform/alloymodel/AlloyModelTable.java)
- Catch it and add to Reporter [(example)](/app/src/main/java/ca/uwaterloo/watform/parser/Main.java)
- Call exitIfHasErrors() [(example)](/app/src/main/java/ca/uwaterloo/watform/parser/Main.java)

