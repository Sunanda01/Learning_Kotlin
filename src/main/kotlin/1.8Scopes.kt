//Scopes are used to: reduce repetitive code and work on an object inside a block
fun main(){
    // 1. Let : used when objects may be null
    var name: String? = "Sunanda"
    name?.let {
        println(it.length)
    }
    // if name is null:
    // a. Kotlin skips the block safely
    // b. no crash happens
    // c. no execution happens

    // 2. Apply : used for Object configuration/setup
    class User{
        var fname=""
        var age=0
    }
    val user = User().apply {
        fname = "Sunanda Sadhu"
        age = 24
    }
    println("Name = ${user.fname} \nAge = ${user.age}")
    // VERY common in:
    // a. ViewBinding
    // b. RecyclerView
    // c. object initialization

    // 3. Also : Used for Extra side work/logging
    val numbers = mutableListOf(1,2,3)
        .also {
            println("List created")
        }
    println(numbers)

    // 4. Run : Used when Need object + final result
    // a. Object context
    // b. Multiple operations
    // c. Final computed result
    val result = "Hello".run {
        println(this)           // this = "Hello"
        length * 2              // multiple operations possible
    }                           // LAST line becomes return value
    println(result)

    // 5. with : used for Work on object multiple times
    with(binding) {
        text.text = "Hello"
        button.isEnabled = true
    }
    // "Inside this block, treat binding as current object"

    // Without with
    // binding.text.text = "Hello"
    // binding.button.isEnabled = true
    // binding.image.alpha = 0.5f
    // repeated binding

    // let              null handling
    // apply            object setup
    // also             side effects/logging
    // run              compute result
    // with             work on object repeatedly
}