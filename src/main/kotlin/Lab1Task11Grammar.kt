@file:Suppress("SpellCheckingInspection")

import util.twice

/**
 * Definition: `L(G)={a[1]a[2]…a[n]a[1]a[2]…a[n]| a[i]∈{c, d}}`
 */
class Lab1Task11Grammar : Grammar(
    terminals = listOf(c, d),
    nonTerminals = listOf(Initial, Factory, Middle, End, CGoingToFinish, DGoingToFinish, C, D),
    initialSymbol = Initial,
    rules = listOf(
        Initial produces listOf(Factory, End), // init
        Factory produces listOf(c, Factory, C), // create c
        Factory produces listOf(d, Factory, D), // create d
        Factory produces Middle, // finish half of string
        listOf(CGoingToFinish, C) produces listOf(C, CGoingToFinish), // swap moving C with static C
        listOf(DGoingToFinish, C) produces listOf(C, DGoingToFinish), // swap moving D with static C
        listOf(CGoingToFinish, D) produces listOf(D, CGoingToFinish), // swap moving C with static D
        listOf(DGoingToFinish, D) produces listOf(D, DGoingToFinish), // swap moving D with static D
        listOf(CGoingToFinish, End) produces listOf(End, CGoingToFinish), // swap moving C with Finish
        listOf(DGoingToFinish, End) produces listOf(End, DGoingToFinish), // swap moving D with Finish
        listOf(End, CGoingToFinish) produces listOf(End, c), // "save" C at finish
        listOf(End, DGoingToFinish) produces listOf(End, d), // "save" D at finish
        listOf(Middle, C) produces listOf(Middle, CGoingToFinish), // start journey for leftest 'C'
        listOf(Middle, D) produces listOf(Middle, DGoingToFinish), // start journey for leftest 'D'
        listOf(Middle, End) produces emptyList() // finish creation
    )
) {

    override fun test(sequence: CharSequence): Boolean {
        if (sequence.length % 2 != 0) return false
        val leftPart = sequence.slice(0 until sequence.length / 2)
        val rightPart = sequence.slice(sequence.length / 2..sequence.lastIndex)
        return (
            leftPart.all { c -> c.toString() in terminals.map { t -> t.string } } && // TODO
            leftPart == rightPart
        )
    }

    override fun parse(sequence: CharSequence): List<Int> {
        require(test(sequence))
        val leftHalf = sequence.slice(0 until sequence.length / 2)
        val rightHalf = leftHalf.reversed()

        val initIndex = 0
        val creationIndices = mapOf('c' to 1, 'd' to 2)
        val middleIndex = 3
        val beginJourneyIndices = mapOf('c' to 12, 'd' to 13)
        val swapIndices = mapOf(
            listOf('c', 'c') to 4,
            listOf('d', 'c') to 5,
            listOf('c', 'd') to 6,
            listOf('d', 'd') to 7
        )
        val finishingSwapsIndices = mapOf('c' to listOf(8, 10), 'd' to listOf(9, 11))
        val finishIndex = 14

        return buildList {
            add(initIndex)
            addAll(leftHalf.map { creationIndices.getValue(it) })
            add(middleIndex)
            addAll(rightHalf.flatMapIndexed { index, runningChar -> buildList {
                add(beginJourneyIndices.getValue(runningChar))
                addAll(rightHalf.drop(index + 1).map { staticChar -> swapIndices.getValue(listOf(runningChar, staticChar)) })
                addAll(finishingSwapsIndices.getValue(runningChar))
            }})
            add(finishIndex)
        }
    }

    fun randomProducerByN(n: Int) = iterator {
        while (true) yield (
            List(n) { terminals.random() }.twice()
        )
    }

    companion object {
        val Initial = "I".nonTerminal
        val Factory = "F".nonTerminal
        val Middle = "M".nonTerminal
        val End = "E".nonTerminal
        val CGoingToFinish = "C'".nonTerminal
        val DGoingToFinish = "D'".nonTerminal
        val C = "C".nonTerminal
        val D = "D".nonTerminal
        val c = "c".terminal
        val d = "d".terminal
    }
}

/* producing example:
0.1.2.2.1.2.2.2.1.2.2.3         // cddcdddcdd<Middle><D><D><C><D><D><D><C><D><D><C><Finish> // creating left part with
    .13.7.5.7.7.7.5.7.7.5.9.11  // cddcdddcdd<Middle><D><C><D><D><D><C><D><D><C><Finish>d   // moving 'd' to finish
    .13.5.7.7.7.5.7.7.5.9.11    // cddcdddcdd<Middle><C><D><D><D><C><D><D><C><Finish>dd     // moving 'd' to finish
    .12.6.6.6.4.6.6.4.8.10      // cddcdddcdd<Middle><D><D><D><C><D><D><C><Finish>cdd       // moving 'c' to finish
    .13.7.7.5.7.7.5.9.11        // cddcdddcdd<Middle><D><D><C><D><D><C><Finish>dcdd         // moving 'd' to finish
    .13.7.5.7.7.5.9.11          // cddcdddcdd<Middle><D><C><D><D><C><Finish>ddcdd           // moving 'd' to finish
    .13.5.7.7.5.9.11            // cddcdddcdd<Middle><C><D><D><C><Finish>dddcdd             // moving 'd' to finish
    .12.6.6.4.8.10              // cddcdddcdd<Middle><D><D><C><Finish>cdddcdd               // moving 'c' to finish
    .13.7.5.9.11                // cddcdddcdd<Middle><D><C><Finish>dcdddcdd                 // moving 'd' to finish
    .13.5.9.11                  // cddcdddcdd<Middle><C><Finish>ddcdddcdd                   // moving 'd' to finish
    .12.8.10                    // cddcdddcdd<Middle><Finish>cddcdddcdd                     // moving 'c' to finish
    .14                         // cddcdddcddcddcdddcdd                                     // finish creation
0.1.2.2.1.2.2.2.1.2.2.3.13.7.5.7.7.7.5.7.7.5.9.11.13.5.7.7.7.5.7.7.5.9.11.12.6.6.6.4.6.6.4.8.10.13.7.7.5.7.7.5.9.11.13.7.5.7.7.5.9.11.13.5.7.7.5.9.11.12.6.6.4.8.10.13.7.5.9.11.13.5.9.11.12.8.10.14
*/
