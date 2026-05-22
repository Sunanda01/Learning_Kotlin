class User {
    companion object {
        const val ROLE = "ADMIN"
    }
}
fun main(){
    println(User.ROLE)
}

// Companion Object : Kotlin version of Java static
// Used for : constants and utility methods