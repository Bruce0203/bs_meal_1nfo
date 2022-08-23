package io.github.bruce0203.bsmeal1nfo

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.IGClient.Builder.LoginHandler
import com.github.instagram4j.instagram4j.responses.accounts.LoginResponse
import com.github.instagram4j.instagram4j.responses.media.MediaResponse.MediaConfigureTimelineResponse
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils
import de.taimos.totp.TOTP
import kr.go.neis.api.School
import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Hex
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable


fun main() {
    val dist = File("output/dist.jpg")
//    AddTextToImg.execute(File("assets/image.png"), getMyLunch(), dist)
    val caption = SimpleDateFormat("yyyy.MM.dd(${getWeek()})").format(Date())
    publish(dist, caption)
}

fun getWeek(): String {
    val cal = Calendar.getInstance()
    cal.time = Date()
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

    val inputCode = Callable { getTOTPCode(System.getenv("OTP_SECRET")) }
    val twoFactorHandler = LoginHandler { client: IGClient, response: LoginResponse ->
        IGChallengeUtils.resolveTwoFactor(client, response, inputCode)
    }

    val client = IGClient.builder()
        .username(System.getenv("INSTARGRAM_USERNAME"))
        .password(System.getenv("INSTARGRAM_PASSWORD"))
        .onTwoFactor(twoFactorHandler)
        .login()
    client.actions()
        .timeline()
        .uploadPhoto(dist, caption)
        .thenAccept { response: MediaConfigureTimelineResponse? ->
            println(
                "Successfully uploaded photo!"
            )
        }
        .join() // block current thread until complete

}

fun getTOTPCode(secretKey: String?): String? {
    val base32 = Base32()
    val bytes: ByteArray = base32.decode(secretKey)
    val hexKey: String = Hex.encodeHexString(bytes)
    return TOTP.getOTP(hexKey)
}
