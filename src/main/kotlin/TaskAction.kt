enum class TaskAction(
    val code: Int,
    val label: String,
    val execute: () -> Unit
) {
    TestSequence(
        code = 1,
        label = "1 - Проверить, может ли цепочка быть порождена грамматикой",
        execute = ::testSequenceAction
    ),
    GrammarInfo(
        code = 2,
        label = "2 - Информация по грамматике",
        execute = ::grammarInfoAction
    ),
    ParseSequence(
        code = 3,
        label = "3 - Разобрать цепочку на шаги её создания",
        execute = ::parseSequenceAction
    ),
    ProduceSequenceByCode(
        code = 4,
        label = "4 - Создать цепочку по заданному коду",
        execute = ::produceSequenceByCodeAction
    ),
    ProduceSequenceByCodeStepByStep(
        code = 5,
        label = "5 - Просмотреть пошагово создание цепочки по заданному коду",
        execute = ::produceSequenceStepByStepAction
    ),
    ExitAction(
        code = 0,
        label = "0 - Выйти из программы",
        execute = ::exitAppAction
    ),
    Help(
        code = -1,
        label = "-1 - Справка по программе",
        execute = ::appInfoAction
    );

    companion object {
        fun byCode(code: Int) = values().first { it.code == code }

        val codes get() = values().map { it.code }
    }
}
