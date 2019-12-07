package us.ihmc.airly

import com.google.gson.GsonBuilder
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody

/**
 * Created by gbenincasa on 5/15/18.
 */

fun main(args: Array<String>) = mainBody {
    val sw = Location(50.200035, 19.264862)
    val ne = Location(50.208027, 19.285269)

    ArgParser(args).parseInto(::Config).run {

        var airly = AirlyClientBuilder(apiKey)
        if (debug || verbose) airly = airly.setLogger()
        airly.setRequestLimit().build().getMeasurementInArea(Area(sw, ne))
                .doOnSubscribe { print("Started") }
                .doOnTerminate { print("Terminated") }
                .subscribe({ println(GsonBuilder().setPrettyPrinting().create().toJson(it)) }, { println(it.message) })
                .dispose()
    }
}

