# dashast

These classes are to store the parsed elements of a Dash model.

DashFile is the top class to use, which consists of a number of DashParagraphs.

DashState is a subclass of AlloyParagraph.

All others are subclasses of either ASTNode or DashExpr (which is a subclass of ASTNote).

DashExpr provides a wrapper for printing method expressions of DashFrom, DashDo, etc.  

DashRef is a subclass of AlloyExpr that represents expressions such as:
Root/A/B[exp1,exp2]/v1
for events, states, trans, and vars.  This is an addition to what's allowed in the grammar for expressions.

DashParam is a subclass of AlloyExpr that is a completely internal class to represent a parameter that consists of (statename, parameter name).  It was needed to reverse engineer a parameter expression into a "thisState".
It is used in parameter lists.  It can also be used in an expression.
Each parameter will be turned into a signature in Alloy.
Previously we changed "thisState" into p1_AID during the revolse phase, which is not reversable into a thisState (for DashTablesToDashAST).  Having DashParam is a less specific to translation and provides an intermediate abstraction.