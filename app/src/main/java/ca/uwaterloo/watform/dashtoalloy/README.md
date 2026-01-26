# README

## Translator

Is built in a Decorator design pattern where each class extension adds more functionality and only the last class in the sequential chain is the only class expected to be used by the outside world.

All of the classes in the sequences are named D2A

BaseD2A
AlloyModelInterfaceD2A
SpaceSigsD2A
SnapshotSigD2A
InitsD2A
InvsD2A

TransPreD2A
TransIsEnabledAfterStep
TransTestIfNextStable
TransPostD2A
Trans
SmallStep

TracesFactD2A
TcmcFactD2A
ElectrumFactD2A

ReachabilityD2A
CompleteBigStepsD2A
AllSnapshotDiffD2A
EnoughOpsD2A
SingleEventD2A
StutterD2A

DashToAlloy

Note that the methods in these class are not all used depending on the translation option chosen.

Everything adds an Alloy predicate to the Alloy model unless it is labelled Fact in the class name.

# TranslateExprVis

Is its own class to translated DashExpr to AlloyExpr so it can be used by other parts of dashplus than just the Dash to Alloy translation.

# Notes

Depends almost completely on AlloyExprFactory to avoid "new X" extra text.