package io.github.bruce0203.bsmeal1nfo

import com.github.instagram4j.instagram4j.responses.media.MediaResponse.MediaConfigureTimelineResponse
import java.text.SimpleDateFormat
import java.util.*


fun main() {
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
    val sec = (cal.timeInMillis / 1000.0).toInt()
    val timeZoneOffset = (cal.timeZone.getOffset(cal.timeInMillis) / 1000.0)
    val takenAt = client.actions().timeline().feed().firstOrNull()?.feed_items?.get(0)?.taken_at?: 0L
    val dayStart = sec - (sec % 86400) + timeZoneOffset
    if (dayStart < takenAt) {
        println(
            """
                    --------------------------
                   "Skipped due to already exist!" 
                    --------------------------
                """
        )
        return
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
}

