package guilherme.nogueira.domain.utils

import guilherme.nogueira.network.utils.dateFormatter
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime() : LocalDateTime {
    val formatter = dateFormatter
    return LocalDateTime.parse(this, formatter)
}

fun LocalDateTime.formatForDisplay(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    return this.format(formatter)
}

fun LocalDateTime.timeUntil(): String {
    val now = LocalDateTime.now()

    return if (this.isBefore(now)) {
        "Ended"
    } else {
        val duration = Duration.between(now, this)
        when {
            duration.toDays() > 0 -> {
                val days = duration.toDays()
                val hours = duration.toHours() % 24
                "${days}d ${hours}h"
            }
            duration.toHours() > 0 -> "${duration.toHours()}h"
            else -> "Soon"
        }
    }
}