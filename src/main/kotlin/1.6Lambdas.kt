package lambdas
fun main() {
    val numbers = intArrayOf(10,20,30,40,50,60)
    val result = numbers.filter { it % 3 == 0 }
    println(result)
}