# Class Hierarchy
- it's all flat 

AlloyVarExpr \
├── AlloyAtNameExpr \
├── AlloyDisjExpr \
├── AlloyFunMaxExpr \
├── AlloyFunMinExpr \
├── AlloyFunNextExpr \
├── AlloyIdenExpr \
├── AlloyIntExpr \
├── AlloyNameExpr \
├── AlloyNoneExpr \
├── AlloyNumExpr \
├── AlloyPredToOrdExpr \
├── AlloyQnameExpr \
├── AlloySeqExpr \
├── AlloySeqIntExpr \
├── AlloySigIntExpr \
├── AlloyStepsExpr \
├── AlloyStrLiteralExpr \
├── AlloyStringExpr \
├── AlloySumExpr \
├── AlloyThisExpr \
└── AlloyUnivExpr 

# AlloyScopableExpr
- sealed **interface**; it's not part of the **class** hierarchy
- these are vars that be given a scope in commands
- AlloyScopableExpr is created because AlloyVarExpr is too general in 
    [AlloyCmdPara](/app/src/test/java/ca/uwaterloo/watform/alloyast/AlloyCmdParaTest.java)
- see `typescope` in [Dash.g4](/app/src/main/antlr/Dash.g4)

AlloyScopableExpr \
├── AlloyQnameExpr \
├── AlloySigIntExpr \
├── AlloyIntExpr \
├── AlloySeqExpr \
├── AlloyStringExpr \
└── AlloyStepsExpr 

# AlloySigRefExpr
- sealed **interface**; it's not part of the **class** hierarchy
- these are vars that are references to relations
- AlloySigRefExpr is used when AlloyVarExpr is too general: 
    - [AlloyImportPara](/app/src/main/java/ca/uwaterloo/watform/alloyast/paragraph/AlloyImportPara.java)
    - [AlloySigPara](/app/src/main/java/ca/uwaterloo/watform/alloyast/paragraph/sig/AlloySigPara.java)
    - [AlloyPredPara](/app/src/main/java/ca/uwaterloo/watform/alloyast/paragraph/AlloyPredPara.java)
    - [AlloyFunPara](/app/src/main/java/ca/uwaterloo/watform/alloyast/paragraph/AlloyFunPara.java)

AlloySigRefExpr \
├── AlloyQnameExpr \
├── AlloyUnivExpr \
├── AlloyStringExpr \
├── AlloyStepsExpr \
├── AlloySigIntExpr \
├── AlloySeqIntExpr \
└── AlloyNoneExpr 

