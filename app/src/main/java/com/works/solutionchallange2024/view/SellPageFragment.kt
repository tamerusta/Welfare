package com.works.solutionchallange2024.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.adapter.ViewPagerAdapterSellDetail
import com.works.solutionchallange2024.util.CommonUtil
import com.works.solutionchallange2024.databinding.FragmentSellPageBinding
import com.works.solutionchallange2024.model.ImageItem
import com.works.solutionchallange2024.model.SellPage
import com.works.solutionchallange2024.service.LocalDatabase
import com.works.solutionchallange2024.service.TagsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SellPageFragment : Fragment() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private var _binding: FragmentSellPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback
    private lateinit var viewPagerAdapterSellDetailWithUrl: ViewPagerAdapterSellDetail
    private var imageList: MutableList<ImageItem> = mutableListOf()
    private lateinit var sellPageData: SellPage
    private lateinit var db: LocalDatabase
    private lateinit var dao: TagsDao
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8, 0, 8, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellPageBinding.inflate(inflater, container, false)

        val root: View = binding.root
        val sellPageProductDescriptionEditText = binding.sellPageProductDescriptionEditText
        val characterCountTextView = binding.characterCountTextView
        val currentText = sellPageProductDescriptionEditText.text.toString()
        val currentLength = currentText.length
        characterCountTextView.text = "$currentLength/300"

        sellPageProductDescriptionEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                // Calculate character count and update TextView
                val inputLength = s?.length ?: 0
                characterCountTextView.text = "$inputLength/300"

                // Disable further input if maximum character limit (300) is reached
                if (inputLength >= 300) {
                    sellPageProductDescriptionEditText.isFocusableInTouchMode = false
                } else {
                    sellPageProductDescriptionEditText.isFocusableInTouchMode = true
                }
            }
        })

        sellPageData = SellPage(
            id = 1,
            productName = "Clothes",
            adStatus = "Passive",
            adress = "Körfez Mah. Ağaç Sk. Daire:2 No:5 İzmit/Kocaeli",
            imageList = ArrayList(),
            description = "UltraGrip Süper Kayma Önleyici Eldiven, günlük kullanımınızda konfor ve güvenlik sunar. Özel kayma önleyici dokularıyla, her işi güvenle yapabilirsiniz. Evde temizlik, bahçe işleri veya spor yaparken rahatlıkla kullanılabilir.",
            category = "SHOE"
        )

        sellPageData.imageList.add("https://www.iskarpinayakkabi.com/i/l/006/0065919_puma-372605-puma-up-erkek-siyah-spor-ayakkabi.jpeg")
        sellPageData.imageList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQy5dpIZ0PMlKnCk1GjEI6qhIUE1zMqI-etu30UWugo5p0bhh4RYDQKheqcMy3WTRJWHN0&usqp=CAU\"")
        sellPageData.imageList.add("https://www.iskarpinayakkabi.com/i/l/006/0065919_puma-372605-puma-up-erkek-siyah-spor-ayakkabi.jpeg")

        takeImageWithUrl(sellPageData.imageList)

        RestoreData(sellPageData)
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
                    loadTags() // Text boş ise tüm listeyi getir
                } else if (s.length >= 3) {
                    search(s.toString()) // Text dolu ve 3 karakterden fazlaysa arama yap
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        listView.setOnItemClickListener { parent, view, position, id ->
            val clickedItem = arrayAdapter.getItem(position)
            binding.sellPageProductSearchText.setText(clickedItem)
            Log.e("Clicked item", "$clickedItem at position $position")
        }

        binding.sellPageConstraintLayout.setOnClickListener {
            listView.visibility = View.INVISIBLE
        }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    pickImages.launch("image/*")
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Devam Etmek İçin İzin Gerekli",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        return root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellPageEndButton.setOnClickListener {
            UpdateState()

            binding.sellPageImageAddImageView.setOnClickListener {
                checkPermissionAndOpenGallery()
            }

            binding.sellPageEditAcceptButon.setOnClickListener {

                val searchText = binding.sellPageProductSearchText.text.toString()
                val categoryText = binding.sellPageCategoryAutoCompeteTextView.text.toString()

                if (searchText.isEmpty() || categoryText.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please enter the information completely",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    CommonUtil.showAlertDialog(
                        requireContext(),
                        "Edit",
                        "Are you sure you want to edit?",
                        "Yes",
                        "No",
                        {
                            Log.e("GEL", getWrittenSellPage().toString())
                            updateDataAfterEdit(getWrittenSellPage())
                            getWrittenSellPage()
                            RestoreData(getWrittenSellPage())
                            DefaultState()
                        },
                        {
                            // Actions to be taken when the No button is clicked
                            // Since the user canceled the operation, there is no need to do anything.
                        }
                    )
                }
            }

            binding.sellPageEditCancelButton.setOnClickListener {
                restoreDataOnCancel()
                DefaultState()
            }

            binding.SellPageTrashImageView.setOnClickListener {
                CommonUtil.showAlertDialog(
                    requireContext(),
                    "Delete",
                    "Are you sure you want to delete this product?",
                    "Yes",
                    "No",
                    {
                    },
                    {
                        // Since the user canceled the operation, there is no need to do anything.
                    }
                )
            }
        }
    }

    fun checkPermissionAndOpenGallery() {
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

                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                pickImages.launch("image/*")
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
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                } else {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {

                pickImages.launch("image/*")

            }

        }
    }

    private val selectedImageUris: MutableList<String> =
        mutableListOf()

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            uris.forEach { uri ->
                selectedImageUris.add(uri.toString())
            }

            val geciciArray = ArrayList(selectedImageUris)
            takeImageWithUrl(geciciArray)
        }

    fun takeImage() {
        val temporaryUriList = mutableListOf<String>()

        for (imageItem in imageList) {
            temporaryUriList.addAll(imageItem.uriList)
        }
        val temporaryArrayList: ArrayList<String> =
            temporaryUriList.toMutableList() as ArrayList<String>

        ViewPagerAdapterSellDetail(temporaryArrayList, binding.sellFragmentViewPager)

        val dotsImagge = Array(imageList.size) { ImageView(requireContext()) }
        clearDots()
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

    private fun clearDots() {
        binding.slideDotLL.removeAllViews()
    }

    fun takeImageWithUrl(imageUrlList: ArrayList<String>) {

        viewPagerAdapterSellDetailWithUrl = ViewPagerAdapterSellDetail(imageUrlList, binding.sellFragmentViewPager)
        binding.sellFragmentViewPager.adapter = viewPagerAdapterSellDetailWithUrl

        val dotsImagge = Array(imageUrlList.size) { ImageView(requireContext()) }
        clearDots()
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

    fun UpdateState() {

        binding.sellPageAdStatusSwitch.setOnClickListener {
            if (binding.sellPageAdStatusSwitch.isChecked) {
                binding.sellPageAdStatusSwitch.text = "Active"
                binding.sellPageAdStatusSwitch.thumbTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.switchColor1
                    )
                )
                binding.sellPageAdStatusSwitch.trackTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.switchColor2
                    )
                )
            } else {
                binding.sellPageAdStatusSwitch.text = "Passive"
                binding.sellPageAdStatusSwitch.thumbTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.switchColorPassive1
                    )
                )
                binding.sellPageAdStatusSwitch.trackTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.switchColorPassive2
                    )
                )
            }
        }

        binding.sellPageCategoryAutoCompeteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val itemSelected = parent.getItemAtPosition(position)
                Toast.makeText(requireContext(), "Kategori : $itemSelected", Toast.LENGTH_SHORT)
                    .show()
            }

        binding.sellPageCategoryAutoCompeteTextView.isEnabled = true
        binding.sellPageProductSearchText.isEnabled = true
        binding.sellPageAdStatusSwitch.isEnabled = true
        binding.sellPageProductDescriptionEditText.isEnabled = true
        binding.sellPageTextInputLayout.isClickable = true
        binding.sellPageTextInputLayout.isEnabled = true
        binding.sellPageEditAcceptButon.visibility = View.VISIBLE
        binding.sellPageEditCancelButton.visibility = View.VISIBLE
        binding.SellPageTrashImageView.visibility = View.VISIBLE
        binding.sellPageImageAddImageView.visibility = View.VISIBLE
        binding.sellPageEndButton.visibility = View.INVISIBLE
    }

    fun DefaultState() {

        binding.sellPageProductSearchText.isEnabled = false
        binding.sellPageAdStatusSwitch.isEnabled = false
        binding.sellPageAdressEditText.isEnabled = false
        binding.sellPageProductDescriptionEditText.isEnabled = false
        binding.sellPageTextInputLayout.isClickable = false
        binding.sellPageTextInputLayout.isEnabled = false
        binding.sellPageEndButton.visibility = View.VISIBLE
        binding.sellPageImageAddImageView.visibility = View.INVISIBLE
        binding.SellPageTrashImageView.visibility = View.INVISIBLE
        binding.sellPageEditAcceptButon.visibility = View.INVISIBLE
        binding.sellPageEditCancelButton.visibility = View.INVISIBLE

    }

    fun RestoreData(sellPageData: SellPage?) {

        binding.sellPageProductSearchText.text =
            Editable.Factory.getInstance().newEditable(sellPageData?.productName ?: "")
        binding.sellPageAdressEditText.text =
            Editable.Factory.getInstance().newEditable(sellPageData?.adress ?: "")
        binding.sellPageCategoryAutoCompeteTextView.text =
            Editable.Factory.getInstance().newEditable(sellPageData?.category ?: "")
        binding.sellPageAdStatusSwitch.text =
            Editable.Factory.getInstance().newEditable(sellPageData?.adStatus ?: "")
        binding.sellPageProductDescriptionEditText.text =
            Editable.Factory.getInstance().newEditable(sellPageData?.description ?: "")

        takeImageWithUrl(sellPageData!!.imageList)
    }

    fun getWrittenSellPage(): SellPage {
        val productName = binding.sellPageProductSearchText.text.toString()
        val category = binding.sellPageCategoryAutoCompeteTextView.text.toString()
        val adStatus = binding.sellPageAdStatusSwitch.text.toString()
        val adress = binding.sellPageAdressEditText.text.toString()
        val description = binding.sellPageProductDescriptionEditText.text.toString()
        val geciciArray = ArrayList(selectedImageUris)

        return SellPage(id, productName, adStatus, adress,geciciArray, description, category)

    }

    var updatedSellPageData: SellPage? = null

    fun updateDataAfterEdit(newSellPageData: SellPage) {
        updatedSellPageData = newSellPageData
    }

    fun restoreDataOnCancel() {
        RestoreData(updatedSellPageData)
    }

}