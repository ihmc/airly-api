package us.ihmc.airly

import com.xenomachina.argparser.ArgParser

/**
 * Created by gbenincasa on 5/15/18.
 */
class Config(parser: ArgParser) {

    companion object {
        fun parse(args: Array<String>) = ArgParser(args).parseInto(::Config).run {  }
    }

    val verbose by parser.flagging("-v", "--verbose", help = "enable verbose mode")

    val debug by parser.flagging("-d", "--debug", help = "run in debug mode")

    val apiKey by parser.storing("-k", "--api-key", help = "secret key to access the API")
}