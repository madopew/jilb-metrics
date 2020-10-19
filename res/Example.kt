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
            1 -> {
                println("Would u like to greet? (1/2)")
                val greeting = readLine().toString().toInt()
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
            7 -> println("Sunday")
            8 -> return;
            else -> println("try again")
        }
    }   while (true)
}