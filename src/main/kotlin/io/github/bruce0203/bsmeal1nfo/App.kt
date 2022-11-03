package io.github.bruce0203.bsmeal1nfo

import com.github.instagram4j.instagram4j.responses.media.MediaResponse.MediaConfigureTimelineResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

var isRunCold = false

fun main(args: Array<String>) {
    isRunCold = args.getOrElse(0) { "false" }.toBoolean()
    publish()
}

fun getWeek(): String {
    val cal = Calendar.getInstance()
    cal.time = Date()
    return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.NARROW_FORMAT, Locale.KOREAN)
}

fun publish() {
    val lunch = getMyLunch()
    val client = login()
    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    cal.time = Date()
    val timeZoneOffset = (cal.timeZone.getOffset(cal.timeInMillis) / 1000.0).toLong()
    val sec = (cal.timeInMillis / 1000.0).toInt() + timeZoneOffset
    val takenAt = (client.actions.timeline().feed().maxOfOrNull { feed -> feed.feed_items.maxOfOrNull { it.taken_at } ?: 0L }?: 0) + timeZoneOffset
    val dayStart = sec - (sec % 86400)
    println("""
    ---------debugMsg---------
    --------------------------
    "takenAt: $takenAt"
    "timeZoneOffset: $timeZoneOffset"
    "dayStart: $dayStart"
    --------------------------
    """)
    if (dayStart < takenAt && !isRunCold) {
            println(
                """
                    --------------------------
                   "Skipped due to already exist!" 
                    --------------------------
                """
            )
    }
    val caption = SimpleDateFormat("yyyy.MM.dd(${getWeek()})").format(Date())
    client.actions()
        .timeline()
        .uploadPhoto(createImg(lunch), caption)
        .thenAccept { response: MediaConfigureTimelineResponse? ->
            println(
                """
                    --------------------------
                   "Successfully uploaded photo!" 
                    --------------------------
                """
            )
        }
        .join() // block current thread until complete
    exitProcess(0)
}

