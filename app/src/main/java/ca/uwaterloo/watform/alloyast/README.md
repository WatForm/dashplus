# Alloy AST

* AlloyBinaryExpr, AlloyUnaryExpr, AlloyVarExpr are not abstract so visitors can create them directly.  However their subclasses MUST not add any fields.

