package com.works.solutionchallange2024.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myui.common.GetLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.adapter.ViewPagerAdapter
import com.works.solutionchallange2024.util.CommonUtil
import com.works.solutionchallange2024.common.permissionS
import com.works.solutionchallange2024.databinding.FragmentSellBinding
import com.works.solutionchallange2024.model.LocationData
import com.works.solutionchallange2024.model.SellPage
import com.works.solutionchallange2024.model.ViewPagerItemData
import com.works.solutionchallange2024.model.retrofit.AddAdvert
import com.works.solutionchallange2024.service.LocalDatabase
import com.works.solutionchallange2024.service.TagsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.UUID

class Sell2Fragment : Fragment(), permissionS {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var _binding: FragmentSellBinding? = null
    private lateinit var imageList: ArrayList<String>
    var stringImageList: ArrayList<String>? = null
    var imageListMultipart : List<MultipartBody.Part>? = null
    private val binding get() = _binding!!
    private val PERMISSION_REQUEST_CODE = 123
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback
    private lateinit var db: LocalDatabase
    private lateinit var dao: TagsDao
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8, 0, 8, 0)
    }

    lateinit var flpc: FusedLocationProviderClient
    var city: String = ""
    var adress: String = ""
    var productLongitude: Double = 0.0
    var productLatitude: Double = 0.0
    var getLocation: GetLocation = GetLocation()
    var locationData: LocationData? = null

    @SuppressLint("VisibleForTests")
    private lateinit var locationRequest: LocationRequest
    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->

            val imageItemList = uris.map { uri ->
                ViewPagerItemData(UUID.randomUUID().toString(), uri.toString())
            }
            takeImage(imageItemList)
        }

    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun takeImage(imageItemList: List<ViewPagerItemData>) {
        val imageAdapter = ViewPagerAdapter()
        binding.sellFragmentViewPager?.adapter = imageAdapter
        imageAdapter.submitList(imageItemList)

        val dotsImagge = Array(imageItemList.size) { ImageView(requireContext()) }
        dotsImagge.forEach {
            it.setImageResource(R.drawable.non_active_dot)
            binding.slideDotLL.addView(it, params)
        }
        dotsImagge[0].setImageResource(R.drawable.active_dot)
        pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dotsImagge.mapIndexed { index, imageView ->
                    if (position == index) {
                        imageView.setImageResource(
                            R.drawable.active_dot
                        )
                    } else {
                        imageView.setImageResource(R.drawable.non_active_dot)
                    }
                }
                super.onPageSelected(position)
            }
        }
        binding.sellFragmentViewPager.registerOnPageChangeCallback(pageChangeListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //

        binding.takeLocation.setOnClickListener {
            checktLocationPermission(requireContext(), requireActivity())
            getLastLocation()

            binding.sellPageAdressEditText.text = locationData?.location
        }

        imageList = ArrayList()

        DefaultState()

        // Initialize database and DAO
        db = LocalDatabase.databaseAccess(requireContext())!!
        dao = db.getTags()

        // Initialize adapter and ListView
        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        val listView = binding.listView
        listView.adapter = arrayAdapter

        loadTags()

        val searchText = binding.sellPageProductSearchText

        searchText.setOnClickListener {
            listView.visibility = View.VISIBLE
        }

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    loadTags()
                } else if (s.length >= 3) {
                    search(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            val clickedItem = arrayAdapter.getItem(position)
            binding.sellPageProductSearchText.setText(clickedItem)
            Log.e("Clicked item", "$clickedItem at position $position")
            listView.visibility = View.INVISIBLE

                val category = clickedItem?.let { findCategory(it) }
                Log.d("Category", "Category for \"$clickedItem\": $category")
                binding.sellPageCategoryTextView.text = category.toString()

        }

        binding.sellPageConstraintLayout.setOnClickListener {
            listView.visibility = View.INVISIBLE
        }

        binding.sellPageImageAddImageView.setOnClickListener {
            checkPermissionAndOpenGallery()
        }
        binding.sellPageEditAcceptButon.setOnClickListener {

            CommonUtil.showAlertDialog(
                requireContext(),
                "Save",
                "Are you sure you want to save?",
                "YES",
                "NO",
                {
                    Log.e("GEL", getWrittenSellPage().toString())
                    getWrittenSellPage()
                    DefaultState()

                },
                {
                    // Actions to be taken when the No button is clicked
                    // Since the user canceled the operation, there is no need to do anything.
                }
            )
        }

        binding.sellPageEditCancelButton.setOnClickListener {
            cancelEditing()
            DefaultState()
            requireActivity().supportFragmentManager.popBackStack()

            Log.e("tarı", "deneme")
        }

        return root
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flpc = LocationServices.getFusedLocationProviderClient(requireContext())
        initLocationServices()

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            }
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            }

    }

    private fun search(keyword: String) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            val list = dao.searchTag("%$keyword%")

            arrayAdapter.clear()
            for (tag in list) {
                arrayAdapter.add(tag.tag_name)
            }
        }
    }

    private fun loadTags() {
        val job = CoroutineScope(Dispatchers.Main).launch {
            val list = dao.allTags()

            arrayAdapter.clear()
            for (tag in list) {
                arrayAdapter.add(tag.tag_name)
            }
        }
    }

    private val selectImagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Galeriye erişim izni verilmedi.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissionAndOpenGallery() {
        if (checkPermission()) {
            openGallery()
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    Snackbar.make(
                        requireView(),
                        "Permission needed for gallery",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Allow", View.OnClickListener {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()
                } else {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    openGallery()
                }
            } else {
                openGallery()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Snackbar.make(
                        requireView(),
                        "Permission needed for gallery",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Allow", View.OnClickListener {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                openGallery()
            }

        }

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Galeriye erişim izni verilmedi.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openGallery() {
        pickImages.launch("image/*")
    }


    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    private fun cancelEditing() {
        stringImageList?.clear()
        binding.sellFragmentViewPager.adapter?.notifyDataSetChanged()
    }

    fun UpdateState() {

        binding.sellPageProductSearchText.isEnabled = true
        binding.sellPageProductDescriptionEditText.isEnabled = true
        binding.sellPageEditAcceptButon.visibility = View.VISIBLE
        binding.sellPageEditCancelButton.visibility = View.VISIBLE
        binding.sellPageImageAddImageView.visibility = View.VISIBLE
    }

    fun DefaultState() {

        binding.sellPageAdressEditText.isEnabled = true
        binding.sellPageProductDescriptionEditText.isEnabled = true
        binding.sellPageImageAddImageView.visibility = View.VISIBLE
        binding.sellPageEditAcceptButon.visibility = View.VISIBLE
        binding.sellPageEditCancelButton.visibility = View.VISIBLE
    }

    fun RestoreData(sellPageData: SellPage?) {
        binding.sellPageProductSearchText.text =
            Editable.Factory.getInstance().newEditable(sellPageData?.productName ?: "")
        binding.sellPageAdressEditText.text =
            Editable.Factory.getInstance().newEditable(sellPageData?.adress ?: "")
        binding.sellPageProductDescriptionEditText.text =
            Editable.Factory.getInstance().newEditable(sellPageData?.description ?: "")
    }

    fun getWrittenSellPage(): AddAdvert {

        if (!::imageList.isInitialized) {
            imageList = ArrayList()
        }

        val productName = binding.sellPageProductSearchText.text.toString()
        val category = binding.sellPageCategoryTextView.text.toString()
        val adress = binding.sellPageAdressEditText.text.toString()
        val description = binding.sellPageProductDescriptionEditText.text.toString()

        return AddAdvert("Title",description,category,productName,"2024-03-15","30","active","public",imageListMultipart!!,"Antalya","1")

    }
    private fun findCategory(itemName: String): String {
        val categories = hashMapOf<String, ArrayList<String>>()

        val electronics = arrayListOf(
            "Phone", "Tablet", "Computer", "Headphones", "Radio",
            "Smartwatch", "Speaker", "Monitor", "Powerbank",
            "Keyboard", "Mouse", "Game consoles"
        )
        categories["Electronics"] = electronics

        val hobby = arrayListOf(
            "Antiques and collectibles", "Music albums", "Magazines and newspapers",
            "DVD and Blu-ray movies", "Photography equipment", "Comic books",
            "Figurines", "Board games", "Toys", "Arts and crafts"
        )
        categories["Hobby"] = hobby

        val homeFurnishings = arrayListOf(
            "Bookcase", "Desk", "Wardrobe", "Sofa", "TV stand",
            "Rug", "Floor lamp", "Work desk", "Dining table", "TV stand",
            "Wardrobe", "Nightstand", "Display cabinet", "Coat rack",
            "Curtain", "Mirror", "Kitchen table", "Chair", "Coffee table", "Bed"
        )
        categories["Home Furnishings"] = homeFurnishings

        val clothing = arrayListOf(
            "Coat", "T-shirt", "Pants", "Sweater", "Shoes",
            "Hat", "Shorts", "Shirt", "Dress", "Jacket",
            "Skirt", "Gloves", "Jewelry and accessories"
        )
        categories["Clothing"] = clothing

        val babyProducts = arrayListOf(
            "Baby care products (diapers etc.)", "Baby clothing", "Crib",
            "Stroller", "Baby toys"
        )
        categories["Baby Products"] = babyProducts

        val musicalInstruments = arrayListOf(
            "Acoustic guitar", "Bass guitar", "Violin",
            "Drum set", "Flute", "Electric guitar"
        )
        categories["Musical Instruments"] = musicalInstruments

        for ((category, items) in categories) {
            if (itemName in items) {
                return category
            }
        }
        return "Category not found"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
