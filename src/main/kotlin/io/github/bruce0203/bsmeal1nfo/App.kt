package io.github.bruce0203.bsmeal1nfo

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.responses.media.MediaResponse.MediaConfigureTimelineResponse
import kr.go.neis.api.School
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


fun main() {
    val dist = File("output/dist.png")
    AddTextToImg.execute(File("assets/image.png"), getMyLunch(), dist)
    val caption = SimpleDateFormat("yyyy.MM.dd(${getWeek()})").format(Date())
    publish(dist, caption)
}

fun getWeek(): String {
    val cal = Calendar.getInstance()
    cal.time = Date()
    println(cal[Calendar.DAY_OF_WEEK])
    return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.NARROW_FORMAT, Locale.KOREAN)
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

fun publish(dist: File, caption: String) {
    val client = IGClient.builder()
        .username(System.getenv("INSTARGRAM_USERNAME"))
        .password(System.getenv("INSTARGRAM_PASSWORD"))
        .login()
    client.actions()
        .timeline()
        .uploadPhoto(dist, caption)
        .thenAccept { response: MediaConfigureTimelineResponse? ->
            println("Successfully uploaded photo!")
            println(response)
        }
        .join() // block current thread until complete
}