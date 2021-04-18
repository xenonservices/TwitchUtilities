package services.xenon.twitchutilities.counter.thread

import services.xenon.twitchutilities.counter.storage.CounterStorage

class CounterThread(
    val storage: CounterStorage
): Thread()
{

    override fun run()
    {
        while(true)
        {
            storage.getCounters().forEach { it.write() }
            sleep(500)
        }
    }

}