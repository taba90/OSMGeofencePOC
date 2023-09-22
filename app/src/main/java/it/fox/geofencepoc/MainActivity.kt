package it.fox.geofencepoc

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import it.fox.geofencepoc.broadcastreceivers.APP_STARTED
import it.fox.geofencepoc.broadcastreceivers.LocationProximityStarter
import it.fox.geofencepoc.R
import org.osmdroid.config.Configuration


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        PreferenceManager.getDefaultSharedPreferences(ctx)
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_main)
        val header=findViewById<View>(R.id.appheader)
        header.setOnClickListener{
            v->goToHome()
        }
        goToHome()
        broadcastAppStarted()
    }

    fun goToHome() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container,HomeFragment())
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu)
        return false
    }


    private fun broadcastAppStarted() {
        val jobReceiver = Intent(applicationContext, LocationProximityStarter::class.java)
        jobReceiver.action = APP_STARTED
        sendBroadcast(jobReceiver)
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