package com.works.solutionchallange2024.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myui.common.GetLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.works.solutionchallange2024.HomeActivity
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.common.permissionS
import com.works.solutionchallange2024.databinding.FragmentProfileInformationBinding
import com.works.solutionchallange2024.model.LocationData

import java.util.Locale
class ProfileInformationFragment : Fragment(), permissionS {
    private lateinit var binding: FragmentProfileInformationBinding
    lateinit var flpc: FusedLocationProviderClient
    var city: String = ""
    var adress: String = ""
    var productLongitude: Double = 0.0
    var productLatitude: Double = 0.0
    var getLocation: GetLocation = GetLocation()
    var locationData: LocationData? = null
    lateinit var mapView: MapView

    @SuppressLint("VisibleForTests")
    private lateinit var locationRequest: LocationRequest
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileInformationBinding.inflate(inflater, container, false)
        binding.profileInformationBackImageView.setOnClickListener {
            val action = ProfileInformationFragmentDirections.actionProfileInformationFragmentToProfileFragment()
            findNavController().navigate(action)
        }
        mapView = binding.root.findViewById(R.id.mapView)

        binding.profilePasswordTextView.setOnClickListener {
            val action = ProfileInformationFragmentDirections.actionProfileInformationFragmentToProfileChangePasswordFragment()
            findNavController().navigate(action)
        }

        binding.profileLocationTextView.setOnClickListener {

            checktLocationPermission(requireContext(), requireActivity())
            getLastLocation()
        }
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            updateMap()
        }
        binding.imageView7.setOnClickListener {
            findNavController().navigate(R.id.action_profileInformationFragment_to_profileFragment)
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flpc = LocationServices.getFusedLocationProviderClient(requireContext())
        initLocationServices()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showBottomNavigation()
    }

    fun getLastLocation() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            flpc.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    productLatitude = it.latitude
                    productLongitude = it.longitude
                    var geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val adreS = Geocoder(requireContext(), Locale.getDefault())
                        .getFromLocation(productLatitude, productLongitude, 1)
                    city = adreS?.get(0)?.adminArea.toString()
                    adress = adreS?.get(0)?.getAddressLine(0).toString()
                    locationData = LocationData(
                        productLatitude.toString(),
                        productLongitude.toString(),
                        adress,
                        city
                    )
                    updateMap()
                    getLocation.checkLastLocation(locationData!!)
                }
            }
        }
    }

    private fun initLocationServices() {
        flpc = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
    }

    private fun updateMap() {
        val coordinates = LatLng(productLatitude, productLongitude)
        mapView.getMapAsync { googleMap ->
            googleMap.clear() // Clear existing markers
            googleMap.addMarker(MarkerOptions().position(coordinates).title("Product Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14f))
        }
    }
}