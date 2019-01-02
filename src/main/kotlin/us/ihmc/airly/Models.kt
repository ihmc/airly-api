package us.ihmc.airly.api.models

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

data class Current(var fromDateTime: String, var tilDateTime: String, var values: List<Value>)

data class Index(var name: String, var value: Double, var level: String,
                 var description: String, var advice: String, var color: String)

data class Standard(var name: String, var pollutant: String, var limit: Double, var percent: Double)

data class Measurement(var current: Current, var indexes: List<Index>, var standards: List<Standard>)

data class LocatedMeasurement(val measurement: Measurement, val installation: Installation)

