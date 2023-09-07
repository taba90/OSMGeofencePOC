package it.fox.geofencepoc.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import it.fox.geofencepoc.domain.Geofence
import it.fox.geofencepoc.domain.RegisteredLocation

@Database(entities=[Geofence::class,RegisteredLocation::class], version = 1)
abstract class GeofenceDatabase : RoomDatabase() {

    abstract fun geofenceDao(): GeofenceDao
}