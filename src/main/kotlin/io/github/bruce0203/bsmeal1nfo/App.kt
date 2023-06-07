package io.github.bruce0203.bsmeal1nfo

import kotlin.system.exitProcess

fun main(args: Array<String>) {
    publish()
}

fun publish() {
    val lunch = getMyLunch()
    val client = login()
    client.actions()
        .story()
        .uploadPhoto(createImg(lunch))
        .thenAccept {
            println(
                """
                    --------------------------
                   "Successfully uploaded photo!" 
                    --------------------------
                """.trimIndent()
            )
        }
        .join() // block current thread until complete
    exitProcess(0)
}

