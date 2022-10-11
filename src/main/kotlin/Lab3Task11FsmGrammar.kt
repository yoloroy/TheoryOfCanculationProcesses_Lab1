class Lab3Task11FsmGrammar : FsmGrammar(
    initialSymbol = start,
    alphabet = listOf(five, six),
    states = listOf(start, TODO()),
    fsmRules = mapOf(
        start to listOf(TODO()),
        TODO()
    )
) {
    companion object {
        val start = "S".nonTerminal
        val five = "5".terminal
        val six = "6".terminal
    }
}
