// lazy : Initialize ONLY when needed.
fun main() {
    println("Program Started")
    val number by lazy {
        println("Initializing number")
        10
    }
    println("Before accessing number")
    println(number)
    println(number)
    println(number)
}

//  Output
//  Program Started
//  Before accessing number
//  Initializing number
//  10
//  10
//  10
// Runs only first time accessed.
// Good for performance.