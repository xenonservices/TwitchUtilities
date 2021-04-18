package services.xenon.twitchutilities.clipsorter

import com.github.twitch4j.TwitchClient

object GameDictionary
{

    private val entries = mutableMapOf<Int, String>()
    // TODO: Add a way to save the entries to a file
    // TODO: Add a way to load the entries from a file, with support for the user to change the name of the games outputted folder

    fun fetchEntries(ids: List<Int>, client: TwitchClient)
    {
        val usedIds = ids.filterNot { entries.containsKey(it) }
            .map { it.toString() }
            .toSet()

        client.helix.getGames(null, usedIds.toList(), null).execute()
            .games.forEach { game ->
                entries[game.id.toInt()] = game.name
                println("Obtained ${game.id} -> ${game.name}")
            }
    }

    fun getEntries(): Map<Int, String> = entries

}