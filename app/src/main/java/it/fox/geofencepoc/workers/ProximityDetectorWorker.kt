package it.fox.geofencepoc.workers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.asLiveData
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import it.fox.geofencepoc.api.RegisteredLocationApi
import it.fox.geofencepoc.domain.Geofence
import it.fox.geofencepoc.domain.RegisteredLocation
import it.fox.geofencepoc.repository.GeofenceRepository
import it.fox.geofencepoc.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.osmdroid.util.GeoPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.RuntimeException
import java.util.Date

class ProximityDetectorWorker(private val ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

    private val repository: GeofenceRepository = GeofenceRepository.get()

    override suspend fun doWork(): Result {
        val locationManager: LocationManager =
            ctx.getSystemService(LOCATION_SERVICE) as LocationManager
        var deviceId: String?= null
        var location: Location? = null

        if (Utils.checkPermission(
                arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),ctx)) {
                location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) location =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null) {
            val flow: Flow<MutableList<Geofence>> = repository.allGeofences
            val payload:MutableList<RegisteredLocation> = mutableListOf()
            runBlocking(Dispatchers.IO){
                val list = flow.first()
                for (gf in list) {
                    val dist: Float = gf.computeDistance(location)
                    if (dist < gf.distance) {
                        payload.add(RegisteredLocation(1L,dist,location.latitude,location.longitude,gf.latitude,gf.longitude))
                    }
                }
            }

            val client:RegisteredLocationApi = Retrofit.Builder().baseUrl("http://192.168.1.13:8082/").addConverterFactory(GsonConverterFactory.create()).build().create(RegisteredLocationApi::class.java)
            client.addLocations(payload)
        }
        return Result.success();
    }
}