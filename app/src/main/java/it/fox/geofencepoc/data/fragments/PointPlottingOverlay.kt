package it.fox.geofencepoc.data.fragments

import android.graphics.drawable.Drawable
import android.view.MotionEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay


class PointPlottingOverlay(icon:Drawable) : Overlay(){


    val markerIcon:Drawable = icon;


    override fun onLongPress(e: MotionEvent, mapView: MapView): Boolean {
        if (markerIcon != null) {
            val pt = mapView.projection.fromPixels(e.x.toInt(), e.y.toInt(), null) as GeoPoint
            /*
             * <b>Note</b></b: when plotting a point off the map, the conversion from
             * screen coordinates to map coordinates will return values that are invalid from a latitude,longitude
             * perspective. Sometimes this is a wanted behavior and sometimes it isn't. We are leaving it up to you,
             * the developer using osmdroid to decide on what is right for your application. See
             * <a href="https://github.com/osmdroid/osmdroid/pull/722">https://github.com/osmdroid/osmdroid/pull/722</a>
             * for more information and the discussion associated with this.
             */

            //just in case the point is off the map, let's fix the coordinates
            if (pt.longitude < -180) pt.longitude = pt.longitude + 360
            if (pt.longitude > 180) pt.longitude = pt.longitude - 360
            //latitude is a bit harder. see https://en.wikipedia.org/wiki/Mercator_projection
            if (pt.latitude > MapView.getTileSystem().maxLatitude) pt.latitude =
                MapView.getTileSystem().maxLatitude
            if (pt.latitude < MapView.getTileSystem().minLatitude) pt.latitude =
                MapView.getTileSystem().minLatitude
            val m = Marker(mapView)
            m.position = pt
            m.icon = markerIcon
            m.image = markerIcon
            m.title = "A demo title"
            m.subDescription = """
            A demo sub description
            ${pt.latitude},${pt.longitude}
            """.trimIndent()
            m.snippet = "a snippet of information"
            mapView.overlayManager.add(m)
            mapView.invalidate()
            return true
        }
        return false
    }
}