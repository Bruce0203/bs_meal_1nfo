package io.github.bruce0203.bsmeal1nfo

import kr.go.neis.api.School
import java.util.*


fun main() {
    val school = School.find(School.Region.GYEONGGI, "백신고등학교")
    val cal = Calendar.getInstance()
    cal.time = Date()
    println(cal)
    val menu = school.getMonthlyMenu(cal[Calendar.YEAR], cal[Calendar.MONTH])
    println(menu[cal[Calendar.DATE] - 1].lunch)

}
