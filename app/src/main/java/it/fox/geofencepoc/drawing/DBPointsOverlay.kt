package it.fox.geofencepoc.drawing

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.lifecycle.LifecycleOwner
import it.fox.geofencepoc.viewmodel.GeofenceViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay

class DBPointsOverlay(val marker: Drawable, val mapView: MapView, val viewModel: GeofenceViewModel, val viewLifecycleOwner: LifecycleOwner) : Overlay() {


    override fun onResume() {
        super.onResume()
        viewModel.allGeofenencesCenters.observe(viewLifecycleOwner) { geoflist ->
            for (gf in geoflist) {
                mapView.overlays.removeIf { ov: Overlay ->
                    ov is Marker && ov.id.equals(gf.id.toString())
                }
                val geoPoint = GeoPoint(gf.latitude,gf.longitude)
                val m = Marker(mapView)
                m.id = gf.id.toString()
                m.position = geoPoint
                m.icon = marker
                m.image = marker
                m.setOnMarkerClickListener(DeleteDBPointListener(mapView,viewModel, viewLifecycleOwner))
                if (!mapView.overlayManager.contains(m)){
                    mapView.overlayManager.add(m)
                }
            }
            mapView.invalidate()
        }
    }


}