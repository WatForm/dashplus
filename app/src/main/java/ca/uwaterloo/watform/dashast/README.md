# dashast

These classes are simply to store the parsed elements of a Dash model.

DashFile is the top class to use and is a subclass of AlloyFile

DashState is a subclass of AlloyParagraph.

All others are subclasses of either ASTNode or DashExpr (which is a subclass of ASTNote).
