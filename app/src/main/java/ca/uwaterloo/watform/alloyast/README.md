# AlloyCtorError
- Alloy AST constructors can throw exceptions if the instance are not well-formed
- These errors can be caught by the caller to treat as feedback for the user (Parser Visitor)
- If they are not caught, they are interpreted as Implementation Errors; there's something wrong with the code

# equals and hashCode
- The equals and hashCode are not semantic aware
    - ex: (A and B) is not equal to (B and A)
    - this may change in the future
    - Nov 20, 2025

