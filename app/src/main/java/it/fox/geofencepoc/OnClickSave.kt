package it.fox.geofencepoc

import android.view.View
import android.view.View.OnClickListener
import it.fox.geofencepoc.domain.Geofence
import it.fox.geofencepoc.drawing.PT_PREFIX
import it.fox.geofencepoc.viewmodel.GeofenceViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.UUID

class OnClickSave(private val mapView: MapView, private val viewModel:GeofenceViewModel) : OnClickListener {

    override fun onClick(v: View?) {
        mapView
        .overlays.forEach { overlay ->
            if (overlay is Marker && overlay.id.startsWith(PT_PREFIX)) {
                val marker: Marker = overlay
                val point: GeoPoint = marker.position
                val lat: Double = point.latitude
                val lon: Double = point.longitude
                val geofence= Geofence(UUID.randomUUID(),"aname",lat,lon)
                viewModel.insert(geofence)
                mapView.overlayManager.remove(overlay)
                mapView.invalidate()
            }
        }
    }

}