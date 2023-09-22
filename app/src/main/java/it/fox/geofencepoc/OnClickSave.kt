package it.fox.geofencepoc

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import it.fox.geofencepoc.domain.Geofence
import it.fox.geofencepoc.drawing.PT_PREFIX
import it.fox.geofencepoc.viewmodel.GeofenceViewModel
import it.fox.geofencepoc.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.UUID

class OnClickSave(private val mapView: MapView, private val viewModel:GeofenceViewModel) : OnClickListener {

    override fun onClick(v: View?) {
        mapView
        .overlays.forEach { overlay ->
            if (overlay is Marker && overlay.id.startsWith(PT_PREFIX)) {
                val inflater=mapView.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout:View=inflater.inflate(R.layout.form_geofence_dialog,null)
                val editName:EditText=layout.findViewById(R.id.name_input)
                val editRadius:EditText=layout.findViewById(R.id.radius_input)
                val alert: AlertDialog.Builder = AlertDialog.Builder(mapView.context)
                alert.setView(layout)
                alert.setTitle(R.string.geofence_fields_title)
                alert.setMessage(R.string.geofence_fields_msg)
                alert.setPositiveButton(
                        R.string.submit
                        ) { dialog, _ ->
                    if (isNotEmpty(editName) && isNotEmpty(editRadius)) {
                        val marker: Marker = overlay
                        val point: GeoPoint = marker.position
                        val lat: Double = point.latitude
                        val lon: Double = point.longitude
                        val geofence = Geofence(UUID.randomUUID(), editName.text.toString(),Integer.valueOf(editRadius.text.toString()), lat, lon)
                        viewModel.insert(geofence)
                        mapView.overlayManager.remove(overlay)
                        mapView.invalidate()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(mapView.context,R.string.values_mandatory,Toast.LENGTH_SHORT).show()
                    }
                }
                alert.setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                alert.show()
            }
        }
    }

    private fun isNotEmpty(text:EditText): Boolean{
        return text.text!=null && text.text.isNotEmpty()
    }

}