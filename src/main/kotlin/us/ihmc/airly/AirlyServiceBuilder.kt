package us.ihmc.airly

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AirlyServiceBuilder(apiKey: String) {
    private var httpClientBuilder = OkHttpClient.Builder()
            .setKey(apiKey)

    fun setVerbose(): AirlyServiceBuilder {
        httpClientBuilder = httpClientBuilder.setLogLevel(true)
        return this
    }

    fun build() = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(AirlyAPI.BASE_URL)
            .client(httpClientBuilder.build())
            .build().create(AirlyAPI::class.java)
}