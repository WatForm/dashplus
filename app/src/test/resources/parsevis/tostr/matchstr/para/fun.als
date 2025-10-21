fun someName: A {a}

fun someName [b:B] : A {a}
fun someName (b:B) : A {a}

fun someName [b:B, c:C] : A {a}
fun someName (b:B, c:C) : A {a}

fun univ.someName: A {a}

fun univ.someName [b:B] : A {a}
fun univ.someName (b:B) : A {a}

fun univ.someName [b:B, c:C] : A {a}
fun univ.someName (b:B, c:C) : A {a}

private fun someName : A {a}

private fun someName [b:B] : A {a}
private fun someName (b:B) : A {a}

private fun someName [b:B, c:C] : A {a}
private fun someName (b:B, c:C) : A {a}

private fun univ.someName : A {a}

private fun univ.someName [b:B] : A {a}
private fun univ.someName (b:B) : A {a}

private fun univ.someName [b:B, c:C] : A {a}
private fun univ.someName (b:B, c:C) : A {a}

fun someName: lone A {a}

fun someName [b:B] : one A {a}
fun someName (b:B) : some A {a}

fun someName [b:B, c:C] : set A {a}
fun someName (b:B, c:C) : lone A {a}

fun univ.someName: one A {a}

fun univ.someName [b:B] : some A {a}
fun univ.someName (b:B) : set A {a}

fun univ.someName [b:B, c:C] : lone A {a}
fun univ.someName (b:B, c:C) : one A {a}

private fun someName : some A {a}

private fun someName [b:B] : set A {a}
private fun someName (b:B) : lone A {a}

private fun someName [b:B, c:C] : one A {a}
private fun someName (b:B, c:C) : some A {a}

private fun univ.someName : set A {a}

private fun univ.someName [b:B] : lone A {a}
private fun univ.someName (b:B) : one A {a}

private fun univ.someName [b:B, c:C] : some A {a}
private fun univ.someName (b:B, c:C) : set A {a}

