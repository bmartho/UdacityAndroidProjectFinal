package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {
    lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(
            this, VoterInfoViewModelFactory()
        ).get(VoterInfoViewModel::class.java)

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val args: VoterInfoFragmentArgs by navArgs()
        val electionId = args.argElectionId
        val division = args.argDivision

        val address = "${division.state} ${division.country}"
        viewModel.getVoterInfo(address, electionId)

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        return binding.root

    }

    //TODO: Create method to load URL intents

}