package us.ihmc.airly.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import us.ihmc.airly.api.models.SensorInfoWithPollutionLevel;

import java.util.List;


/**
 * Created by gbenincasa on 5/15/18.
 */
public interface AirlyAPI {

    String BASE_URL = "https://airapi.airly.eu/";

    @GET("v1/sensor/measurements")
    Observable<SensorInfoWithPollutionLevel> observableSensor(@Query("sensorId") String sensorId);

    @GET("v1/sensors/current")
    Observable<List<SensorInfoWithPollutionLevel>> observableSensorsInArea(
            @Query("southwestLat") float southwestLat, @Query("southwestLong") float southwestLong,
            @Query("northeastLat") float northeastLat, @Query("northeastLong") float northeastLong,
            @Query("apikey") String apikey);

    @GET("v1/sensors/current")
    Call<List<SensorInfoWithPollutionLevel>> sensorsInArea(
            @Query("southwestLat") float southwestLat, @Query("southwestLong") float southwestLong,
            @Query("northeastLat") float northeastLat, @Query("northeastLong") float northeastLong,
            @Query("apikey") String apikey);
}
