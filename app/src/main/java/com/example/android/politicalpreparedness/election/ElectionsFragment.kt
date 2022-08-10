package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    private lateinit var electionListAdapter: ElectionListAdapter
    private lateinit var followedElectionListAdapter: ElectionListAdapter
    lateinit var binding: FragmentElectionBinding
    lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            this, ElectionsViewModelFactory(requireContext())
        ).get(ElectionsViewModel::class.java)

        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        configureObservers()

        electionListAdapter = ElectionListAdapter(ElectionListener { election ->
            goToVoterInfo(election)
        })

        followedElectionListAdapter = ElectionListAdapter(ElectionListener { election ->
            goToVoterInfo(election)
        })

        with(binding.upcomingElectionRecycler) {
            adapter = electionListAdapter
        }

        with(binding.followedElectionRecycler) {
            adapter = followedElectionListAdapter
        }

        return binding.root
    }

    private fun goToVoterInfo(election: Election) {
        this.findNavController()
            .navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    election.id,
                    election.division
                )
            )
    }

    private fun configureObservers() {
        viewModel.upcomingElections.observe(viewLifecycleOwner) { elections ->
            if (elections.isNotEmpty()) {
                electionListAdapter.submitList(elections)
                binding.upcomingElectionRecycler.visibility = VISIBLE
            }
        }

        viewModel.followedElections.observe(viewLifecycleOwner) { elections ->
            if (elections.isNotEmpty()) {
                followedElectionListAdapter.submitList(elections)
                binding.followedElectionRecycler.visibility = VISIBLE
            }

            binding.followedElectionLoadingWheel.visibility = GONE
        }

        viewModel.upcomingElectionsLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.upcomingElectionLoadingWheel.visibility = VISIBLE
            } else {
                binding.upcomingElectionLoadingWheel.visibility = GONE
            }
        }

        viewModel.upcomingElectionsError.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                binding.upcomingElectionErrorText.visibility = VISIBLE
            } else {
                binding.upcomingElectionErrorText.visibility = GONE
            }
        }
    }
}