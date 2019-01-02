package us.ihmc.airly.app

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import us.ihmc.airly.AirlyServiceBuilder
import us.ihmc.airly.api.models.Area
import us.ihmc.airly.api.models.Location
import us.ihmc.airly.getMeasurementsInArea

/**
 * Created by gbenincasa on 5/15/18.
 */

fun main(args: Array<String>) = mainBody {
    val sw = Location(52.0707, 21.3606)
    val ne = Location(52.3957, 20.6885)

    ArgParser(args).parseInto(::Config).run {

        var airly = AirlyServiceBuilder(apiKey)
        if (debug || verbose) airly = airly.setVerbose()
        airly.build().getMeasurementsInArea(Area(sw, ne))
                .doOnSubscribe { print("Started") }
                .doOnTerminate { print("Terminated") }
                .subscribe({
                    for (m in it) println(m)
                }, {
                    println(it.message)
                })
                .dispose()
    }
}

