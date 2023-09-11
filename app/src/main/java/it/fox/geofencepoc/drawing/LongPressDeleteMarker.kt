package it.fox.geofencepoc.drawing

import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asFlow
import it.fox.geofencepoc.viewmodel.GeofenceViewModel
import it.fox.osmgeofencepoc.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.UUID


class LongPressDeleteMarker(
    mapView: MapView,
    val viewModel: GeofenceViewModel,
    val viewLifecycleOwner: LifecycleOwner
) : Marker(mapView) {

    override fun onLongPress(event: MotionEvent?, mapView: MapView?): Boolean {
        if (mapView?.context != null) {
            val alert: AlertDialog.Builder = AlertDialog.Builder(mapView.context)
            alert.setTitle("Rimuovi Geofence center")
            alert.setMessage("Sei sicuro di voler eliminare la località?")
            alert.setPositiveButton(
                R.string.yes
            ) { dialog, _ ->
                viewModel.getGeofence(UUID.fromString(id)).observe(viewLifecycleOwner) { gf ->
                    viewModel.delete(gf).invokeOnCompletion {
                        mapView.invalidate()
                    }
                    dialog.dismiss()
                }
            }
            alert.setNegativeButton(
                R.string.no
            ) { dialog, _ -> // close dialog
                dialog.dismiss()
            }
            alert.show()
        }
        return true
    }
}