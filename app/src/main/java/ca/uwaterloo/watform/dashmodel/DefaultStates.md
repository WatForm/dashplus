# Correctness of Defaults in Dash+

The following table shows the rules for correctness of labelling child 
states as default states in a Dash+ model.

defList is the set of children the user has labelled as defaults.

The number of OR states are the labels of the rows.
The number of AND states are the labels of the columns.

| OR\AND                   | 0                     | >=1; no def           | >=1; some but not all def | >=1; all def          |
|--------------------------|-----------------------|-----------------------|---------------------------|-----------------------|
| 0                        | LEAF STATE            | fix defList to be all | ERROR allAndDefaults      | defList okay as is    |
| 1; not def               | fix defList to be 1   | ERROR missingDefault  | ERROR allAndDefaults      | defList okay as is    |
| 1; def                   | defList okay as is    | defList okay as is    | ERROR tooManyDefaults     | ERROR tooManyDefaults |
| >1; no def               | ERROR missingDefault  | ERROR missingDefault  | ERROR tooManyDefaults     | defList okay as is    |
| >1; 1 def                | defList okay as is    | defList okay as is    | ERROR tooManyDefaults     | ERROR tooManyDefaults |
| >1; some or all def      | ERROR tooManyDefaults | ERROR tooManyDefaults | ERROR tooManyDefaults     | ERROR tooManyDefaults |
