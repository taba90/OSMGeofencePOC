package it.fox.geofencepoc

import android.app.Application
import it.fox.geofencepoc.repository.GeofenceRepository
import it.fox.geofencepoc.repository.UserDataRepository


class GeofenceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GeofenceRepository.initialize(this)
        UserDataRepository.initialize(this)
    }
}