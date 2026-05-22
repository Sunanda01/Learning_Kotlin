// Used when : Variable initialized later
class LazyDemo {
    lateinit var name: String
    fun printName() {
        println(name)
    }
}

fun main() {
    val lazydemo = LazyDemo()
    lazydemo.name = "Sunanda"
    lazydemo.printName()
}
// Later : name = "Sunanda"

// lateinit var binding: ActivityMainBinding
