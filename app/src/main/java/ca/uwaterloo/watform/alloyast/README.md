# AlloyCtorError
- Alloy AST constructors can throw exceptions if the instance are not well-formed
- These errors can be caught by the caller to treat as feedback for the user (Parser Visitor)
- If they are not caught, they are interpreted as Implementation Errors; there's something wrong with the code

# equals and hashCode
- The equals and hashCode are not semantic aware
    - ex: (A and B) is not equal to (B and A)
    - this may change in the future
    - Nov 20, 2025

# Non-Nullable fields
- All AST fields are non-nullable
- Optional fields are wrapped in the Optional class
- `GeneralUtil.reqNonNull` is used to check all fields are not null at the end of ctors
    - NOTE: ctor args can be null for optional fields, but by end
        of ctor, all fields should not be null
    - AlloyBinaryExpr ex: 
        `reqNonNull(nullField(pos, this), this.left, this.right, this.op);`
- If a field is null, an Implementation Error is thrown: `ImplementationError.nullField`

