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
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable
import javax.imageio.ImageIO


fun main() {
    publish()
}

fun getWeek(): String {
    val cal = Calendar.getInstance()
    cal.time = Date()
    return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.NARROW_FORMAT, Locale.KOREAN)
}

fun getMyLunch(): String {
    val school = School.find(School.Region.valueOf(System.getenv("SCHOOL_REGION")), System.getenv("SCHOOL_NAME"))
    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    cal.time = Date()
    val menu = school.getMonthlyMenu(cal[Calendar.YEAR], cal[Calendar.MONTH] + 1)
    return menu[cal[Calendar.DATE] - 1].lunch.run(::removeNumbersInString).apply(::assertIsLunch)
}

fun assertIsLunch(input: String) = input.apply { if (input.length < 3) { throw Exception("No Lunch Found!") } }

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

fun publish() {
    val lunch = getMyLunch()

    val inputCode = Callable { getTOTPCode(System.getenv("OTP_SECRET")) }
    val twoFactorHandler = LoginHandler { client: IGClient, response: LoginResponse ->
        IGChallengeUtils.resolveTwoFactor(client, response, inputCode)
    }

    val client = IGClient.builder()
        .username(System.getenv("INSTARGRAM_USERNAME"))
        .password(System.getenv("INSTARGRAM_PASSWORD"))
        .onTwoFactor(twoFactorHandler)
        .login()
    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    cal.time = Date()
    val sec = (cal.timeInMillis / 1000.0).toInt()
    val timeZoneOffset = (cal.timeZone.getOffset(cal.timeInMillis) / 1000.0)
    val takenAt = client.actions().timeline().feed().first().feed_items[0].taken_at + timeZoneOffset
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
    val png = File("output/dist.png")
    AddTextToImg.execute(File("assets/image/image.png"), lunch, png)
    val jpg = File("output/dist.jpg")
    pngToJpg(png, jpg)
    val caption = SimpleDateFormat("yyyy.MM.dd(${getWeek()})").format(Date())
    client.actions()
        .timeline()
        .uploadPhoto(jpg, caption)
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

fun getTOTPCode(secretKey: String?): String? {
    val base32 = Base32()
    val bytes: ByteArray = base32.decode(secretKey)
    val hexKey: String = Hex.encodeHexString(bytes)
    return TOTP.getOTP(hexKey)
}

fun pngToJpg(png: File, jpg: File) {
    val beforeImg = ImageIO.read(png)
    val afterImg = BufferedImage(beforeImg.width, beforeImg.height, BufferedImage.TYPE_INT_RGB)
    afterImg.createGraphics().drawImage(beforeImg, 0, 0, Color.white, null)
    ImageIO.write(afterImg, "jpg", jpg)
}
