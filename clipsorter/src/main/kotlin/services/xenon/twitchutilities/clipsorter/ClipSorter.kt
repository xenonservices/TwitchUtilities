package services.xenon.twitchutilities.clipsorter

import com.google.gson.GsonBuilder
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import services.xenon.twitchutilities.clipsorter.data.ClipMeta
import java.io.File
import java.nio.file.Files
import java.text.SimpleDateFormat

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::ClipSorterArgs).let { sorterArgs ->
        println("Hello from Twitch ClipSorter :)")

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val folder = File("V:\\Rec\\Twitch Clips\\Test\\Clips")
        val sortedFolder = File("V:\\Rec\\Twitch Clips\\Test\\Sorted")
        if (!sortedFolder.exists())
        {
            sortedFolder.mkdir()
        }

        val gameMap = mapOf( // TODO: Replace with Twitch API?
            491487 to "Dead by Daylight",
            27471 to "Minecraft"
        )

        val folderMap = gameMap.mapValues {
            File(sortedFolder, it.value)
        }

        val format = SimpleDateFormat("MM-dd-YYYY")

        folder.listFiles()
            .filter { it.name.endsWith(".meta") }
            .map { gson.fromJson(it.reader(), ClipMeta::class.java) }
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
                    if(!dateFolder.exists()) {
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
}

class ClipSorterArgs(parser: ArgParser)
{

}