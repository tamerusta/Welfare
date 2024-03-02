package com.works.solutionchallange2024.view
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlin.random.Random
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
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
import com.works.solutionchallange2024.adapter.ImageAdapter
import com.works.solutionchallange2024.common.LocationPermissionHelper
import com.works.solutionchallange2024.databinding.FragmentProductBuyDetailBinding
import com.works.solutionchallange2024.model.ImageItems
import com.works.solutionchallange2024.config.AppDatabase
import com.works.solutionchallange2024.model.FavData
import com.works.solutionchallange2024.model.LocationData
import com.works.solutionchallange2024.model.retrofit.AdvertDetail
import com.works.solutionchallange2024.service.FavDao
import com.works.solutionchallange2024.viewmodel.ProductDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class ProductBuyDetailFragment : Fragment() {
    private lateinit var productDetailViewModel: ProductDetailViewModel
    private lateinit var binding : FragmentProductBuyDetailBinding
    private lateinit var favDao : FavDao
    private lateinit var favDb : AppDatabase
    private lateinit var selectedAdvert : AdvertDetail
    lateinit var mapView: MapView
    lateinit var flpc: FusedLocationProviderClient
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback

    var message = ""
    var city: String = ""
    var adress: String = ""
    var productLongitude: Double = 0.0
    var productLatitude: Double = 0.0
    var getLocation: GetLocation = GetLocation()
    var locationData: LocationData? = null

    @SuppressLint("VisibleForTests")
    private lateinit var locationRequest: LocationRequest
    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8, 0, 8, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBuyDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        favDb = AppDatabase.databaseAccess(requireContext())!!
        favDao = favDb.FavDao()

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        binding.purchaseDetailGoLoc.setOnClickListener {
            openLocation(adress)
        }

        var productId = arguments?.getString("productId")
        Log.e("productId","$productId")

        binding.purchaseDetailAddFav.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                if (productId != null) {
                    addFavorites(productId!!)
                } else{
                    productId = "aabbcc"
                    addFavorites(productId!!)
                }
                Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT)
                    .show()
        }

        binding.productDetailBackButton.setOnClickListener {
           findNavController().navigate(R.id.action_productBuyDetailFragment2_to_homePageFragment)
        }
        mapView.getMapAsync {
            updateMap()
        }

        productDetailViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[ProductDetailViewModel::class.java]
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flpc = LocationServices.getFusedLocationProviderClient(requireContext())
        initLocationServices()
        val locationPermissionHelper = LocationPermissionHelper()
        locationPermissionHelper.checktLocationPermission(requireContext(), requireActivity())
    }

    fun takeImage(imageUriList: List<String>) {
        val imageItemsList = imageUriList.mapIndexed { index, uri ->
            ImageItems("image_$index", uri)
        }

        val imageAdapter = ImageAdapter()
        binding.viewpager?.adapter = imageAdapter
        imageAdapter.submitList(imageItemsList)

        // DOT ADDED PART AS THE NUMBER OF ADDED PHOTOS.

        val dotsImagge = Array(imageItemsList.size) { ImageView(requireContext()) }

        dotsImagge.forEach {
            it.setImageResource(R.drawable.non_active_dot)
            binding.slideDotLL.addView(it, params)
        }

        dotsImagge[0].setImageResource(R.drawable.active_dot)
        pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dotsImagge.mapIndexed { index, imageView ->
                    if (position == index) {
                        imageView.setImageResource(R.drawable.active_dot)
                    } else {
                        imageView.setImageResource(R.drawable.non_active_dot)
                    }
                }
                super.onPageSelected(position)
            }
        }
        binding.viewpager.registerOnPageChangeCallback(pageChangeListener)
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
                    var adreS = geocoder.getFromLocation(productLatitude, productLongitude, 1)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).hideBottomNavigation()

        val args: ProductBuyDetailFragmentArgs by navArgs()
        val productId = args.advertId

        productDetailViewModel.getAdvertByDetail(productId)

        productDetailViewModel.advert.observe(viewLifecycleOwner){
            setData(it)
            selectedAdvert = it

        }

        binding.purchaseDetailJoinRaffle.setOnClickListener {
            productDetailViewModel.joinAdvertRaffle(productId)
        }

        productDetailViewModel.message.observe(viewLifecycleOwner){
            message = it
            if (message.isNotEmpty())
            {
                Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            }
        }

        productDetailViewModel.success.observe(viewLifecycleOwner){
            if (it)
            {
                val transition = ProductBuyDetailFragmentDirections.actionProductBuyDetailFragment2ToHomePageFragment()
                findNavController().navigate(transition)
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

    fun openLocation(adres: String) {

        // let set on button click

        val uri = Uri.parse("geo:0, 0?q=$adres")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    private fun updateMap() {
        val coordinates = LatLng(productLatitude, productLongitude)
        mapView.getMapAsync { googleMap ->
            googleMap.clear() // Clear existing markers
            googleMap.addMarker(MarkerOptions().position(coordinates).title("Ürün Konumu"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14f))
        }
    }

    fun setData(advert : AdvertDetail)
    {
        val latitude = advert.latitude.replace(',', '.').toDouble()
        val longitude = advert.longitude.replace(',', '.').toDouble()
        productLatitude = latitude
        productLongitude = longitude
        updateMap()
        takeImage(advert.images)
        binding.productDetailLocationCity.text = advert.city
        binding.productDetailUserName.text = advert.owner
        binding.productDetailLocationDistrict.text = "Belek/Antalya"
        binding.purchaseDetailExplanation.text = advert.description
        binding.purchaseDetailAvarageLike.text = advert.point.toString()
        binding.purchaseDetailProductName.text = advert.tag
        binding.purchaseDetailProductName.text = "Participants : ${advert.participantCount} / ${advert.minParticipants}"
        val rating = Random.nextDouble(0.0, 5.0)
        binding.ratingBar.rating = rating.toFloat()

    }

    fun addFavorites(productId : String){
        val job = CoroutineScope(Dispatchers.Main).launch {
            val newFav = FavData(0,productId)
            favDao.addTag(newFav)
            Log.e("fav basarili","${newFav.tag_id},$productId")
        }
    }
}