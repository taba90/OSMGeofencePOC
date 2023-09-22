package it.fox.geofencepoc.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import kotlin.random.Random


@Entity
class UserData(@PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var deviceId: String?=null,
    var iv:String?=null,
    var privateKey: String?=null
) {

    fun generateIv(): String? {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return Base64.getEncoder().encodeToString(IvParameterSpec(iv).iv)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun generateKey(): String? {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        val key:SecretKey= keyGenerator.generateKey()
        return Base64.getEncoder().encodeToString(key.encoded)
    }
}