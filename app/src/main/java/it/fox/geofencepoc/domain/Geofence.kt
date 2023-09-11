package it.fox.geofencepoc.domain

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import org.osmdroid.util.Distance
import java.util.UUID

@Entity
data class Geofence(
    @PrimaryKey var id:UUID= UUID.randomUUID(),
    var name:String?=null,
    var distance: Int=250,
    var latitude:Double=0.0,
    var longitude:Double=0.0) {


    fun computeDistance(location: Location): Float {
        val geofence = Location("geofence")
        geofence.latitude=latitude
        geofence.longitude=longitude
        return geofence.distanceTo(location)
    }

}