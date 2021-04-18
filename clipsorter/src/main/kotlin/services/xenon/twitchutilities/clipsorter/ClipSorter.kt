package services.xenon.twitchutilities.clipsorter

import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.google.gson.GsonBuilder
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import services.xenon.twitchutilities.clipsorter.data.ClipMeta
import java.io.File
import java.nio.file.Files
import java.text.SimpleDateFormat
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::ClipSorterArgs).let { sorterArgs ->
        val client: TwitchClient = TwitchClientBuilder.builder()
            .withClientId(sorterArgs.clientId)
            .withClientSecret(sorterArgs.authToken)
            .withEnableHelix(true)
            .build()

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val folder = sorterArgs.clipsFolder
        if (!folder.exists())
        {
            throw RuntimeException("The specified clips folder (${folder.path}) doesn't exist.")
        }

        val sortedFolder = sorterArgs.sortedFolder
        if (!sortedFolder.exists())
        {
            sortedFolder.mkdir()
        }

        val format = SimpleDateFormat("MM-dd-YYYY")

        println("Mapping clip meta...")
        val mapped = folder.listFiles()
            .filter { it.name.endsWith(".meta") }
            .map { gson.fromJson(it.reader(), ClipMeta::class.java) }

        val gameMap = mutableMapOf<Int, String>()
        val ids = mapped.map { "${it.gameId}" }
            .toSet()

        client.helix.getGames(null, ids.toList(), null).execute()
            .games.forEach { game ->
                gameMap[game.id.toInt()] = game.name
                println("Obtained ${game.id} -> ${game.name}")
            }

        val folderMap = gameMap.mapValues {
            File(sortedFolder, it.value)
        }

        println("Starting to sort ${mapped.size} clip${if (mapped.size == 1) "" else "s"}")
        println("===============================")
        val duration = measureTimeMillis {
            mapped.parallelStream()
                .forEach { meta ->
                    val gameFolder = folderMap[meta.gameId]
                    if (gameFolder != null)
                    {
                        if (!gameFolder.exists())
                        {
                            gameFolder.mkdir()
                        }

                        val originalClip = File(folder, "${meta.id}.mp4")
                        if (!originalClip.exists())
                        {
                            println("MP4 is missing for ${meta.id}")
                            return@forEach
                        }

                        val dateFolder = File(gameFolder, format.format(meta.createdAt))
                        if (!dateFolder.exists())
                        {
                            dateFolder.mkdir()
                        }

                        val targetClip = File(dateFolder, originalClip.name)
                        if (targetClip.exists())
                        {
                            println("Clip ${meta.id} has already been sorted")
                            return@forEach
                        }

                        Files.copy(originalClip.toPath(), targetClip.toPath())
                        println("Clip ${meta.id} has now been sorted!")
                    }
                }
        }

        println("===============================")
        println("Finished sorting the Twitch Clips!")
        println("It took ${duration}ms to perform.")
    }
}

class ClipSorterArgs(parser: ArgParser)
{

    val clipsFolder by parser.storing(
        "--clipsFolder",
        help = "The target folder of the downloaded clips"
    ) { File(this) }
    val sortedFolder by parser.storing("--sortedFolder", help = "The target folder for the sorted clips") { File(this) }

    val clientId by parser.storing("--clientId", help = "Twitch OAuth Client ID")
    val authToken by parser.storing("--authToken", help = "Twitch OAuth Client Secret")
}