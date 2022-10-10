import Grammar.*
import util.replace

abstract class Grammar(
    val terminals: List<Terminal>,
    val nonTerminals: List<NonTerminal>,
    val rules: List<Rule>,
    val initialSymbol: NonTerminal
) {

    abstract fun test(sequence: CharSequence): Boolean

    abstract fun parse(sequence: CharSequence): List<Int>

    open fun produce(code: List<Int>): List<Symbol> {
        var list = listOf<Symbol>(initialSymbol)
        for (i in code) list = rules[i].applyTo(list)
        return list
    }

    open fun produceStepByStep(code: List<Int>): Sequence<List<Symbol>> {
        var list = listOf<Symbol>(initialSymbol)
        return sequence {
            yield(list)
            for (i in code) {
                list = rules[i].applyTo(list)
                yield(list)
            }
        }
    }

    override fun toString() = """${this::class.simpleName}: G = (
	VN = {${nonTerminals.present(", ")}},
	VT = {${terminals.present(", ")}},
	P = {${
        rules.withIndex()
            .joinToString(
                separator = ",\n\t\t",
                prefix = "\n\t\t",
                postfix = "\n\t",
                transform = { (index, rule) -> "[$index] $rule" }
            )
    }},
	S = $initialSymbol
)"""

    sealed class Symbol(val string: String) : Comparable<String> by string {
        fun list() = listOf(this)

        abstract override fun toString(): String
    }

    class Terminal(string: String) : Symbol(string) {
        override fun toString() = "Terminal<$string>"
    }
    class NonTerminal(string: String) : Symbol(string) {
        override fun toString() = "NonTerminal<$string>"
    }

    class Rule(val oldValue: List<Symbol>, val newValue: List<Symbol>) {
        fun applyTo(symbols: List<Symbol>) = symbols.replace(oldValue, newValue)

        override fun toString() = "f: ${oldValue.present()} -> ${newValue.present()}"
    }
}

val String.terminal get() = Terminal(this)
val String.nonTerminal get() = NonTerminal(this)

infix fun List<Symbol>.produces(product: List<Symbol>) = Rule(this, product)
infix fun List<Symbol>.produces(product: Symbol) = Rule(this, product.list())
infix fun Symbol.produces(product: List<Symbol>) = Rule(this.list(), product)
infix fun Symbol.produces(product: Symbol) = Rule(this.list(), product.list())

fun List<Symbol>.present(separator: String = "") = if (isEmpty()) "ε" else joinToString(separator) { symbol ->
    when(symbol) {
        is Terminal -> symbol.string
        //is NonTerminal -> "<${symbol.string}>"
        is NonTerminal -> symbol.string
    }
}
