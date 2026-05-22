fun main() {

    // Safe Call Operator = ?. => Call method/property ONLY IF object is not null
    // Elvis Operator = ?: => Provide default value if left side is null

    println("Enter a number: ")
    val input = readln()
    val number = input.toIntOrNull() ?: 0
    println("$number is even = ${number % 2 == 0}")
    val incremented = input.toIntOrNull()?.inc()
    println("Incremented Number = $incremented")
}

class `8Scopes` {
}