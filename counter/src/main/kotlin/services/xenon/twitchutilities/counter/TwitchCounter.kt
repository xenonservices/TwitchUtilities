package services.xenon.twitchutilities.counter

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::TwitchCounterArgs).let { twitchArgs ->
        val credential = OAuth2Credential("twitch", twitchArgs.authToken)

        val client: TwitchClient = TwitchClientBuilder.builder()
            .withDefaultAuthToken(credential)
            .withEnablePubSub(true)
            .build()

        client.pubSub.listenForChannelPointsRedemptionEvents(null, "83711921")

        client.eventManager.onEvent(RewardRedeemedEvent::class.java) {
            println("Redeem event: $it")
        }

        println("Hello from Twitch Counter :)")
    }
}

class TwitchCounterArgs(parser: ArgParser)
{

    val authToken by parser.storing("--authToken", help = "Twitch OAuth Token")
    val channelId by parser.storing("--channelId", help = "Twitch Channel Id")

}