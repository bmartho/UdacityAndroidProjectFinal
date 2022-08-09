package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

class VoterInfoViewModel : ViewModel() {
    private val repository = ElectionsRepository()

    val voterInfo: LiveData<VoterInfoResponse?>
        get() = _voterInfo
    private val _voterInfo = MutableLiveData<VoterInfoResponse?>(null)

    val voterInfoError: LiveData<Boolean>
        get() = _voterInfoError
    private val _voterInfoError = MutableLiveData(false)

    val voterInfoLoading: LiveData<Boolean>
        get() = _voterInfoLoading
    private val _voterInfoLoading = MutableLiveData(false)

    val voterAddress: LiveData<String?>
        get() = _voterAddress
    private val _voterAddress = MutableLiveData<String?>(null)

    val isDbSaved: LiveData<Boolean>
        get() = _isDbSaved
    private val _isDbSaved = MutableLiveData(false)

    val stateBallotUrl: LiveData<String?>
        get() = _stateBallotUrl
    private val _stateBallotUrl = MutableLiveData<String?>(null)

    val stateLocationUrl: LiveData<String?>
        get() = _stateLocationUrl
    private val _stateLocationUrl = MutableLiveData<String?>(null)

    val electionInformationUrl: LiveData<String?>
        get() = _electionInformationUrl
    private val _electionInformationUrl = MutableLiveData<String?>(null)

    fun getVoterInfo(address: String, electionId: Int) {
        viewModelScope.launch {
            _voterInfoLoading.value = true
            _voterInfoError.value = false

            val voterInfo = repository.getVoterInfo(address, electionId)

            if (voterInfo == null) {
                _voterInfoError.value = true
            } else {
                _voterInfoError.value = false
                _voterInfo.value = voterInfo

                buildFinalAddress(voterInfo)

                _stateBallotUrl.value =
                    voterInfo.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
                _stateLocationUrl.value =
                    voterInfo.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
                _electionInformationUrl.value =
                    voterInfo.state?.get(0)?.electionAdministrationBody?.electionInfoUrl
            }

            _voterInfoLoading.value = false
        }
    }


    private fun buildFinalAddress(voterInfoResponse: VoterInfoResponse) {
        voterInfoResponse.state?.let { list ->
            if (list.isNotEmpty()) {
                list[0].electionAdministrationBody.correspondenceAddress?.let { address ->
                    address.apply {
                        _voterAddress.value = "$line1 - $city - $state"
                    }
                }
            }
        }
    }


    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URL
    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}