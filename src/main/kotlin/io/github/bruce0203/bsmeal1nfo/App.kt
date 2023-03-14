package io.github.bruce0203.bsmeal1nfo

import java.util.*
import kotlin.system.exitProcess

var isRunCold = false

fun main(args: Array<String>) {
    isRunCold = args.getOrElse(0) { "false" }.toBoolean()
    publish()
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
    client.actions()
        .story()
        .uploadPhoto(createImg(lunch))
        .thenAccept {
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

