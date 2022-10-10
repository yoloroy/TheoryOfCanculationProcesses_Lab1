import Grammar.*

abstract class FsmGrammar(
    initialSymbol: NonTerminal,
    val alphabet: List<Terminal>,
    val states: List<NonTerminal>,
    val fsmRules: Map<NonTerminal, List<NonTerminal?>>
): Grammar(
    initialSymbol = initialSymbol,
    terminals = alphabet,
    nonTerminals = states,
    rules = fsmRules.flattenToRules(alphabet)
) {
    init {
        require(fsmRules.all { (_, transitions) -> transitions.size == fsmRules.values.first().size })
    }

    override fun produce(code: List<Int>): List<Symbol> {
        val list = mutableListOf<Terminal>()
        var state = initialSymbol
        for (i in code) {
            list += alphabet[i]
            val transitions = fsmRules[state] ?: throw IllegalArgumentException("Bad code")
            state = transitions[i] ?: continue
        }
        return list
    }

    override fun produceStepByStep(code: List<Int>): Sequence<List<Symbol>> = sequence {
        val list = mutableListOf<Terminal>()
        var state = initialSymbol
        for (i in code) {
            list += alphabet[i]
            val transitions = fsmRules[state] ?: throw IllegalArgumentException("Bad code")
            state = transitions[i] ?: continue
            yield(list + state)
        }
        yield(list)
    }

    override fun parse(sequence: CharSequence): List<Int> {
        require(test(sequence))
        return sequence.map { c -> alphabet.indexOfFirst { it.string == c.toString() } }
    }

    override fun test(sequence: CharSequence): Boolean {
        var state = initialSymbol
        val sequenceAsSteps = sequence.map { c -> alphabet.indexOfFirst { it.string == c.toString() } }
        for (i in sequenceAsSteps.dropLast(1)) {
            state = fsmRules[state]!![i] ?: return false
        }
        if (fsmRules[state]!![sequenceAsSteps.last()] != null) return false
        return true
    }

    override fun toString() = """${this::class.simpleName}: G = (
	VN = {${nonTerminals.present(", ")}},
	VT = {${terminals.present(", ")}},
	P = {${
        fsmRules
            .toList()
            .joinToString(
                separator = ",\n\t\t",
                prefix = "\n\t\t",
                postfix = "\n\t",
                transform = { (oldState, newStates) -> "${oldState.string} -> ${newStates.withIndex().joinToString { (i, v) -> "$i: ${(v?.string ?: "-")}"}}" }
            )
    }},
	S = $initialSymbol
)"""
}

private fun Map<NonTerminal, List<Symbol?>>.flattenToRules(alphabet: List<Terminal>) = flatMap { (oldState, transitions) ->
    return@flatMap transitions.mapIndexed { i, newState -> oldState produces listOfNotNull(alphabet[i], newState) }
}
