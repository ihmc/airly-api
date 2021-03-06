package us.ihmc.airly

/**
 * Created by gbenincasa on 5/15/18.
 */

data class Address(var country: String = "", var city: String,
                   var street: String = "", var number: String)

data class Location(var latitude: Double, var longitude: Double)

data class Area(val sw: Location, val ne: Location)

data class Sponsor(var name: String, var description: String, var logo: String, var link: String)

data class Installation(var id: Int, var location: Location, var address: Address,
                        var elevation: Double, var sponsor: Sponsor, var airly: Boolean)

data class Value(var name: String, var value: Double)

data class SingleMeasurement(var fromDateTime: String, var tillDateTime: String, var values: List<Value>, var indexes: List<Index>, var standards: List<Standard>)

data class Index(var name: String, var value: Double, var level: String,
                 var description: String, var advice: String, var color: String)

data class Standard(var name: String, var pollutant: String, var limit: Double, var percent: Double)

data class Measurement(var current: SingleMeasurement, var history: List<SingleMeasurement>, var forecast: List<SingleMeasurement>)

data class LocatedMeasurement(val measurement: Measurement, val installation: Installation)

