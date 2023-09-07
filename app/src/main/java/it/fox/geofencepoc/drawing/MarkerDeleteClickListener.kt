package it.fox.geofencepoc.drawing

import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay

class MarkerDeleteClickListener : Marker.OnMarkerClickListener{
    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
        val overlays:List<Overlay> =mapView!!.overlayManager.overlays()
        var result =false
        overlays.forEach { overlay ->
            if (overlay is Marker && overlay.id.equals(marker!!.id))
                result=mapView.overlays.remove(overlay)
        };
        return  result;
    }
}