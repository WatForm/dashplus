# Differences in Well-Formedness Rules

### 1. Global Identifier Uniqueness
* **Alloy Analyzer:** Names can overlap between different paragraphs. The analyzer uses type-checking to distinguish them based on context.
* **DashPlus:** Enforces strict uniqueness when comparing Dash identifiers with Alloy identifiers. Dash identifiers across all Alloy paragraphs **cannot** overlap.
    * *See `AlloyPara.getId` for definition of paragraph identifiers.*

### 2. Field and Signature Namespace
* **Alloy Analyzer:** Allows a Field name to be identical to a Signature name.
* **DashPlus:** Field names and Signature names must be distinct.
