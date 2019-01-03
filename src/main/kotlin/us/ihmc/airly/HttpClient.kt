package us.ihmc.airly

import devcsrj.okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import java.util.concurrent.ScheduledExecutorService
import kotlin.system.measureTimeMillis


internal class RateLimiter private constructor(private val maxPermits: Int, private val timePeriod: TimeUnit) {
    private val semaphore: Semaphore = Semaphore(maxPermits)
    private var scheduler: ScheduledExecutorService? = null

    fun acquire() = semaphore.acquire()

    fun stop() = scheduler!!.shutdownNow()

    fun schedulePermitReplenishment() {
        scheduler = Executors.newScheduledThreadPool(1)
        scheduler!!.scheduleWithFixedDelay({
            semaphore.release(maxPermits - semaphore.availablePermits())
        }, 0, 1, timePeriod)
    }

    companion object {

        fun create(permits: Int, timePeriod: TimeUnit): RateLimiter {
            val limiter = RateLimiter(permits, timePeriod)
            limiter.schedulePermitReplenishment()
            return limiter
        }
    }
}

internal class RateLimiterInterceptor(permits: Int, timePeriod: TimeUnit) : Interceptor
{
    private val logger = LoggerFactory.getLogger(RateLimiterInterceptor::class.java)
    private val limiter = RateLimiter.create(permits, timePeriod)

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val time = measureTimeMillis{
            limiter.acquire()
        }

        val log = if (time > 10) {
            msg:String -> logger.info(msg)
        } else {
            msg:String -> logger.debug(msg)
        }
        log("Acquiring permission to issue HTTP request took $time ms")

        return chain.proceed(originalRequest)
    }
}

fun OkHttpClient.Builder.setLogLevel(logger: Logger): OkHttpClient.Builder {
    /*var level: HttpLoggingInterceptor.Level = level
    if (verbose) {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = level
    return this.addInterceptor(interceptor)*/

    return this.addInterceptor(HttpLoggingInterceptor(logger))
}

fun OkHttpClient.Builder.setKey(apiKey: String): OkHttpClient.Builder {
    return this.addInterceptor {
        val originalRequest = it.request()
        val builder = originalRequest.newBuilder().header("apikey", apiKey)
        val newRequest = builder.build()
        it.proceed(newRequest)
    }
}

fun OkHttpClient.Builder.limitRequests(permits: Int, timePeriod: TimeUnit): OkHttpClient.Builder {
    return this.addInterceptor(RateLimiterInterceptor(permits, timePeriod))
}

fun OkHttpClient.Builder.setLanguage(language: String="en"): OkHttpClient.Builder {
    return this.addInterceptor {
        val originalRequest = it.request()
        val builder = originalRequest.newBuilder().header("Accept-Language", language)
        val newRequest = builder.build()
        it.proceed(newRequest)
    }
}
