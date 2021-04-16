package services.xenon.twitchutilities.clipsorter

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::ClipSorterArgs).let { sorterArgs ->
        println("Hello from Twitch ClipSorter :)")
    }
}

class ClipSorterArgs(parser: ArgParser)
{

}