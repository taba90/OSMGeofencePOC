package it.fox.geofencepoc

import android.media.MediaDrm
import android.os.Build
import androidx.annotation.RequiresApi
import java.security.MessageDigest
import java.util.UUID

class UniqueDeviceId {

    companion object {
        fun getUniqueId(): String? {
            val WIDEVINE_UUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
            var wvDrm: MediaDrm? = null
            var result: String? = null
            try {
                wvDrm = MediaDrm(WIDEVINE_UUID)
                val widevineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
                val md = MessageDigest.getInstance("SHA-256")
                md.update(widevineId)
                result = toHexString(md.digest())
            } catch (e: Exception) {

            } finally {
                if (Build.VERSION.SDK_INT >= 28)
                    wvDrm?.close()
                else
                    @Suppress("DEPRECATION")
                    wvDrm?.release()
            }
            return result
        }

        fun toHexString(bytes: ByteArray): String? {
            val hexString = StringBuilder()
            for (i in bytes.indices) {
                val hex = Integer.toHexString(0xFF and bytes[i].toInt())
                if (hex.length == 1) {
                    hexString.append('0')
                }
                hexString.append(hex)
            }
            return hexString.toString()
        }
    }
}