package io.github.bruce0203.bsmeal1nfo

import kr.go.neis.api.School
import java.util.*

fun getMyLunch(): String {
    val school = School.find(School.Region.valueOf(System.getenv("SCHOOL_REGION")), System.getenv("SCHOOL_NAME"))
    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    cal.time = Date()
    val menu = school.getMonthlyMenu(cal[Calendar.YEAR], cal[Calendar.MONTH] + 1)
    return menu[cal[Calendar.DATE] - 1].lunch.run(::removeNumbersInString).apply(::assertIsLunch)
}

fun removeNumbersInString(input: String): String {
    val strBuilder = StringBuilder()
    input.split("\n")
        .map { txt -> txt.substring(0,
            0.coerceAtLeast(txt
                .indexOfFirst { Character.isDigit(it) || it == '(' }).run { if (this <= 0) txt.length else this }) }
        .forEach { strBuilder.append(it) ; strBuilder.append("\n")}
    return strBuilder.toString()
        .replace("+", "")
        .replace("-", "")
        .replace("*", "")
        .replace(";", "")
        .replace("&", "")
}

fun assertIsLunch(input: String) = input.apply { if (input.length < 3) { throw Exception("No Lunch Found!") } }
