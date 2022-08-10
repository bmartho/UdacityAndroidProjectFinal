package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
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
            this, VoterInfoViewModelFactory(requireContext())
        ).get(VoterInfoViewModel::class.java)

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val args: VoterInfoFragmentArgs by navArgs()
        val electionId = args.argElectionId
        val division = args.argDivision

        val address = "${division.state} ${division.country}"
        viewModel.getVoterInfo(address, electionId)

        binding.stateLocations.setOnClickListener {
            viewModel.stateLocationUrl.value?.let { url ->
                loadUrl(url)
            }
        }

        binding.stateBallot.setOnClickListener {
            viewModel.stateBallotUrl.value?.let { url ->
                loadUrl(url)
            }
        }

        binding.stateExtraInfo.setOnClickListener {
            viewModel.electionInformationUrl.value?.let { url ->
                loadUrl(url)
            }
        }

        return binding.root
    }

    private fun loadUrl(url: String) {
        var finalurl = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            finalurl = "http://$url"
        }

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(finalurl))
        startActivity(browserIntent)
    }

}