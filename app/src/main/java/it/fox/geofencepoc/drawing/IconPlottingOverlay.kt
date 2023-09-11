package it.fox.geofencepoc.drawing

import android.graphics.drawable.Drawable
import android.view.MotionEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.PointReducer
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import java.util.UUID


const val PT_PREFIX:String="usr_added";

class IconPlottingOverlay(m: Drawable): Overlay() {

    private val markerIcon: Drawable=m

    override fun onLongPress(e:MotionEvent, mapView: MapView): Boolean {
        if (markerIcon != null) {
            val pt = mapView.projection.fromPixels(e.x.toInt(), e.y.toInt(), null) as GeoPoint
            if (pt.longitude < -180) pt.longitude = pt.longitude + 360
            if (pt.longitude > 180) pt.longitude = pt.longitude - 360
            val m = Marker(mapView)
            m.id= PT_PREFIX + (UUID.randomUUID().toString())
            m.position = pt
            m.icon = markerIcon
            m.image = markerIcon
            m.setOnMarkerClickListener(MarkerDeleteClickListener())
            mapView.overlayManager.removeIf { ov: Overlay -> ov is Marker && ov.id.startsWith(PT_PREFIX) }
            mapView.overlayManager.add(m)
            mapView.invalidate()
            return true
        }
        return false

    }
}