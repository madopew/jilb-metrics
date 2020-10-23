fun main() {
    print(factorial(4))
    val list = bubbleSort(intArrayOf(2,15,1,8,4))
    printArray(list.toTypedArray())
    getAmountOfSymbols(1235)
    printWeek()
    val resArray : Array<Int> = getFilledArray();
    findAverageValue(resArray)
    println(taylorSin(Math.PI))
    println(compare(12,13))
    println(getSign(-12.3))
    var x = 20.0
    x = findSin(x) + findCos(x)
    print(x)
}

// Developed by Vlad Maiski and Bakyt Madi

fun getAbs(a : Double) : Double {
    if (a < 0)
        return -a
    else
        return a
}

/*
Some comments which can help u to understand the program
And also some code here to try confuse the metric's code
fun isOp() {
    if ( a & 1 ) {
        println("нечетно")
    } else {
        println("четно")
}
 */

fun getSign(a : Double) : Int {
    if (a > 0)
        return 1
    else
        return -1
}


fun resolveFunction(a : Double) : Double {
    val eps : Double = 0.00001
    val x : Double
    var y : Double
    var det : Double
    var n : Int = 3
    x = a
    y = a
    det = a
    do {
        det = -det * x * x / ((n + 1)*n)
        n++
        y += det
    } while (getAbs(det) > eps)
    return y
}

fun findExp(a : Double) : Double {
    val eps : Double = 0.00001
    var x : Double
    var y : Double
    var det : Double
    var n : Int = 1
    x = a
    y = 1.0
    det = 1.0
    do {
        det = det * x / n
        n++
        y += det
    } while (getAbs(det) > eps)
    return y
}

fun printWeek() {
    var userChoose: Int
    do {
        println("""
            1 - Monday
            2 - Tuesday 
            3 - Wednesday 
            4 - Thursday 
            5 - Friday 
            6 - Saturday 
            7 - Sunday 
            8 - to exit program
        """.trimIndent())
        userChoose = readLine().toString().toInt()
        when(userChoose) {
            1 -> println("Monday")
            2 -> println("Tuesday")
            3 -> println("Wednesday")
            4 -> println("Thursday")
            5 -> println("Friday")
            6 -> println("Saturday")
            7 -> println("Sunday")
            8 -> return;
            else -> println("try again")
        }
    }while (true)
}

fun calcuate(a : Double) : Double {
    val eps : Double = 0.00001
    var x : Double
    var y : Double
    var det : Double
    var n : Int = 3
    x = a
    y = a
    det = a
    do {
        det = -det * x * x / ((n + 1)*n)
        n++
        y += det
    } while (getAbs(det) > eps)
    return y
}

fun compare(a : Int, b : Int) : Int {
    if (a < b) {
        return b
    } else
        return a
}

fun findSin(a : Double) : Double {
    val eps : Double = 0.00001
    var x : Double
    var y : Double
    var vs : Double
    var n : Int = 2
    x = a
    y = x
    vs = x
    do {
        vs = - vs * x * x / ((2 * n - 1)*(2 * n - 2))
        n++
        y += vs
    } while (getAbs(vs) > eps)
    return y % (2 * 3.1415926)
}

fun findCos(a : Double) : Double {
    val eps : Double = 0.00001
    var x : Double
    var y : Double
    var vs : Double
    var n : Int = 2
    x = a
    y = 1.0
    vs = 1.0
    do {
        vs = - vs * x * x / ((n - 1)*(n))
        n += 2
        y += vs
    } while (getAbs(vs) > eps)
    return y % (2 * 3.1415926)
}

fun getFilledArray() : Array<Int> {
    println("Enter amount of elements")
    val amountOfElmnts : Int = readLine().toString().toInt()
    val resArray = IntArray(amountOfElmnts)
    for (i in 0 until amountOfElmnts) {
        println("Enter element number " + (i + 1))
        resArray[i] = readLine().toString().toInt()
    }
    return resArray.toTypedArray()
}


fun findAverageValue(array : Array<Int>) {
    var sum : Int = 0
    for(i in array.indices) {
        print(array[i].toString() + " ")
        sum += array[i]
    }
    println("\n" +/*output sum/size!*/ sum.toDouble() / array.size)
}

fun printArray(array : Array<Int>) {
    for (i in array.indices)
        print(" " + array[i])
    println()
}

fun getAmountOfSymbols(_value : Int) {
    var value = _value
    var amount = 0
    while (value != 0) {
        value /= 10
        amount++
    }
    println(amount)
}


//Bubble Sort
fun bubbleSort(arr:IntArray):IntArray{
    var swap = true
    while(swap){
        swap = false
        for(i in 0 until arr.size-1){
            if(arr[i] > arr[i+1]){
                val temp = arr[i]
                arr[i] = arr[i+1]
                arr[i + 1] = temp

                swap = true
            }
        }
    }
    return arr
}

fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result *= i
    }
    return result
}

fun taylorSin(x: Double): Double {
    var sumNeu: Double
    var sumOld: Double
    var sum: Double
    var i = 1
    sumNeu = x
    sum = sumNeu // This should calculating the first term Value
    do  //the loop do will calculating the Tailor Series
    {
        sumOld = sumNeu
        i++
        sum = +sum * x * x / i
        i++
        sum /= i
        sumNeu = sumOld + sum
    } while (sumNeu != sumOld)
    return sumNeu
}

fun madness() {
    for(i in 1..10)
        for(j in 1..10)
            for(k in 1..10)
                for(l in 1..10)
                    for(m in 1..10)
                        for(n in 1..10) {
                            val num = i + j + k + l + m + n
                            if(num < 50) {
                                when(num) {
                                    10 -> println("yes it's 10")
                                    20 -> println("i'm almost 20 too!")
                                    else -> println("most common answer!")
                                }
                            } else if(num == 50)
                                println("unlikely to happen")
                            else if(num == 60)
                                println("also unliely")
                        }
    println("how much time did it take?????")
}