package us.ihmc.airly.api;

import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import us.ihmc.airly.api.models.Area;
import us.ihmc.airly.api.models.SensorInfoWithPollutionLevel;

import java.util.List;


/**
 * Created by gbenincasa on 5/15/18.
 */
public class AirlyRxService {

    private final AirlyAPI airlyApi;
    private final String apiKey;

    public AirlyRxService(String apiKey) {
        this(apiKey, null);
    }

    public AirlyRxService(String apiKey, OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(AirlyAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        if (client != null) {
            builder = builder.client(client);
        }

        this.airlyApi = builder.build().create(AirlyAPI.class);
        this.apiKey = apiKey;
    }

    public void forEachCurrentMeasurementsInArea(Area area, Callback<List<SensorInfoWithPollutionLevel>> cback) {
        airlyApi.sensorsInArea(
                area.getSw().getLatitude(), area.getSw().getLongitude(),
                area.getNe().getLatitude(), area.getNe().getLongitude(),
                apiKey
        ).enqueue(cback);
    }

    public Flowable<SensorInfoWithPollutionLevel> observableCurrentMeasurements(Area area) {
        return observableCurrentMeasurementsList(area).flatMap(Flowable::fromIterable);
    }

    private Flowable<List<SensorInfoWithPollutionLevel>> observableCurrentMeasurementsList(Area area) {
        return airlyApi.observableSensorsInArea(
                area.getSw().getLatitude(), area.getSw().getLongitude(),
                area.getNe().getLatitude(), area.getNe().getLongitude(),
                apiKey
        );
    }
}
