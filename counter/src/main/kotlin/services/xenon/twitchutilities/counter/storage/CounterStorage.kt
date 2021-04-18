package services.xenon.twitchutilities.counter.storage

import services.xenon.twitchutilities.counter.model.Counter
import services.xenon.twitchutilities.counter.model.RedemptionData
import services.xenon.twitchutilities.counter.thread.CounterThread
import java.io.File


class CounterStorage
{

    private val counters = mutableListOf<Counter>()

    init
    {
        CounterThread(this).start()

        registerCounter(Counter("test", File("test.txt"), listOf(
            RedemptionData("Nothing", 300_000L)
        ))).addTime(4255598)
    }

    fun registerCounter(counter: Counter): Counter
    {
        counters += counter
        return counter
    }

    fun getCounters(): List<Counter> = counters

}