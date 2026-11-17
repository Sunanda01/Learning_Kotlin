# Kotlin
- Interoperability with **Java**
- Concise & Readable Syntax
- Versatile Language
- Kotlin Multiplatform
- BE Development

### 1. Read Input
```kotlin
val input = readln()
println("Entered value is $input")
```

---

### 2. Nullability and Null-Safe Operators in Kotlin

Kotlin provides **Null Safety** to prevent `NullPointerException (NPE)` at runtime.

By default, variables cannot hold `null`. To allow null values, use `?` after the data type.

### 2.1. Non-Nullable Variables
Cannot store null values.

```kotlin
var name: String = "Kotlin"
name = null  // Compilation error
```

### 2.2. Nullable Variables (`?`)
Can store either a value or null.

```kotlin
var name: String? = "Kotlin"
name = null  // Valid
```

### 2.3. Null-Safe Operators
### Safe Call Operator (`?.`)
- Accesses properties or methods only if the value is not null.
- Returns null if the variable is null.

```kotlin
val name: String? = null
println(name?.length)  // null
```

### Elvis Operator (`?:`)
- Provides a default value when the expression on the left is null.

```kotlin
val name: String? = null
val length = name?.length ?: 0

println(length)  // 0
```

### Not-Null Assertion Operator (`!!`)
- Forces Kotlin to treat a nullable variable as non-null.
- Throws `NullPointerException` if the value is null.

```kotlin
val name: String? = null
println(name!!.length)  // NullPointerException
```

**Avoid using `!!` unless absolutely necessary.**

### Safe Cast Operator (`as?`)
- Safely converts an object to another type.
- Returns null if the conversion fails instead of throwing `ClassCastException`.

```kotlin
val value: Any = "Kotlin"
val result = value as? String
```

---

### 3. when Expression in Kotlin
The when expression is used to check multiple conditions. It is similar to switch in Java.

```kotlin
fun main() {
    val day = readln().toIntOrNull()

    when (day) {
      null -> println("INVALID INPUT!!!!!!!!!")
        1 -> println("Monday")
        2 -> println("Tuesday")
        3 -> println("Wednesday")
        4 -> println("Thursday")
        5 -> println("Friday")
        in 10..20 -> println("Between 10 - 20")
        else -> println("Invalid day")
    }
}
```

---

### 4. Exception Handling
```kotlin
fun main() {
    try {
        val result = 10 / 5
        println(result)
    } catch (e: ArithmeticException) {
        println("Cannot divide by zero")
    } catch (e: Exception) {
        println("Something went wrong")
    } finally {
        println("Execution completed")
    }
}
```

---

### 5. Arrays

```kotlin
fun main() {
    print("Enter array size: ")
    val n:Int = readln().toIntOrNull() ?: 0

    val arr = IntArray(n)

    for (i in 0 until n) {
        print("Enter element: ")
        arr[i] = readln().toIntOrNull() ?: 0
    }

    println(arr) // returns [I@41629346
    println(arr.contentToString())
}
```

---

### 6. Loops
### 6.1. For Loop
#### a. Using .. (Inclusive range)
Includes both start and end values.

```kotlin
for (i in 1..5) {
    println(i)
}
```
#### b. Using until (Exclusive range)
Excludes the end value.

```kotlin
for (i in 1 until 5) {
    println(i)
}
```

#### c. Using downTo (Reverse loop)
Iterates in descending order.

```kotlin
for (i in 5 downTo 1) {
    println(i)
}
```

#### d. Using step (Custom increment)
Specifies the increment/decrement value.

```kotlin
for (i in 1..10 step 2) {
    println(i)
}
```

#### e. Iterating over an array

```kotlin
val arr = arrayOf(10, 20, 30, 40)
for (i in arr) {
    println(i)
}
```

### 6.2. While Loop
### 6.3. Do-While Loop
### 6.4. Loop Control Statements

```kotlin
for (i in 1..5) {
    if (i == 3) break
    println(i)
}
```

```kotlin
for (i in 1..5) {
    if (i == 3) continue
    println(i)
}
```

---

### 7. Function Declaration and Function Call

### 7.1. Function with Parameters
```kotlin
fun add(a: Int, b: Int) {
    println(a + b)
}

fun main() {
    add(10, 20)
}
```

### 7.2. Function with Return Type
```kotlin
fun add(a: Int, b: Int) {
    return a+b
}

fun main() {
    val sum = add(10, 20)
    println(sum)
}
```

### 7.3. Single Expression Function
```kotlin
fun multiply(a: Int, b: Int): Int = a * b

fun main() {
    println(multiply(5, 4))
}
```

### 7.4. Extension Function + Qualified
```kotlin
fun main() {
    println("Enter a string:")
    val input = readln()
    
    val reversed = input.myReversed()
    println(reversed)

    if (input == reversed) {
        println("That's a palindrome!")
    } else {
        println("Not a palindrome!")
    }
}

fun String.myReversed(): String {
    val finalString = buildString {
        for (i in this@myReversed.lastIndex downTo 0) {
            append(this@myReversed[i])
        }
    }
    return finalString
}
```

---

### 8. Lambda
### 8.1. Lambda with Parameters
``` kotlin
fun main() {
    val add = { a: Int, b: Int -> a + b }

    println(add(10, 20))
}
```

### 8.2. Lambda with Return Type
``` kotlin
fun main() {
    val multiply: (Int, Int) -> Int = { a, b ->
        a * b
    }

    println(multiply(5, 4))
}
```

### 8.3. Lambda with it
When a lambda has only one parameter, Kotlin provides an implicit parameter called it.
``` kotlin
fun main() {
    val square: (Int) -> Int = { it * it }

    println(square(5))
}
```

---

### 9. Normal Class vs Data Class
### 9.1. Normal Class
A normal class is used to define objects with properties and behaviours (functions).

```kotlin
class Student(val name: String, val age: Int) {

    fun display() {
        println("Name: $name")
        println("Age: $age")
    }
}

fun main() {
    val s1 = Student("Sunanda", 24)
    s1.display()
}
```

**Note**
- Used to represent objects.
- Can contain properties, constructors and functions.
- Does not automatically generate toString(), equals() or hashCode() based on properties.

### 9.2. Data Class
A data class is mainly used to store data.
Use the data keyword before the class declaration.

``` kotlin
data class Student(val name: String, val age: Int)

fun main() {
    val s1 = Student("Sunanda", 24)
    val s2 = Student("Sunanda", 24)
    val s3 = s2.copy(age=25)

    println(s1)
    println(s1 == s2)
    println(s3)
    println(s2 == s3)
}
```

**Note** :
Kotlin automatically generates the following functions for data classes:
- toString() – Provides a readable string representation.
- equals() – Compares objects based on properties.
- hashCode() – Generates a hash code based on properties.
- copy() – Creates a copy of an object.
- componentN() – Allows destructuring declarations.

**Rules for Data Classes**
1. Must have at least one parameter in the primary constructor.
2. Primary constructor parameters must be declared using val or var.
3. Cannot be abstract, open, sealed or inner.
4. Only properties declared in the primary constructor participate in generated equals(), hashCode(), toString() and copy().

---

### 10. Interface
Defines a contract that specifies properties and functions that a class must implement.

``` kotlin
interface Camera {
    fun clickPhoto()
}

interface MusicPlayer {
    fun playMusic()
}

class Smartphone : Camera, MusicPlayer {

    override fun clickPhoto() {
        println("Photo clicked")
    }

    override fun playMusic() {
        println("Playing music")
    }
}

fun main() {
    val phone = Smartphone()

    phone.clickPhoto()
    phone.playMusic()
}
```

---

### 11. Abstract Class
Class that cannot be instantiated directly. It is used as a base class for other classes.

**It can contain:**
- Abstract properties and functions (without implementation).
- Normal properties and functions (with implementation).
- Constructors.

``` kotlin
abstract class Vehicle(val brand: String) {

    abstract fun start()

    fun displayBrand() {
        println("Brand: $brand")
    }
}

class Car(brand: String) : Vehicle(brand) {

    override fun start() {
        println("$brand car started")
    }
}

fun main() {
    val car = Car("BMW")

    car.displayBrand()
    car.start()
}
```
---

### 12. Sealed Class and Sealed Interface
Used to represent a restricted set of types. It controls **which classes can inherit or implement it**. Useful when we want to define a fixed set of possible states or outcomes.

``` kotlin
sealed interface Payment

data class Cash(val amount: Int) : Payment
data class Card(val amount: Int) : Payment
data class UPI(val amount: Int) : Payment

fun processPayment(payment: Payment) {
    when (payment) {
        is Cash -> println("Cash: ${payment.amount}")
        is Card -> println("Card: ${payment.amount}")
        is UPI -> println("UPI: ${payment.amount}")
    }
}

fun main() {
    processPayment(UPI(500))
}
```

---

### 13. Singleton in Kotlin
A Singleton is a design pattern that ensures only one instance of a class exists throughout the application.
``` kotlin
object Counter {

    var count = 0

    fun increment() {
        count++
        println(count)
    }
}

fun main() {
    Counter.increment()
    Counter.increment()
    Counter.increment()
}

```

---