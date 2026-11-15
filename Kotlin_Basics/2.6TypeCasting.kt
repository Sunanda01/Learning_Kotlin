// 1. is → Type Check = "Is this object of this type?"
// 2. as → Unsafe Cast = Forcefully convert type.
// 3. as? → Safe Cast = "Try casting. If impossible, return null."

fun main() {
    val obj: Any = "Hello"
    if(obj is String) {
        println(obj.length)
    }

    val obj1: Any = "Hello"
    println(obj1 is String)
    val str = obj1 as String
    println(str.length)

//    val obj2: Any = 10
//    val str1 = obj2 as String   // ClassCastException
//    println(str1)

    val obj3: Any = 10
    val str2 = obj3 as? String
    println(str2)
}