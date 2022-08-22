package io.github.bruce0203.bsmeal1nfo

import kr.go.neis.api.School
import java.io.File
import java.util.*


fun main() {
    AddTextToImg.execute(File("assets/image.png"), getMyLunch(), File("output/dist.png"))
    println(getMyLunch())
}

fun getMyLunch(): String {
    val school = School.find(School.Region.GYEONGGI, "백신고등학교")
    val cal = Calendar.getInstance()
    cal.time = Date()
    val menu = school.getMonthlyMenu(cal[Calendar.YEAR], cal[Calendar.MONTH] + 1)
    return menu[cal[Calendar.DATE] - 1].lunch.run(::removeNumbersInString)
}

fun removeNumbersInString(input: String): String {
    val strBuilder = StringBuilder()
    input.split("\n")
        .map { txt -> txt.substring(0,
            0.coerceAtLeast(txt
                .indexOfFirst { Character.isDigit(it) || it == '(' }).run { if (this <= 0) txt.length else this }) }
        .forEach { strBuilder.append(it) ; strBuilder.append("\n")}
    return strBuilder.toString()
}