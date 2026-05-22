package oop
fun main() {
    val rect1 = Rectangle(10f,20f)
    println("Area = ${rect1.area}")

    val point1 = Point(10f,20f)
    val point2 = Point(10f,20f)

    println(point1 == point2)
}

class Rectangle(val width: Float, val height: Float){
    val area = width * height
}

data class Point(val x: Float, val y: Float)