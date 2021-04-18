package services.xenon.twitchutilities.counter

import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import services.xenon.twitchutilities.counter.storage.CounterStorage
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.reflect.KClass

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::TwitchCounterArgs).let { twitchArgs ->
        val client: TwitchClient = TwitchClientBuilder.builder()
            .withClientId(twitchArgs.clientId)
            .withClientSecret(twitchArgs.authToken)
            .withEnablePubSub(true)
            .build()

        client.pubSub.listenForChannelPointsRedemptionEvents(null, twitchArgs.channelId)

        val storage = CounterStorage()

        client.eventManager.onEvent(RewardRedeemedEvent::class.java) {
            var flagged = false
            storage.getCounters()
                .forEach { counter ->
                    counter.redemptions
                        .filter { data -> data.redemptionTitle == it.redemption.reward.title }
                        .forEach { data ->
                            counter.addTime(data.additionalTime)
                            println("${it.redemption.user.displayName} redeemed ${it.redemption.reward.title}")
                            flagged = true
                        }
                }

            if (!flagged)
            {
                println("Redeem event: $it")
            }
        }

        println("Hello from Twitch Counter :)")
    }
}

class TwitchCounterArgs(parser: ArgParser)
{

    val clientId by parser.storing("--clientId", help = "Twitch OAuth Client ID")
    val authToken by parser.storing("--authToken", help = "Twitch OAuth Client Secret")
    val channelId by parser.storing("--channelId", help = "Twitch Channel Id")

}