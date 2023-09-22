package it.fox.geofencepoc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.fox.geofencepoc.domain.UserData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {

    @Query("SELECT * FROM userdata")
    fun getUserData(): Flow<MutableList<UserData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userData: UserData)

    @Query("DELETE FROM userdata")
    suspend fun deleteAll()
}