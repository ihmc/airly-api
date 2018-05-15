package us.ihmc.airly.app

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import us.ihmc.airly.api.AirlyRxService
import us.ihmc.airly.api.models.Area
import us.ihmc.airly.api.models.Coordinates
import us.ihmc.airly.api.models.SensorInfoWithPollutionLevel

/**
 * Created by gbenincasa on 5/15/18.
 */

internal class DbgCallback : Callback<List<SensorInfoWithPollutionLevel>> {

    override fun onResponse(call: Call<List<SensorInfoWithPollutionLevel>>,
                            response: Response<List<SensorInfoWithPollutionLevel>>) {
        for (size in response.body()!!) {
            println(size.toString())
        }
    }

    override fun onFailure(call: Call<List<SensorInfoWithPollutionLevel>>, t: Throwable) {
        t.printStackTrace()
    }
}

internal fun getLoggingInterceptor(verbose: Boolean): OkHttpClient {
    var level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
    if (verbose) {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = level
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
}


class AirlyRxApp(cfg: Config) {
    private val dbg: Boolean
    private val svc: AirlyRxService

    init {
        this.svc = AirlyRxService(cfg.apiKey,
                getLoggingInterceptor(cfg.debug || cfg.verbose))
        this.dbg = cfg.debug
    }

    internal fun execute(area: Area) {
        if (dbg) {
            svc.forEachCurrentMeasurementsInArea(area, DbgCallback())
        } else {
            svc.observableCurrentMeasurements(area).subscribe { sensor -> println(sensor.toString()) }
        }
    }

}

fun main(args: Array<String>) = mainBody {
    val sw = Coordinates(52.0707.toFloat(), 21.3606.toFloat())
    val ne = Coordinates(52.3957.toFloat(), 20.6885.toFloat())

    ArgParser(args).parseInto(::Config).run {
        AirlyRxApp(this).execute(Area(sw, ne))
    }
}
