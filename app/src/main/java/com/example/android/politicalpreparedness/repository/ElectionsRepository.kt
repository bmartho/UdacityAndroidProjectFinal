package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val dataSource: ElectionDao) {
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

    suspend fun followElection(election: Election) {
        withContext(Dispatchers.IO) {
            dataSource.insertElection(election)
        }
    }

    suspend fun unfollowElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            dataSource.deleteElectionById(electionId)
        }
    }

    suspend fun getElectionById(electionId: Int) =
        withContext(Dispatchers.IO) {
            dataSource.getElectionById(electionId)
        }

    fun getFollowedElections() = dataSource.getAllElections()
}