package hr.fika.budgeapp.common.utils

object DateTimeUtils {
    fun getDateSuffix(dayOfMonth: Int) = when (dayOfMonth) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}