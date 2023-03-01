package io.github.bruce0203.bsmeal1nfo

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils
import de.taimos.totp.TOTP
import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Hex
import java.util.concurrent.Callable

fun getTOTPCode(secretKey: String?): String? {
    val base32 = Base32()
    val bytes: ByteArray = base32.decode(secretKey)
    val hexKey: String = Hex.encodeHexString(bytes)
    return TOTP.getOTP(hexKey)
}

fun login(): IGClient {
    val inputCode = Callable { getTOTPCode(System.getenv("OTP_SECRET")) }
    val client = IGClient.builder()
        .username(System.getenv("INSTARGRAM_USERNAME"))
        .password(System.getenv("INSTARGRAM_PASSWORD"))
        .onTwoFactor { client, response -> IGChallengeUtils.resolveTwoFactor(client, response, inputCode) }
        .onChallenge { client, response -> IGChallengeUtils.resolveChallenge(client, response, inputCode) }
        .login()
    return client
}