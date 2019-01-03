package us.ihmc.airly

import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AirlyServiceBuilder(apiKey: String) {
    private var httpClientBuilder = OkHttpClient.Builder()
            .setKey(apiKey)

    fun setLogger(logger: Logger = LoggerFactory.getLogger(AirlyAPI::class.java)): AirlyServiceBuilder {
        httpClientBuilder = httpClientBuilder.setLogLevel(logger)
        return this
    }

    fun setRequestLimit(permits: Int = 50, timePeriod: TimeUnit = TimeUnit.MINUTES): AirlyServiceBuilder {
        httpClientBuilder = httpClientBuilder.limitRequests(permits, timePeriod)
        return this
    }

    fun build() = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(AirlyAPI.BASE_URL)
            .client(httpClientBuilder.build())
            .build().create(AirlyAPI::class.java)
}