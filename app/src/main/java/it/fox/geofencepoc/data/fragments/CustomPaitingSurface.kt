package it.fox.geofencepoc.data.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import org.osmdroid.library.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow
import org.osmdroid.views.overlay.milestones.MilestoneBitmapDisplayer
import org.osmdroid.views.overlay.milestones.MilestoneManager
import org.osmdroid.views.overlay.milestones.MilestonePathDisplayer
import org.osmdroid.views.overlay.milestones.MilestonePixelDistanceLister


class CustomPaitingSurface(context:Context, attrs: AttributeSet?): View(context, attrs) {

    fun setMode(mode: Mode) {
        drawingMode = mode
    }

    private var drawingMode = Mode.Polyline

    enum class Mode {
        Polyline, Polygon, PolygonHole, PolylineAsPath
    }
    protected var withArrows = false
    private var mCanvas: Canvas? = null
    private var mPath: Path? = null
    private var map: MapView? = null
    private val pts: MutableList<Point> = ArrayList()
    private var mPaint: Paint? = null
    private var mX = 0f
    private  var mY:Float = 0f
    private val TOUCH_TOLERANCE = 4f

    @Transient
    var lastPolygon: Polygon? = null


    protected override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(bitmap)
    }


    protected override fun onDraw(canvas: Canvas) {
        canvas.drawPath(mPath!!, mPaint!!)
    }

    fun init(mapView: MapView?) {
        map = mapView
    }

    private fun touch_start(x: Float, y: Float) {
        mPath?.reset()
        mPath?.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touch_move(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy: Float = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath?.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touch_up() {
        mPath?.lineTo(mX, mY)
        // commit the path to our offscreen
        mCanvas!!.drawPath(mPath!!, mPaint!!)
        // kill this so we don't double draw
        mPath?.reset()
        if (map != null) {
            val projection = map!!.projection
            val geoPoints = ArrayList<GeoPoint>()
            val unrotatedPoint = Point()
            for (i in pts.indices) {
                projection.unrotateAndScalePoint(pts[i].x, pts[i].y, unrotatedPoint)
                val iGeoPoint =
                    projection.fromPixels(unrotatedPoint.x, unrotatedPoint.y) as GeoPoint
                geoPoints.add(iGeoPoint)
            }
            if (geoPoints.size > 2) {
                //only plot a line unless there's at least one item
                when (drawingMode) {
                    Mode.Polyline, Mode.PolylineAsPath -> {
                        val asPath = drawingMode === Mode.PolylineAsPath
                        val color = Color.argb(100, 100, 100, 100)
                        val line = Polyline(map)
                        line.usePath(true)
                        line.infoWindow = BasicInfoWindow(R.layout.bonuspack_bubble, map)
                        line.outlinePaint.color = color
                        line.title = "This is a polyline" + if (asPath) " as Path" else ""
                        line.setPoints(geoPoints)
                        line.showInfoWindow()
                        line.outlinePaint.strokeCap = Paint.Cap.ROUND
                        //example below
                        /*
                        line.setOnClickListener(new Polyline.OnClickListener() {
                            @Override
                            public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
                                Toast.makeText(mapView.getContext(), "polyline with " + polyline.getPoints().size() + "pts was tapped", Toast.LENGTH_LONG).show();
                                return false;
                            }
                        });
                        */if (withArrows) {
                            val arrowPaint = Paint()
                            arrowPaint.color = color
                            arrowPaint.strokeWidth = 10.0f
                            arrowPaint.style = Paint.Style.FILL_AND_STROKE
                            arrowPaint.isAntiAlias = true
                            val arrowPath = Path() // a simple arrow towards the right
                            arrowPath.moveTo(-10.0f, -10.0f)
                            arrowPath.lineTo(10f, 0f)
                            arrowPath.lineTo(-10f, 10f)
                            arrowPath.close()
                            val managers: MutableList<MilestoneManager> = ArrayList()
                            managers.add(
                                MilestoneManager(
                                    MilestonePixelDistanceLister(50.0, 50.0),
                                    MilestonePathDisplayer(0.0, true, arrowPath, arrowPaint)
                                )
                            )
                            line.setMilestoneManagers(managers)
                        }
                        line.subDescription = line.bounds.toString()
                        map!!.overlayManager.add(line)
                        lastPolygon = null
                    }

                    Mode.Polygon -> {
                        val polygon = Polygon(map)
                        polygon.infoWindow =
                            BasicInfoWindow(R.layout.bonuspack_bubble, map)
                        polygon.fillPaint.color = Color.argb(75, 255, 0, 0)
                        polygon.points = geoPoints
                        polygon.title = "A sample polygon"
                        polygon.showInfoWindow()
                        if (withArrows) {
                            val bitmap = BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.round_navigation_white_48
                            )
                            val managers: MutableList<MilestoneManager> = ArrayList()
                            managers.add(
                                MilestoneManager(
                                    MilestonePixelDistanceLister(20.0, 200.0),
                                    MilestoneBitmapDisplayer(
                                        90.0,
                                        true,
                                        bitmap,
                                        bitmap.width / 2,
                                        bitmap.height / 2
                                    )
                                )
                            )
                            polygon.setMilestoneManagers(managers)
                        }
                        polygon.setOnClickListener { polygon, mapView, eventPos ->
                            lastPolygon = polygon
                            polygon.onClickDefault(polygon, mapView, eventPos)
                            Toast.makeText(
                                mapView.context,
                                "polygon with " + polygon.actualPoints.size + "pts was tapped",
                                Toast.LENGTH_LONG
                            ).show()
                            false
                        }
                        //polygon.setSubDescription(BoundingBox.fromGeoPoints(polygon.getPoints()).toString());
                        map!!.overlayManager.add(polygon)
                        lastPolygon = polygon
                    }

                    Mode.PolygonHole -> if (lastPolygon != null) {
                        val holes: MutableList<List<GeoPoint>> = ArrayList()
                        holes.add(geoPoints)
                        lastPolygon!!.setHoles(holes)
                    }
                }
                map!!.invalidate()
            }
        }
        pts.clear()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        pts.add(Point(x.toInt(), y.toInt()))
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touch_start(x, y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                touch_move(x, y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                touch_up()
                invalidate()
            }
        }
        return true
    }

    fun destroy() {
        map = null
        lastPolygon = null
    }
}