package it.fox.geofencepoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import it.fox.geofencepoc.domain.Geofence
import it.fox.geofencepoc.repository.GeofenceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.UUID

class GeofenceViewModel:ViewModel() {

    private val gfRepo:GeofenceRepository=GeofenceRepository.get()

    val allGeofenencesCenters : LiveData<MutableList<Geofence>> = gfRepo.allGeofences.asLiveData()


    fun insert (geofence: Geofence) {
        viewModelScope.launch {
            gfRepo.insert(geofence)
        }
    }

    fun update (geofence: Geofence) {
        viewModelScope.launch {
            gfRepo.update(geofence)
        }
    }

    fun delete(geofence: Geofence): Job{
        return viewModelScope.launch {
            gfRepo.delete(geofence)
        }
    }

    fun getGeofence(id: UUID): LiveData<Geofence> {
        return liveData {
            emit(gfRepo.getGeofence(id))
        }
    }


}