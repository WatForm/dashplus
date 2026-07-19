# Atom / AtomTuple / TupleSet Semantics

## `Atom`

Three kinds: `LabelAtom` — the normal, non-integer atom, `IntegerAtom` (a concrete, in-range integer), `OverflowAtom` (an integer whose true value is known to have overflowed (or cannot be precisely determined), tagged with a direction: `OVERFLOW_UP`, `OVERFLOW_DOWN`, or `OVERFLOW_UNKNOWN`).

**Equality (`threeEqual`)**
- A label is equal only to an identical label; never equal to an integer or an overflowed atom.
- Two concrete integers: equal iff their values match, as usual.
- Anything involving an overflowed atom:
  - if either side's overflow direction is unknown → `UNKNOWN` - the integer value of such overflow is unknown and *might* be anything
  - if both sides overflowed the same way → `UNKNOWN` — they *might* be the same value, can't be ruled out
  - if the sides overflowed in different directions (or one overflowed up/down while the other is concrete or overflowed the opposite way) → `FALSE` — they're certainly on opposite sides of the representable range and cannot coincide

**Ordering (`threeLessThan` and derived `>`, `>=`, `<=`)**
- Labels have no order — comparing one raises an error.
- Two concrete integers order normally.
- A value known to have overflowed up is greater than *anything* not also known to have overflowed up; overflowed down is less than anything not also overflowed down.
- Two values that overflowed in the same direction have unknowable relative order (`UNKNOWN`) — both are "very large" or "very small," but which is larger can't be determined.
- Any comparison touching an `OVERFLOW_UNKNOWN` value is `UNKNOWN`.
- `>=`, `<=`, `>` are defined from `<` and `=` the ordinary way and behave consistently under uncertainty (e.g. if `a < b` is `UNKNOWN`, then `a >= b` is `UNKNOWN` too).

**Structural identity** — a separate, non-semantic notion: two atoms "look the same" (same label, same integer value, or same overflow direction) regardless of whether they're provably equal. Two atoms that overflowed the same direction are structurally identical even though their equality is `UNKNOWN`. This is what `TupleSet` uses for deduplication, not `threeEqual` (see below).

## `AtomTuple`

An ordered, non-empty list of atoms.
- Two tuples of different arity are simply unequal (`FALSE`), never uncertain.
- Two tuples of the same arity are equal iff every position is equal (element-wise `threeEqual`, combined so that any one `FALSE` position makes the whole tuple unequal).
- `containsOverflow()` reports whether any element is an overflowed atom.
- Structural identity: same arity and every element structurally identical.

## `TupleSet`

A set of tuples (unordered, deduplicated).

**Deduplication merges by structural identity, not `threeEqual`** Two overflowed atoms with the same direction get merged even though their equality is technically `UNKNOWN`, because nothing in this evaluator can ever tell them apart — every comparison only looks at direction, so they're behaviorally identical no matter what. Atoms that overflowed in different directions are never merged, since they genuinely can be distinguished. One practical consequence: determining whether a set is a scalar can be imprecise. Multiple structurally identical tuples will be collapsed into one.

**Membership** — a tuple is `TRUE`-in a set if it definitely matches something in it, `FALSE` if it's definitely absent, `UNKNOWN` if no match is confirmed but one can't be ruled out either.

**Subset / equality** between sets follow directly: `A ⊆ B` is `TRUE`/`FALSE`/`UNKNOWN` depending on whether every element of `A` is confirmed, denied, or unresolved as a member of `B`; set equality is subset in both directions.

**Set operations are conservative under uncertainty — this is the key semantic point.**
- `union` — always exact; combines both sets and merges anything that's structurally the same.
- `crossProduct` — always exact; pairs every element of one set with every element of the other, no comparisons involved.
- `intersect(A, B)` — keeps only elements of `A` *confirmed* to be in `B`. An element whose membership in `B` is unresolved is left out, not included on the possibility that it belongs.
- `diff(A, B)` — keeps only elements of `A` *confirmed absent* from `B`. An element with unresolved membership is left out of the difference too, not assumed absent.
- `join(A, B)` — combines a tuple from `A` with a tuple from `B` only when their shared column is *confirmed* equal. A pair whose match is merely unresolved is dropped, not optimistically joined.

The upshot: `intersect(A,B)` and `diff(A,B)` together don't necessarily reconstruct all of `A` when overflow makes some memberships unresolved — some elements simply don't appear in either result. The evaluator only reports what it can prove; it never guesses an element into a result on the possibility that it belongs there.

## Arithmetic (`plus`, `minus`, `mul`, `div`, `rem`)

All five operate on scalars (arity-1 `TupleSet`s) and follow the same shape: if both operands are concrete integers, compute directly and let the result factory (`getIntScalar`) decide whether the value fits or itself becomes an overflow atom. Otherwise, at least one operand is an overflow atom, and the result direction is derived from the operands' directions rather than from any concrete value. Throughout, `OVERFLOW_UNKNOWN` means "the exact result is unknown".

**`plus`** —
- either side `OVERFLOW_UNKNOWN` → `OVERFLOW_UNKNOWN`.
- both sides overflowed with known directions: same direction → that direction (magnitude only grows further out of range); different directions → `OVERFLOW_UNKNOWN` (could cancel back into range, could stay out — unresolvable from direction alone).
- one side overflowed, one concrete: adding a same-signed concrete value keeps the direction; adding an opposite-signed one → `OVERFLOW_UNKNOWN` (might pull the value back in range, might not).

**`minus`** — computed as `a - b`, not implemented as a literal `plus(a, -b)` call, because naively flipping `b`'s direction (`UP`→`DOWN`) and reusing `plus`'s rule hides an asymmetric-range boundary case that only bites in one specific combination:
- either side `OVERFLOW_UNKNOWN` → `OVERFLOW_UNKNOWN`.
- both sides overflowed with known directions: same rule as `plus` (flip `b`'s direction, then same-direction → that direction, different → `OVERFLOW_UNKNOWN`). The boundary case below doesn't need special handling here — wherever it could matter, either the answer is genuinely `OVERFLOW_UNKNOWN` anyway, or `a`'s own unbounded magnitude forces the same conclusion regardless of exactly where `-b` lands.
- `a` overflowed, `b` concrete → reduces cleanly to `plus`'s single-overflow rule with `b`'s sign flipped: negating a concrete, finite value is exact, no boundary risk.
- `b` overflowed, `a` concrete → **this is where the asymmetric range actually matters**, and the two directions are no longer symmetric:
  - `b` overflowed `DOWN` → `-b` always exceeds `maxInt` with a full unit of cushion (`|minInt| = |maxInt| + 1`); result is `OVERFLOW_UP` whenever `a ≥ 0`, else `OVERFLOW_UNKNOWN`.
  - `b` overflowed `UP` → `-b`'s minimal-magnitude case negates to *exactly* `minInt` — in range, not overflow. Asserting `OVERFLOW_DOWN` therefore requires a **strictly negative** `a` (`a < 0`, not `a ≤ 0`); `a = 0` is not enough to guarantee clearing the boundary. If `a ≥ 0`, result is `OVERFLOW_UNKNOWN`.

**`mul`** — a product's *sign* is always determined by the two operands' signs alone.
- `0 * x = 0` always, even if `x` is `OVERFLOW_UNKNOWN` — multiplying by zero is exact regardless of the other operand.
- either nonzero side `OVERFLOW_UNKNOWN` → `OVERFLOW_UNKNOWN`.
- same sign (both effectively positive or both effectively negative) → always `OVERFLOW_UP`. This is unconditional, unlike `plus`'s same-direction case: because `|minInt| = |maxInt| + 1`, a product's magnitude only grows past the representable range, and a positive result can never land exactly back on a boundary.
- different sign → always `OVERFLOW_UNKNOWN`, never `OVERFLOW_DOWN`. This is the asymmetric-range case: negating the smallest possible `OVERFLOW_UP` value lands exactly on `minInt` — in range, not overflow — so `OVERFLOW_DOWN` can never be safely asserted from direction alone.

**`div`** — division by two concrete integers, including by zero, currently **throws** (`notSupported`) rather than modeling Alloy's documented div-by-zero special case; concrete/concrete overflow behavior is otherwise ordinary integer division.
- divisor overflowed (`UP` or `DOWN`, not `UNKNOWN`), numerator concrete and not intMin → result is exactly `0`. Otherwise - `OVERFLOW_UNKNOWN` (can be 0 or -1, undetermined).
- both operands overflowed → always `OVERFLOW_UNKNOWN`, regardless of whether directions match: the ratio of two unbounded magnitudes is unconstrained even when both are known to trend the same way.
- numerator overflowed, divisor concrete `1` → identity, direction unchanged.
- numerator overflowed, divisor concrete `-1` → negation, with the same asymmetric-range caveat as `mul`: `OVERFLOW_DOWN` → `OVERFLOW_UP` (always safe), `OVERFLOW_UP` → `OVERFLOW_UNKNOWN` (never safe to assert `DOWN`).
- numerator overflowed, `|divisor| > 1` → `OVERFLOW_UNKNOWN`. The true result is provably in range (`|numerator/divisor| < |numerator|`), but its exact value is unknown.

**`rem`** — like `div`, concrete-by-zero currently throws rather than using the algebraic identity that would make it representable (`a = (a/b)*b + r`, so `r = a` whenever `b = 0`).
- divisor concrete `1` or `-1`, numerator anything (even overflowed with unknown direction) → result is exactly `0`, unconditionally — this must be checked ahead of any direction dispatch since it holds regardless of the numerator's magnitude.
- numerator concrete and not intMin, divisor overflowed (`UP` or `DOWN`, not `UNKNOWN`) → result equals the numerator unchanged. Otherwise - `OVERFLOW_UNKNOWN` since the value may be 0 or stay the same as the numerator, undetermined. 
- numerator overflowed, `|divisor| > 1` → `OVERFLOW_UNKNOWN`. Same status as `div`'s equivalent case: provably in range, exact value not modeled.
- both operands overflowed → always `OVERFLOW_UNKNOWN`.