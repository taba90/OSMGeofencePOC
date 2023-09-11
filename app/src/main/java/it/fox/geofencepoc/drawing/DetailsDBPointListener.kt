package it.fox.geofencepoc.drawing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import it.fox.geofencepoc.viewmodel.GeofenceViewModel
import it.fox.osmgeofencepoc.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import java.util.UUID

class DetailsDBPointListener (val viewModel: GeofenceViewModel, val viewLifecycleOwner: LifecycleOwner): Polygon.OnClickListener {
    override fun onClick(polygon: Polygon, mapView: MapView?,eventPos:GeoPoint): Boolean {
        if (mapView?.context != null && polygon.id !=null) {
            viewModel.getGeofence(UUID.fromString(polygon.id)).observe(viewLifecycleOwner) { gf ->
                val inflater=mapView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout: View =inflater.inflate(R.layout.details_geofence,null)
                val editName: TextView =layout.findViewById(R.id.name_value)
                val editRadius: TextView =layout.findViewById(R.id.radius_value)
                editName.setText(gf.name,TextView.BufferType.NORMAL)
                editRadius.setText(gf.distance.toString(),TextView.BufferType.NORMAL)
                val alert: AlertDialog.Builder = AlertDialog.Builder(mapView.context)
                alert.setTitle(R.string.geofence_fields_title)
                alert.setView(layout)
                alert.setPositiveButton(
                    R.string.ok
                ) { dialog, _ -> // close dialog
                    dialog.dismiss()
                }
                alert.setNegativeButton(
                    R.string.delete_gf
                ) { dialog, _ ->
                        viewModel.delete(gf).invokeOnCompletion {
                            mapView.overlays.remove(polygon)
                            mapView.invalidate()
                        }
                        dialog.dismiss()
                }
                alert.show()
            }
        }
        return true
    }
}