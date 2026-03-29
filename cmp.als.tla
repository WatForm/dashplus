---- MODULE cmp.als ----
EXTENDS FiniteSets, Integers, Sequences
CONSTANTS A_set, B_set
VARIABLES A, B

\* Translation macros
_univ == A_set \union B_set
_none == {}
_iden == {<<_x,_x>> : _x \in _univ}
_some(_S) == ~(\A _x, _y \in _S : (_x = _y))
_lone(_S) == \A _x, _y \in _S : (_x = _y)
_one(_S) == (\A _x, _y \in _S : (_x = _y)) /\ _S /= {}
_no(_S) == (\A _x, _y \in _S : (_x = _y)) /\ _S = {}
_transpose(_R) == {<<_y,_x>> : <<_x,_y>> \in _R}
_domain_restriction(_S,_R) == {_x \in _R : _x[1] \in _S}
_range_restriction(_R,_S) == {_x \in _R : _x[Len(_x)] \in _S}
_inner_product_map(_e1,_e2) == _e1 \o _e2
_inner_product_filter(_e1,_e2) == _e1[Len(_e1)] = _e2[1]
_inner_product(_R1,_R2) == {_inner_product_map(_e1,_e2) : <<_e1,_e2>> \in {}}
_relational_override(_R1,_R2) == (_R1 \ {_x \in _R1 : \E _y \in _R2 : (_x[1] = _y[1])}) \union _R2

\* topological sort on signatures: [A, B]
_sig_sets_unprimed == 
A \in SUBSET {<<_x>> : _x \in A_set} 
/\ B \in SUBSET {<<_x>> : _x \in B_set}
_sig_sets_primed == 
A' \in SUBSET {<<_x>> : _x \in A_set} 
/\ B' \in SUBSET {<<_x>> : _x \in B_set}

\* signature constraints
_all_sig_constraints == TRUE

\* facts
_all_facts == TRUE

\* INIT relation
_Init == 
_sig_sets_unprimed 
/\ _all_sig_constraints 
/\ _all_facts

\* NEXT relation
_Next == UNCHANGED <<A,B>>
====