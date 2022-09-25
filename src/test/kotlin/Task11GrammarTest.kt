import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Task11GrammarTest { // TODO

    private val grammar get() = Task11Grammar()

    @Test
    fun produce() {
        val expected = "cddcdddcddcddcdddcdd"
        val actual = grammar.produce(listOf(0,1,2,2,1,2,2,2,1,2,2,3,13,7,5,7,7,7,5,7,7,5,9,11,13,5,7,7,7,5,7,7,5,9,11,12,6,6,6,4,6,6,4,8,10,13,7,7,5,7,7,5,9,11,13,7,5,7,7,5,9,11,13,5,7,7,5,9,11,12,6,6,4,8,10,13,7,5,9,11,13,5,9,11,12,8,10,14))
        assertEquals(expected, actual.present())
    }

    @Test
    fun parse() {
        val expected = listOf(0,1,2,2,1,2,2,2,1,2,2,3,13,7,5,7,7,7,5,7,7,5,9,11,13,5,7,7,7,5,7,7,5,9,11,12,6,6,6,4,6,6,4,8,10,13,7,7,5,7,7,5,9,11,13,7,5,7,7,5,9,11,13,5,7,7,5,9,11,12,6,6,4,8,10,13,7,5,9,11,13,5,9,11,12,8,10,14)
        val actual = grammar.parse("cddcdddcddcddcdddcdd")
        assertEquals(expected, actual)
    }
}