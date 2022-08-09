package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class ElectionsRepository {
    suspend fun getElections(): List<Election>? {
        return try {
            CivicsApi.retrofitService.getElections().elections
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getVoterInfo(address: String, electionId: Int): VoterInfoResponse? {
        return try {
            CivicsApi.retrofitService.getVoterInfo(address, electionId)
        } catch (e: Exception) {
            null
        }
    }
}