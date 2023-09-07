package it.fox.geofencepoc

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import it.fox.geofencepoc.domain.Geofence
import it.fox.geofencepoc.drawing.DBPointsOverlay
import it.fox.geofencepoc.drawing.IconPlottingOverlay
import it.fox.geofencepoc.drawing.PT_PREFIX
import it.fox.geofencepoc.repository.GeofenceRepository
import it.fox.geofencepoc.viewmodel.GeofenceViewModel
import it.fox.osmgeofencepoc.R
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import java.util.UUID


class MapFragment : Fragment(), View.OnLongClickListener{

    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    private lateinit var locationManager: LocationManager
    private lateinit var save:Button

    private lateinit var viewModel: GeofenceViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel= GeofenceViewModel()
        locationManager= activity?.getSystemService(Activity.LOCATION_SERVICE) as LocationManager;
        val view: View = inflater.inflate(R.layout.map_layout,container,false)
        mapView = view.findViewById(R.id.mapView)
        save=view.findViewById(R.id.savebutton)
        save.bringToFront()
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.visibility=VISIBLE
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller
        mapController.setZoom(15.0)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            var location: Location?=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location==null) location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                mapController.setCenter(GeoPoint(location.latitude,location.longitude))
            }
        }

        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView.setOnLongClickListener(this)


        val plotter = IconPlottingOverlay(this.resources.getDrawable(R.drawable.redpoint,null))
        mapView.overlayManager.add(plotter)
        val dbPlotter=DBPointsOverlay(this.resources.getDrawable(R.drawable.bluepoint,null),mapView,viewModel,viewLifecycleOwner)
        mapView.overlayManager.add(dbPlotter)
        save.setOnClickListener(
            OnClickSave(mapView,viewModel)
        )

    }



    override fun onLongClick(v: View?): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume();
    }



    override fun onDetach() {
        super.onDetach()
        mapView.onDetach()
    }

}