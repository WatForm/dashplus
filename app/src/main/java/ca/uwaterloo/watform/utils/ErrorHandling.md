# Error Handling
* Try to group dir specific exceptions together (could be in class)
* Use Pos whenever possible to make error messages more informative

### [DashPlusError](/app/src/main/java/ca/uwaterloo/watform/utils/DashPlusError.java)
- an instance of DashPlusError is either an error caused by user input or an implementation error
- ex: [AlloyCtorError](/app/src/main/java/ca/uwaterloo/watform/alloyast/AlloyCtorError.java)

### [ImplementationError](/app/src/main/java/ca/uwaterloo/watform/utils/ImplementationError.java)
- ImplementationError reflect an error in code (not user input)
- Throw ImplementationError directly
- These should not be caught and it should propagate directly to Main to exit with the corresponding exit code
    - ex: [parser.Main](/app/src/main/java/ca/uwaterloo/watform/parser/Main.java)

### [Reporter.ErrorUser](/app/src/main/java/ca/uwaterloo/watform/utils/Reporter.java)
- There are a few places where UserErrors can be thrown: AST construction, AlloyModel, DashModel, and maybe more in the future
- These should all be caught and added to Reporter; see the example below
- If they are not caught earlier, they should be at least caught in Main
    - ex: [parser.Main](/app/src/main/java/ca/uwaterloo/watform/parser/Main.java)
- Reporter.exitIfHasError will throw AbortSignal if the Reporter has collected Errors
    - the AbortSignal should only be caught in Main, and we can exit with the corresponding exit code
    - ex: [parser.Main](/app/src/main/java/ca/uwaterloo/watform/parser/Main.java)

### Example: Handling [AlloyCtorError](/app/src/main/java/ca/uwaterloo/watform/alloyast/AlloyCtorError.java)
- These are well-formedness errors that are thrown at construction time of ASTs
- During parsing, these should be caught and collected as user feedback by adding them into the Reporter
- AST construction can also occur during Dash to Alloy translation
    - AlloyCtorError are not caught and allowed to propagate to crash the program in Main
    - Because this reflects erroneous implementation, not bad user input
- This is an example of a DashPlusError that is treated as UserError (if generated during parsing) or as ImplementationError (if generated during translation)

#### AlloyCtorError generated during parsing
- Throw AlloyCtorError.redundantExactly(pos) [(example)](/app/src/main/java/ca/uwaterloo/watform/alloyast/paragraph/command/AlloyCmdPara.java)
- Choose where to catch it to control how far to trace back and continue if wanted
   - In this [example](/app/src/main/java/ca/uwaterloo/watform/alloyast/AlloyFileParseVis.java), we choose to continue parsing the other paragraphs
- Add into Reporter [(example)](/app/src/main/java/ca/uwaterloo/watform/alloyast/AlloyFileParseVis.java)
- To see this example in action, run the jar on [badCmd.als](/app/src/test/resources/reporter/badCmd.als)

#### AlloyCtorError generated during parsing
- Translation not implemented yet Dec 1, 2025
- But essentially not catch AlloyCtorError will result in the AlloyCtorError bubbling up to Main to exit with exit code

