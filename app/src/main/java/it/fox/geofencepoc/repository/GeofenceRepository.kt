package it.fox.geofencepoc.repository

import android.content.Context
import androidx.room.Room
import it.fox.geofencepoc.dao.GeofenceDatabase
import it.fox.geofencepoc.domain.Geofence
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException
import java.util.UUID

private const val DB_NAME="geofence-database"
class GeofenceRepository private constructor(context: Context){

    private val database: GeofenceDatabase = Room.databaseBuilder(context.applicationContext,GeofenceDatabase::class.java,
        DB_NAME).build()

    val allGeofences = getGeofences()

    suspend fun insert(geofence: Geofence) = database.geofenceDao().insert(geofence)

    fun getGeofences() : Flow<MutableList<Geofence>> = database.geofenceDao().getGeofences()

    suspend fun getGeofence(id: UUID): Geofence =database.geofenceDao().getGeofence(id)

    suspend fun delete(gf:Geofence)=database.geofenceDao().delete(gf)
    companion object {
        private var INSTANCE: GeofenceRepository?=null

        fun initialize(context: Context){
            if (INSTANCE==null) INSTANCE= GeofenceRepository(context)
        }

        fun get():GeofenceRepository {
            return INSTANCE?:throw IllegalStateException("Geofence Repository must be initialized")
        }
    }
}