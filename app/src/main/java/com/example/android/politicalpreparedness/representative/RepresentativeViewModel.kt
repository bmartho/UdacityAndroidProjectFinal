package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    private val repository: ElectionsRepository = ElectionsRepository()
) : ViewModel() {
    val representatives: LiveData<List<Representative>>
        get() = _representatives
    private val _representatives = MutableLiveData<List<Representative>>()

    val representativesApiError: LiveData<Boolean>
        get() = _representativesApiError
    private val _representativesApiError = MutableLiveData(false)

    val representativesApiLoading: LiveData<Boolean>
        get() = _representativesApiLoading
    private val _representativesApiLoading = MutableLiveData(false)

    val address = MutableLiveData(Address("", "", "", "", ""))

    fun getRepresentatives() {
        viewModelScope.launch {
            _representativesApiLoading.value = true
            _representativesApiError.value = false
            _representatives.value = listOf()

            address.value?.let { value ->
                val representativeResponse =
                    repository.getRepresentatives(value.toFormattedString())

                if (representativeResponse == null) {
                    _representativesApiError.value = true
                } else {
                    _representativesApiError.value = false

                    val (offices, officials) = representativeResponse
                    _representatives.value =
                        offices.flatMap { office -> office.getRepresentatives(officials) }
                }
            }

            _representativesApiLoading.value = false
        }
    }

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
