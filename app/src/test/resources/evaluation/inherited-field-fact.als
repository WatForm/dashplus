abstract sig Object {}

sig Dir extends Object {
    parent: lone Dir
}

one sig Root extends Dir {} {
    no parent
}
