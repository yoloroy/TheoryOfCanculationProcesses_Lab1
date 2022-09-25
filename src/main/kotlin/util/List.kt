package util

fun <T> List<T>.replace(oldValue: List<T>, newValue: List<T>): List<T> {
    var startI: Int? = null
    for (i in 0..(size - oldValue.size)) {
        if (subList(i, i + oldValue.size) == oldValue) {
            startI = i
            break
        }
    }
    startI ?: throw NoSuchElementException()
    return replace(startI, oldValue.size, newValue)
}

fun <T> List<T>.replace(startIndex: Int, replaceSize: Int, newValue: List<T>) = buildList {
    val initial = this@replace
    addAll(initial.take(startIndex))
    addAll(newValue)
    addAll(initial.takeLast(initial.size - startIndex - replaceSize))
}

fun <T> List<T>.twice() = this + this
