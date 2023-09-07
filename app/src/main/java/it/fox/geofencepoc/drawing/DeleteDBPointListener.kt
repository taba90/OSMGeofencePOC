package it.fox.geofencepoc.drawing

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import it.fox.geofencepoc.viewmodel.GeofenceViewModel
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.UUID

class DeleteDBPointListener (mapView: MapView, val viewModel: GeofenceViewModel, val viewLifecycleOwner: LifecycleOwner): Marker.OnMarkerClickListener {
    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
        if (mapView?.context != null && marker?.id !=null) {
            val alert: AlertDialog.Builder = AlertDialog.Builder(mapView.context)
            alert.setTitle("Rimuovi Geofence center")
            alert.setMessage("Sei sicuro di voler eliminare la località?")
            alert.setPositiveButton(
                "Yes"
            ) { dialog, _ ->
                viewModel.getGeofence(UUID.fromString(marker.id)).observe(viewLifecycleOwner) { gf ->
                    viewModel.delete(gf).invokeOnCompletion {
                        mapView.overlays.remove(marker)
                        mapView.invalidate()
                    }
                    dialog.dismiss()
                }
            }
            alert.setNegativeButton(
                "No"
            ) { dialog, _ -> // close dialog
                dialog.dismiss()
            }
            alert.show()
        }
        return true
    }
}