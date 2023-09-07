package it.fox.geofencepoc

import android.app.Application
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import it.fox.geofencepoc.repository.GeofenceRepository
import it.fox.geofencepoc.workers.ProximityDetectorWorker
import java.time.Duration
import java.time.Period
import java.util.concurrent.TimeUnit

class GeofenceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GeofenceRepository.initialize(this)
        val manager =WorkManager.getInstance(this)
        val minutes:Long=15
        manager.enqueue(PeriodicWorkRequest.Builder(ProximityDetectorWorker::class.java,minutes,TimeUnit.MINUTES).build())
    }
}