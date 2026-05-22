interface AnimalInterface {
    fun sound()
}

class Cat : AnimalInterface{
    override fun sound() {
        println("Cat Sound")
    }
}

fun main(){
    val cat = Cat()
    cat.sound()
}

//Interfaces are used heavily in Android.
//Especially for:
// a. click listeners
// b. callbacks
// c. adapters
// d. communication between fragments