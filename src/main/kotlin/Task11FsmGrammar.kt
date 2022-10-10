/**
 * FSM defined as transitions matrix:
 *
 * | States | 1 | 2 | 3 | 4 |
 * |--------|---|---|---|---|
 * |    S   | B | - | A | - |
 * |    A   | - | A | - | Z |
 * |    B   | B | - | - | Z |
 * |    Z   | - | B | A | - |
 */
class Task11FsmGrammar : FsmGrammar(
    initialSymbol = S,
    alphabet = listOf(zero, one, two, three),
    states = listOf(S, A, B, Z),
    fsmRules = mapOf(
        S to listOf(B, null, A, null),
        A to listOf(null, A, null, Z),
        B to listOf(B, null, null, Z),
        Z to listOf(null, B, A, null)
    )
) {
    companion object {
        val S = "S".nonTerminal
        val A = "A".nonTerminal
        val B = "B".nonTerminal
        val Z = "Z".nonTerminal
        val zero = "0".terminal
        val one = "1".terminal
        val two = "2".terminal
        val three = "3".terminal
    }
}