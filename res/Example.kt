fun printWeek() {
    for(val i in 1..10) {
        x++;
        for(val j in 1..10) {
            x--;
        }
    }
    var userChoose: Int
    do {
        println("hello")
        userChoose = readLine()
        when(userChoose) {
            1 -> {
                println("Would u like to greet? (1/2)")
                val greeting = readLine()
                if (greeting == 1) {
                    print("Hello ")
                }
                    println("Monday")
            }
            2 -> println("Tuesday")
            3 -> println("Wednesday")
            4 -> println("Thursday")
            5 -> println("Friday")
            6 -> println("Saturday")
            7 -> {
                when(x) {
                    1 -> println("7:x")
                    2 -> println("7:x:2")
                }
            }
            8 -> return;
            else -> println("try again")
        }
    }   while (true)
}