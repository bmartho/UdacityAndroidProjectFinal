package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 12345
    }

    lateinit var viewModel: RepresentativeViewModel
    lateinit var binding: FragmentRepresentativeBinding
    lateinit var representativeListAdapter: RepresentativeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)
        binding.viewModel = viewModel

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            viewModel.getRepresentatives()
        }

        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            if (checkLocationPermissions()) {
                getLocation()
            }
        }

        representativeListAdapter = RepresentativeListAdapter()
        binding.representativesRecycler.adapter = representativeListAdapter

        configureObservers()

        return binding.root
    }

    private fun configureObservers() {
        viewModel.representatives.observe(viewLifecycleOwner) { representatives ->
            if (representatives.isNotEmpty()) {
                representativeListAdapter.submitList(representatives)
                binding.representativesRecycler.visibility = View.VISIBLE
            } else {
                binding.representativesRecycler.visibility = View.GONE
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            getLocation()
        } else {
            Toast.makeText(
                requireContext(),
                R.string.no_location_permission_error,
                Toast.LENGTH_LONG
            ).show()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                viewModel.setAddressBasedOnLocation(location, geocoder)
                viewModel.getRepresentatives()
            } else {
                locationErrorMessage()
            }
        }

        fusedLocationClient.lastLocation.addOnFailureListener {
            locationErrorMessage()
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun locationErrorMessage() {
        Toast.makeText(
            requireContext(),
            R.string.get_location_error,
            Toast.LENGTH_LONG
        ).show()
    }
}