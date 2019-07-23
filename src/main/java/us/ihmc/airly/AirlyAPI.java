package us.ihmc.airly;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

/**
 * Created by gbenincasa on 5/15/18.
 */
public interface AirlyAPI {

    String BASE_URL = "https://airapi.airly.eu/";

    int UNLIMITED_RESULTS = -1;

    // Installations

    @GET("v2/installations/{installationId}")
    Flowable<Installation> getInstallation(@Path("installationId") int installationId);

    @GET("v2/installations/nearest")
    Flowable<List<Installation>> getNearestInstallations(@Query("lat") double latitude,
                                                         @Query("lng") double longitude,
                                                         @Query("maxDistanceKM") double maxDistanceKm,
                                                         @Query("maxResults") int maxResults);

    // Measurements

    @GET("v2/measurements/installation")
    Flowable<Measurement> getMeasurements(@Query("installationId") int installationId);

    @GET("v2/measurements/nearest")
    Flowable<Measurement> getNearestMeasurements(@Query("lat") double latitude,
                                                 @Query("lng") double longitude,
                                                 @Query("maxDistanceKM") double maxDistanceKm);

    @GET("v2/measurements/point")
    Flowable<Measurement> getInterpolatedMeasurements(@Query("lat") double latitude,
                                                      @Query("lng") double longitude);
}
