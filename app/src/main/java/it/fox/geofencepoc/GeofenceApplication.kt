package it.fox.geofencepoc

import android.app.Application
import it.fox.geofencepoc.repository.GeofenceRepository


class GeofenceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GeofenceRepository.initialize(this)
    }
}