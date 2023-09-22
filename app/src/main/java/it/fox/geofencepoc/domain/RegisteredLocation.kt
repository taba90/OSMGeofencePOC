package it.fox.geofencepoc.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.fox.geofencepoc.UniqueDeviceId
import java.util.Date

@Entity
class RegisteredLocation(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var distance: Float = 0F,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var workPlaceLat:Double=0.0,
    var workPlaceLon:Double=0.0,
    var deviceId: String?=null,
    var regTimestamp: Date= Date()
) {
}