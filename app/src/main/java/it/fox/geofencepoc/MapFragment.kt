package it.fox.geofencepoc

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.fox.osmgeofencepoc.R
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
@SuppressLint("MissingPermission")
class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    private lateinit var locationManager: LocationManager;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationManager= activity?.getSystemService(Activity.LOCATION_SERVICE) as LocationManager;
        val view: View = inflater.inflate(R.layout.map_layout,null)
        mapView = view.findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller
        mapController.setZoom(15.0)
        var location: Location?=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location==null) location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            mapController.setCenter(GeoPoint(location.latitude,location.longitude))
        }
        return mapView;
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume();
    }



    override fun onDetach() {
        super.onDetach()
        mapView.onDetach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}