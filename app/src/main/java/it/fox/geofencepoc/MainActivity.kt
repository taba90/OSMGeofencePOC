package it.fox.geofencepoc

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import it.fox.geofencepoc.broadcastreceivers.APP_STARTED
import it.fox.geofencepoc.broadcastreceivers.LocationProximityStarter
import it.fox.osmgeofencepoc.R
import org.osmdroid.config.Configuration


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        PreferenceManager.getDefaultSharedPreferences(ctx)
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_main)
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->
            if (isGranted.values.stream().allMatch { v -> v }) {
                if (savedInstanceState == null) {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add(R.id.fragment_container,MapFragment())
                    }
                }
            }
            else {
                // Do otherwise
            }
        }
        val perms:MutableList<String> = mutableListOf(Manifest.permission.FOREGROUND_SERVICE,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_PHONE_STATE)
        if (Build.VERSION.SDK_INT >= 23) perms.add(WRITE_EXTERNAL_STORAGE)
        permissionLauncher.launch(perms.toTypedArray())
        broadcastAppStarted()
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