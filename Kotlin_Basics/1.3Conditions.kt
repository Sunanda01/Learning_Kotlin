fun main() {
    println("Enter a number => ")
    val number = readln()
    val inputAsInteger = number.toIntOrNull()
    when(inputAsInteger) {
        null -> println("Not an integer")
        in 10..20 -> println("Number is between 10 and 20")
        else -> println("Input: $inputAsInteger")
    }
}