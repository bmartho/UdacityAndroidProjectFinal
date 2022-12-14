package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

class ElectionsViewModel(private val repository: ElectionsRepository) : ViewModel() {
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections
    private val _upcomingElections = MutableLiveData(listOf<Election>())

    val followedElections = repository.getFollowedElections()

    val upcomingElectionsError: LiveData<Boolean>
        get() = _upcomingElectionsError
    private val _upcomingElectionsError = MutableLiveData(false)

    val upcomingElectionsLoading: LiveData<Boolean>
        get() = _upcomingElectionsLoading
    private val _upcomingElectionsLoading = MutableLiveData(false)

    init {
        getElections()
    }

    private fun getElections() {
        viewModelScope.launch {
            _upcomingElectionsLoading.value = true
            _upcomingElectionsError.value = false

            val elections = repository.getElections()

            if (elections == null) {
                _upcomingElectionsError.value = true
            } else {
                _upcomingElectionsError.value = false
                _upcomingElections.value = elections
            }

            _upcomingElectionsLoading.value = false
        }
    }
}