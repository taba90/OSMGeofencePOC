package it.fox.geofencepoc.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.fox.geofencepoc.domain.Geofence
import it.fox.geofencepoc.domain.RegisteredLocation
import it.fox.geofencepoc.domain.UserData

@Database(entities=[Geofence::class,RegisteredLocation::class, UserData::class], version = 1)
@TypeConverters(Converters::class)
abstract class GeofenceDatabase : RoomDatabase() {

    abstract fun geofenceDao(): GeofenceDao

    abstract fun userdataDao(): UserDataDao
}