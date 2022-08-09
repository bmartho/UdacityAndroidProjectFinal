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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment : Fragment() {

    private lateinit var electionListAdapter: ElectionListAdapter
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
            this.findNavController()
                .navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        election.id,
                        election.division
                    )
                )
        })

        val manager = LinearLayoutManager(activity)
        with(binding.upcomingElectionRecycler) {
            adapter = electionListAdapter
            layoutManager = manager
        }

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        return binding.root
    }

    private fun configureObservers() {
        viewModel.upcomingElections.observe(viewLifecycleOwner) { elections ->
            if (elections.isNotEmpty()) {
                electionListAdapter.submitList(elections)
                binding.upcomingElectionRecycler.visibility = VISIBLE
            }
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

    //TODO: Refresh adapters when fragment loads

}