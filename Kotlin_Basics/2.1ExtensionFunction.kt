fun main() {
    val email1 = "abc@gmail.com"
    val email2 = "sunanda123"
    println(email1.isEmail())
    println(email2.isEmail())
}

// Extension Function : used heavily for: validation, formatting, utility helpers
// Add a new function to String class, So Now Every String Gets This Function
fun String.isEmail(): Boolean {
    return this.contains("@")
}