# README

## Translator

Is built in a Decorator design pattern where each class extension adds more functionality and only the last class in the sequential chain is the only class expected to be used by the outside world.

All of the classes in the sequences are named D2A

BaseD2A
AlloyModelInterfaceD2A
SpaceSigsD2A
SnapshotSigD2A
InitsD2A

DashToAlloy
# TranslateExprVis

Is its own class to translated DashExpr to AlloyExpr so it can be used by other parts of dashplus than just the Dash to Alloy translation.