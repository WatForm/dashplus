# Model-Sets 

This directory contains directories of alloy models.  These directories should follow the naming convention: YYYY-MM-DD-HH-MM-SS, which is the date/time when the set was created here.  Each directory should contain YYYY-MM-DD-HH-MM-SS/README.md, which summarizes how this set of alloy models was created (source and filters).

# Adapt Alloy 4 to test Antlr grammar
- primed variables used as qnames (allowed in Alloy 4) are changed to 'xPrime' for the purpose of testing the antlr grammar
- 'steps' is a keyword in Alloy and cannot be used as a module name

