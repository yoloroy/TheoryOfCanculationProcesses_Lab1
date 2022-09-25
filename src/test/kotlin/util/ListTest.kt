package util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ListTest {

    @Test
    fun replace() {
        val initialList = "0123456789".toList()
        val expected = "04356789".toList()
        val actual = initialList.replace("1234".toList(), "43".toList())
        assertEquals(expected, actual)
    }

    @Test
    fun replaceByIndices() {
        val initialList = "0123456789".toList()
        val expected = "04356789".toList()
        val actual = initialList.replace(1, 4, "43".toList())
        assertEquals(expected, actual)
    }
}