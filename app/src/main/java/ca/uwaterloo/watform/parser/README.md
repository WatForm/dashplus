# What this CLI does in sequence
## Antlr parse
- Depending on the extension (either .als or .dsh) we start parsing at AlloyFile or DashFile
- see Dash.g4; it contains the grammar for both Dash and Alloy 
- DashLexer.java (Antlr generated) is extended to bail early; stop at first lexical error
- DashParser.java (Antlr generated) is extended to bail early; stop at first ParseCanellationError
- Antlr's parse produces an Antlr AST 

# Parser Visitor
- Antlr generated DashBaseVisitor.java (in app/build/generated-src/antlr/main/antlr/generated) is extended
- This is a visitor to walk over Antlr's AST to generated our own AST objects

# AST
- in alloyast/ and dashast/

# AlloyModel
- HashTables collecting all the paragraphs
- A well-formedness check
- You can add to these HasTables, and a WFF check is done

# toString
- The AlloyModel is turned to a string and passed to Alloy6 Jar
- This calls the toString method of AlloyAST

