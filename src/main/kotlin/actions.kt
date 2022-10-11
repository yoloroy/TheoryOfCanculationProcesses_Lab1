import kotlin.system.exitProcess

private val grammar = Lab2Task11FsmGrammar()

fun testSequenceAction() {
    println("Введите строку для теста соответсвия грамматике")
    val sequence = readln()

    if (grammar.test(sequence)) println("Строка соответствует грамматике")
    else println("Строка не соответствует грамматике")
}

fun grammarInfoAction() {
    println(grammar.toString())
}

fun parseSequenceAction() {
    println("Введите строку для парсинга")
    val sequence = readln().trim()

    if (!grammar.test(sequence)) println("Цепочка не соответствует грамматике")

    try {
        println(grammar.parse(sequence).joinToString(","))
    } catch (e: IllegalArgumentException) {
        println("Введённая цепочка не может быть разобрана")
    }
}

fun produceSequenceByCodeAction() {
    val code = readInput<List<Int>, NumberFormatException>(
        map = { line -> line.split(",").map { it.trim().toInt() } },
        onError = { println("Код цепочки состоит из целых чисел, разделённых запятой формата: <1,2,3,4>") },
        inputCall = "Введите код цепочки, разделитель - запятая:"
    )
    val sequence = grammar.produce(code).present("")
    try {
        println("Ваша цепочка: $sequence")
    } catch (e: NoSuchElementException) {
        println("Вы ввели плохой код, создание цепочки по нему невозможно")
    }
    if (!grammar.test(sequence)) println("Цепочка не соответствует грамматике")
}

fun produceSequenceStepByStepAction() {
    val code = readInput<List<Int>, NumberFormatException>(
        map = { line -> line.split(",").map { it.trim().toInt() } },
        onError = { println("Код цепочки состоит из целых чисел, разделённых запятой формата: <1,2,3,4>") },
        inputCall = "Введите код цепочки, разделитель - запятая:"
    )
    try {
        grammar.produceStepByStep(code)
            .map { symbols -> symbols.present("") }
            .forEach { string ->
                println("Ваша цепочка: $string")
                println("Для продолжения нажмите <Enter>")
                readln()
            }
    } catch (e: NoSuchElementException) {
        println("Вы ввели плохую цепочку, дальнейшее её выполнение невозможно")
    }
    println("Вот создание и закончилось")
}

fun exitAppAction() {
    println("Вы уверены, что хотите покинуть программу?")
    println("0 - да, другой ввод - нет")
    if (readln() == "0") exitProcess(0)
}

fun appInfoAction() {
    TaskAction.values()
        .sortedBy(TaskAction::code)
        .map(TaskAction::label)
        .forEach(::println)
}

private inline fun <T : Any, reified E: RuntimeException> readInput(
    map: (String) -> T,
    onError: () -> Unit,
    inputCall: String
): T {
    var value: T? = null
    while (value == null) {
        println(inputCall)
        try {
            value = map(readln())
        } catch (e: RuntimeException) {
            if (e is E) onError()
            else throw e
        }
    }
    return value
}
