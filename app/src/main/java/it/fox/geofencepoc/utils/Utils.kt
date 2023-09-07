package it.fox.geofencepoc.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class Utils {
    companion object {
        fun checkPermission(permissions:Array<String>, ctx:Context): Boolean{
            for (perm in permissions)
                if (ActivityCompat.checkSelfPermission(ctx,perm)!=  PackageManager.PERMISSION_GRANTED) return false
            return true
        }
    }
}