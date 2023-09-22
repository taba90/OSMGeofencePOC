package it.fox.geofencepoc.api

import com.google.gson.GsonBuilder
import it.fox.geofencepoc.domain.RegisteredLocation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RegisteredLocationApi {

    companion object{
        val DATE_CONVERTER= GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()
    }
    @POST("/locations")
    suspend fun addLocations(@Body values:List<RegisteredLocation>,@Header(value="X-KEY-IV") keys:String?): Response<Any>
}