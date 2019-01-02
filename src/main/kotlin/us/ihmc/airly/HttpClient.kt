package us.ihmc.airly

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun OkHttpClient.Builder.setLogLevel(verbose: Boolean): OkHttpClient.Builder {
    var level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
    if (verbose) {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = level
    return this.addInterceptor(interceptor)
}

fun OkHttpClient.Builder.setKey(apiKey: String): OkHttpClient.Builder {
    return this.addInterceptor {
        val originalRequest = it.request()
        val builder = originalRequest.newBuilder().header("apikey", apiKey)
        val newRequest = builder.build()
        it.proceed(newRequest)
    }
}

fun OkHttpClient.Builder.setLanguage(language: String="en"): OkHttpClient.Builder {
    return this.addInterceptor {
        val originalRequest = it.request()
        val builder = originalRequest.newBuilder().header("Accept-Language", language)
        val newRequest = builder.build()
        it.proceed(newRequest)
    }
}
