fun main() {
    TaskAction.Help.execute()
    while (true) {
        print("> ")
        when (val actionCode = readln().trim().toIntOrNull()) {
            null -> println("Это не код опции")
            !in TaskAction.codes -> println("Такой опции нет")
            else -> TaskAction.byCode(actionCode).execute()
        }
    }
}
