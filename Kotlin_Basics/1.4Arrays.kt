fun main() {
    try{
        val arr= intArrayOf(1,2,3,4,5)
        println("Size is ${arr.size}")
        println(arr) // return [I@72ea2f77 = some random string
        println(arr.contentToString())
        println(arr[4])

        // to print 0 to n
        for(i in 0  until arr.size-1){
            println(arr[i])
        }

        // to print n to 0
        for(i in arr.size-1 downTo 0){
            println(arr[i])
        }

        for(num in arr){
            println(num)
        }
    }catch(e: ArrayIndexOutOfBoundsException){
        println("Index out of bounds")
    }
}