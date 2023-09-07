package it.fox.geofencepoc.api

import it.fox.geofencepoc.domain.RegisteredLocation
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisteredLocationApi {

    @POST("/locations")
    suspend fun addLocations(@Body values:List<RegisteredLocation>)
}