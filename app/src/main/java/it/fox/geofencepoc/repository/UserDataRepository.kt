package it.fox.geofencepoc.repository

import android.content.Context
import androidx.room.Room
import it.fox.geofencepoc.dao.GeofenceDatabase
import it.fox.geofencepoc.domain.Geofence
import it.fox.geofencepoc.domain.UserData
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException

class UserDataRepository private constructor(context: Context) {

    private val database: GeofenceDatabase = Room.databaseBuilder(context.applicationContext,
        GeofenceDatabase::class.java,
        DB_NAME).build()

    val allUserData = getUserData()

    suspend fun insert(userData: UserData) = database.userdataDao().insert(userData)

    suspend fun deleteAll() = database.userdataDao().deleteAll()

    fun getUserData() : Flow<MutableList<UserData>> = database.userdataDao().getUserData()

    companion object {
        private var INSTANCE: UserDataRepository?=null

        fun initialize(context: Context){
            if (INSTANCE==null) INSTANCE= UserDataRepository(context)
        }

        fun get():UserDataRepository {
            return INSTANCE?:throw IllegalStateException("UserData Repository must be initialized")
        }
    }

}