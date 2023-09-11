package it.fox.geofencepoc.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.fox.geofencepoc.domain.Geofence
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface GeofenceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(geofence: Geofence)

    @Update
    suspend fun update(geofence: Geofence)

    @Delete
    suspend fun delete(geofence: Geofence)
    @Query("SELECT * FROM geofence")
    fun getGeofences(): Flow<MutableList<Geofence>>
        // To use Kotlin Symbol Processing (KSP)

    @Query("SELECT * FROM geofence WHERE id=(:id)")
    suspend fun getGeofence(id: UUID): Geofence
}