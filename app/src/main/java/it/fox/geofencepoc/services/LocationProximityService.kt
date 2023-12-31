package it.fox.geofencepoc.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import it.fox.geofencepoc.api.RegisteredLocationApi
import it.fox.geofencepoc.domain.Geofence
import it.fox.geofencepoc.domain.RegisteredLocation
import it.fox.geofencepoc.repository.GeofenceRepository
import it.fox.geofencepoc.utils.Utils
import it.fox.geofencepoc.R
import it.fox.geofencepoc.UniqueDeviceId
import it.fox.geofencepoc.domain.UserData
import it.fox.geofencepoc.repository.UserDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64
import java.util.Date

class LocationProximityService: Service() {

    private val repository: GeofenceRepository = GeofenceRepository.get()

    private val userRepository:UserDataRepository=UserDataRepository.get()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var listener:LocationListener

    private lateinit var locationManager: LocationManager;

    companion object {
        var isRunning:Boolean=false
    }

    override fun onCreate() {
        super.onCreate()

        if (Utils.checkPermission(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),applicationContext)) {
            locationManager =
                applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 0F, LocationProximityListener() );
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning=true
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // no binding
        return null
    }

    private fun getUrl():String{
        var value:String= applicationContext.resources.getString(R.string.portal_endpoint)
        if (!value.endsWith("/")) value += "/"
        return value
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning=false
        scope.cancel()
        locationManager.removeUpdates(listener)
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

   inner  class LocationProximityListener: LocationListener {
        override fun onLocationChanged(location: Location) {
            if (location != null) {
                val flow: Flow<MutableList<Geofence>> = repository.allGeofences
                val flowud: Flow<MutableList<UserData>> = userRepository.allUserData
                val payload:MutableList<RegisteredLocation> = mutableListOf()
                scope.launch {
                    val list = flow.first()
                    var userdatalist=flowud.first()
                    var userData:UserData?=null
                    if (!userdatalist.isEmpty()) {
                        userData = userdatalist.get(0)
                    }
                    for (gf in list) {
                        val dist: Float = gf.computeDistance(location)
                        if (dist < gf.distance) {
                            payload.add(RegisteredLocation(0L,dist,location.latitude,location.longitude,gf.latitude,gf.longitude,UniqueDeviceId.getUniqueId(),Date()))
                        }
                    }
                    if (payload.isNotEmpty()) {
                        val client: RegisteredLocationApi =
                            Retrofit.Builder().baseUrl(getUrl()).addConverterFactory(
                                GsonConverterFactory.create(RegisteredLocationApi.DATE_CONVERTER)
                            ).build().create(RegisteredLocationApi::class.java)
                        try {
                            var header: String? = null
                            if (userData != null) {
                                val userData = userdatalist.get(0)
                                header = userData.iv + "$" + userData.privateKey
                            }
                            val resp: Response<Any> = client.addLocations(payload, header)
                            resp.code()
                        } catch (ex: Exception) {

                        }
                    }
                }
            }
        }

    }
}