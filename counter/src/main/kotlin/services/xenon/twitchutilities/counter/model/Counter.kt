package services.xenon.twitchutilities.counter.model

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.time.Instant

class Counter(
    val name: String,
    val targetFile: File,
    val redemptions: List<RedemptionData>
)
{

    var endTime: Instant = Instant.now()

    fun hasEnded(): Boolean = Instant.now().isAfter(endTime)

    fun addTime(additionalTime: Long)
    {
        endTime = if (hasEnded())
        {
            Instant.now().plusMillis(additionalTime)
        } else
        {
            endTime.plusMillis(additionalTime)
        }
    }

    fun write()
    {
        BufferedWriter(FileWriter(targetFile)).use {
            if (hasEnded())
            {
                it.write("Countdown ended")
            } else
            {
                val differenceMs = endTime.minusMillis(Instant.now().toEpochMilli()).toEpochMilli()
                val seconds = differenceMs / 1000L
                val minutes = seconds / 60
                val hours = minutes / 60

                val secondsFormatted = (seconds % 60).format()
                val minutesFormatted = (minutes % 60).format()
                val hoursFormatted = (hours % 60).format()

                var formatted = ""
                val hoursDisplayed = hoursFormatted.isNotEmpty() && hoursFormatted != "0"
                if (hoursDisplayed)
                {
                    formatted += "$hoursFormatted:"
                }
                if (hoursDisplayed || minutesFormatted.isNotEmpty() && minutesFormatted != "00")
                {
                    formatted += "$minutesFormatted:"
                }
                formatted += secondsFormatted

                it.write("$name: $formatted")
            }
        }
    }

    private fun Long.format(): String
    {
        return if (this <= 9)
        {
            "0$this"
        } else
        {
            "$this"
        }
    }

}