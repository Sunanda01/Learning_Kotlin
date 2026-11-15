// Higher Order Function is: A function that:
// takes another function as parameter
// OR returns another function

fun execute(action: () -> Unit) {       // action = function parameter, () = empty params, Unit = retuns nothing
    println("Before action")
    action()
    println("After action")
}

fun main() {
    // passes lambda function as argument equivalent to execute( function )
    execute {
        println("Hello")
    }
}