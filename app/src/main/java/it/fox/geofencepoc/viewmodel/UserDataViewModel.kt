package it.fox.geofencepoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import it.fox.geofencepoc.domain.UserData
import it.fox.geofencepoc.repository.UserDataRepository
import kotlinx.coroutines.launch

class UserDataViewModel: ViewModel() {

    private val udRepo: UserDataRepository = UserDataRepository.get()

    val allUserData : LiveData<MutableList<UserData>> = udRepo.allUserData.asLiveData()

    fun insert (userData: UserData) {
        viewModelScope.launch {
            udRepo.deleteAll()
            udRepo.insert(userData)
        }
    }
}