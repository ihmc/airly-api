package us.ihmc.airly.app

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import io.reactivex.Flowable
import us.ihmc.airly.AirlyClientBuilder
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

        var airly = AirlyClientBuilder(apiKey)
        if (debug || verbose) airly = airly.setLogger()
        airly.setRequestLimit().build().getMeasurementsInArea(Area(sw, ne))
                .flatMap { Flowable.fromIterable(it) }
                .doOnSubscribe { print("Started") }
                .doOnTerminate { print("Terminated") }
                .subscribe({ println(it) }, { println(it.message) })
                .dispose()
    }
}

