# Airly API
[Airly](https://airly.eu/en/) Unofficial Java API wrapper for
[API Documentation](https://developer.airly.eu/docs)

## Docs

Airly API is compatible with Airly's API v2.

### Donwload

With Gradle:
```
dependencies {
    compile 'com.github.ihmc:airly-api:v0.4-alpha'
}
```

### Client
You can create an Airly client using the `AirlyClientBuilder`:
```
AirlyAPI airly = new AirlyServiceBuilder(apiKey);
```
This will create an instance of `AirlyAPI`:
```
public interface AirlyAPI {

    // Installations

    Flowable<Installation> getInstallation(int installationId);

    Flowable<List<Installation>> getNearestInstallations(double latitude,
                                                         double longitude,
                                                         double maxDistanceKm,
                                                         int maxResults);

    // Measurements

    Flowable<Measurement> getMeasurements(int installationId);

    Flowable<Measurement> getNearestMeasurements(double latitude,
                                                 double longitude,
                                                 double maxDistanceKm);

    Flowable<Measurement> getInterpolatedMeasurements(double latitude,
                                                      double longitude);
}
```
`AirlyAPIExtensions` provides methods to retrieve installations and measurements in an area described by the South-West and North-East corners of a rectangular bounding box. These may come in handy to users of Airly API v1.

### Rate Limit
Airly API access is rate-limited. Default rate limits per apikey are 1000 API requests per day and 50 API requests per minute.
AirlyServiceBuilder does not enforce any limit by default, it can be set as such:
```
AirlyAPI airly = new AirlyClientBuilder(apiKey)
    .setRequestLimit(50, TimeUnit.MINUTES); // Enforce the 50 API requests per minute limit
```
### Snippet
Look at [AirlyRxApp.kt](https://github.com/ihmc/airly-api/blob/master/src/main/kotlin/us/ihmc/airly/AirlyRxApp.kt)
