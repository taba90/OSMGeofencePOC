package it.fox.geofencepoc.drawing

import android.graphics.Color
import androidx.lifecycle.LifecycleOwner
import it.fox.geofencepoc.viewmodel.GeofenceViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polygon

class DBPointsOverlay(val mapView: MapView, val viewModel: GeofenceViewModel, val viewLifecycleOwner: LifecycleOwner) : Overlay() {


    override fun onResume() {
        super.onResume()
        viewModel.allGeofenencesCenters.observe(viewLifecycleOwner) { geoflist ->
            for (gf in geoflist) {
                mapView.overlays.removeIf { ov: Overlay ->
                    (ov is Polygon && ov.id.equals(gf.id.toString()))
                }
                val oPolygon = Polygon(mapView);
                val dist=gf.distance
                val circlePoints: MutableList<GeoPoint> = mutableListOf()
                var f=0F
                while (f <360){
                    circlePoints.add(GeoPoint(gf.latitude , gf.longitude).destinationPoint(dist.toDouble(), f.toDouble()))
                    f+=1
                }
                oPolygon.points = circlePoints
                oPolygon.outlinePaint.color= Color.TRANSPARENT
                oPolygon.outlinePaint.strokeWidth=0f
                oPolygon.fillPaint.color=0x7F00FF00
                oPolygon.setOnClickListener(DetailsDBPointListener(viewModel, viewLifecycleOwner))
                oPolygon.id=gf.id.toString()
                mapView.overlays.add(oPolygon)
            }
            mapView.invalidate()
        }
    }


}