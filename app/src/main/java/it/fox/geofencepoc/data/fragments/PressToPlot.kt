package it.fox.geofencepoc.data.fragments

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import it.fox.osmgeofencepoc.R
import org.osmdroid.api.IMapView
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay


class PressToPlot() : BaseSampleFragment(),View.OnClickListener, View.OnLongClickListener {
    var painting: ImageButton? = null
    var panning:ImageButton? = null
    var textViewCurrentLocation: TextView? = null

    var btnRotateLeft: ImageButton? = null
    var btnRotateRight:ImageButton? = null

    val df: DecimalFormat = DecimalFormat("#.000000")

    override fun getSampleTitle(): String? {
        return "Long Press to Plot Marker"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.layout_drawlines, null)
        btnRotateLeft = v.findViewById<ImageButton>(R.id.btnRotateLeft)
        btnRotateRight = v.findViewById<ImageButton>(R.id.btnRotateRight)
        (btnRotateRight as ImageButton).setOnClickListener(this)
        (btnRotateLeft as ImageButton).setOnClickListener(this)
        textViewCurrentLocation = v.findViewById<TextView>(R.id.textViewCurrentLocation)
        mMapView = v.findViewById<MapView>(R.id.mapview)
        val theMapView:MapView= mMapView as MapView;
       theMapView.setMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent): Boolean {
                Log.i(
                    IMapView.LOGTAG,
                    System.currentTimeMillis().toString() + " onScroll " + event.x + "," + event.y
                )
                //Toast.makeText(getActivity(), "onScroll", Toast.LENGTH_SHORT).show();
                updateInfo()
                return true
            }

            override fun onZoom(event: ZoomEvent): Boolean {
                Log.i(
                    IMapView.LOGTAG,
                    System.currentTimeMillis().toString() + " onZoom " + event.zoomLevel
                )
                updateInfo()
                return true
            }
        })
        val mRotationGestureOverlay = RotationGestureOverlay(mMapView)
        mRotationGestureOverlay.isEnabled = true
        theMapView.setMultiTouchControls(true)
        theMapView.getOverlayManager().add(mRotationGestureOverlay)
        theMapView.setOnLongClickListener(this)
        panning = v.findViewById<ImageButton>(R.id.enablePanning)
        (panning as ImageButton).setVisibility(View.GONE)
        painting = v.findViewById<ImageButton>(R.id.enablePainting)
        (painting as ImageButton).setVisibility(View.GONE)
        val plotter = PointPlottingOverlay(this.resources.getDrawable(R.drawable.marker_default))
        theMapView.getOverlayManager().add(plotter)
        return v
    }

    private fun updateInfo() {
        val mapCenter = mMapView!!.mapCenter
        textViewCurrentLocation?.text = (df.format(mapCenter.latitude) + "," +
                df.format(mapCenter.longitude)
                ) + ",zoom=" + mMapView!!.zoomLevelDouble + ",angle=" + mMapView!!.mapOrientation
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnRotateLeft -> {
                var angle = mMapView!!.mapOrientation + 10
                if (angle > 360) angle = 360 - angle
                mMapView!!.mapOrientation = angle
                updateInfo()
            }

            R.id.btnRotateRight -> {
                var angle = mMapView!!.mapOrientation - 10
                if (angle < 0) angle += 360f
                mMapView!!.mapOrientation = angle
                updateInfo()
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        return true
    }
}