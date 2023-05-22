package io.github.bruce0203.bsmeal1nfo

import com.leeseojune.neisapi.NeisApi
import java.text.SimpleDateFormat
import java.util.*


fun getMyLunch(): String {
    val neis = NeisApi.Builder()
        .build()
    val sch = neis
        .getSchoolByName("백신고등학교").first()
    val meal = neis.getMealsByAbsoluteDay(getNowDate(), sch.scCode, sch.schoolCode)
    return meal.lunch.joinToString("\n")
}

fun getNowDate(): String = run {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    val dt1 = SimpleDateFormat("YYYYMMdd")
    dt1.format(cal.time).toString()
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
