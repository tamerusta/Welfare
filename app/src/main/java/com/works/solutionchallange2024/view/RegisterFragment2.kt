package com.works.solutionchallange2024.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.myui.common.GetLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.works.solutionchallange2024.R
import com.works.solutionchallange2024.databinding.FragmentRegisterFragment2Binding
import com.works.solutionchallange2024.model.LocationData
import com.works.solutionchallange2024.model.UserCredentials
import com.works.solutionchallange2024.model.retrofit.NewUser
import com.works.solutionchallange2024.viewmodel.RegisterViewModel
import java.io.IOException
import java.util.Locale

class RegisterFragment2 : Fragment() {

    private var _binding: FragmentRegisterFragment2Binding? = null
    private val binding get() = _binding!!
    private lateinit var registerButton: Button
    private var isLocationPermissionGranted = false
    private var userCredentials: UserCredentials? = null
    private lateinit var registerViewmodel : RegisterViewModel
    private lateinit var flpc: FusedLocationProviderClient
    private var mMap: GoogleMap? = null
    var thisData: LocationData? = null
    private lateinit var locationTask: Task<Location>
    private lateinit var locationRequest: LocationRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterFragment2Binding.inflate(inflater, container, false)
        val view = binding.root

        registerViewmodel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[RegisterViewModel::class.java]


        initLocationServices()
        takeLastLocation(requireContext(),requireActivity())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: RegisterFragment2Args by navArgs()
        val nameAndSurname = args.nameAndSurname
        val userName = args.userName
        val email = args.email
        val phone = args.phone

        // Alınan verileri bir modele dönüştür
        userCredentials = UserCredentials(nameAndSurname,userName,email,phone)
        Log.e("USER",userCredentials.toString())

        registerButton = view.findViewById(R.id.buttonRegister)

        val passwordEditText = view.findViewById<EditText>(R.id.editTextPassword)
        val confirmPasswordEditText = view.findViewById<EditText>(R.id.editTextPasswordLogin)
        val strengthBars = listOf<View>(
            view.findViewById(R.id.strengthBar1),
            view.findViewById(R.id.strengthBar2),
            view.findViewById(R.id.strengthBar3),
            view.findViewById(R.id.strengthBar4)
        )
        val locationPermission = view.findViewById<TextView>(R.id.textViewPoint)
        val registerButton = view.findViewById<Button>(R.id.buttonRegister)
        val privacyPolicyTextView = view.findViewById<TextView>(R.id.textViewPrivacyPolicyLink)
        val termOfUseTextView = view.findViewById<TextView>(R.id.textViewTermOfUseLink)
        val checkBoxRememberMe = view.findViewById<CheckBox>(R.id.checkBoxRememberMe)

        termOfUseTextView.setOnClickListener {
            // Terms of use direct click to accept function
        }

        privacyPolicyTextView.setOnClickListener {
            // Redirect to privacy policy click function
        }

        binding.imagePadlockTwo.setOnClickListener {
            binding.editTextPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }

        binding.imagePadlock.setOnClickListener {
            binding.editTextPasswordLogin.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }

        passwordEditText.addTextChangedListener(
            PasswordStrengthWatcher(
                passwordEditText,
                confirmPasswordEditText,
                strengthBars
            )
        )

        locationPermission.setOnClickListener {
            requestLocationPermission()

        }

        registerButton.setOnClickListener {
            //userCredentials?.let { credentials ->
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val passwordStrength = calculatePasswordStrength(password)

            when {
                password.isBlank() -> {
                    Toast.makeText(
                        requireContext(),
                        "Password cannot be left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                confirmPassword.isBlank() -> {
                    Toast.makeText(
                        requireContext(),
                        "Password repetition cannot be left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                confirmPassword != password -> {
                    Toast.makeText(requireContext(), "Passwords do not match!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                !isValidPassword(password) -> {
                    Toast.makeText(
                        requireContext(),
                        "Password is invalid! It must be at least 6 characters long and must not contain spaces.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                passwordStrength == 1 -> {
                    showToast("Your password is too weak. Please choose a stronger password.")
                    return@setOnClickListener
                }

                !checkBoxRememberMe.isChecked -> {
                    Toast.makeText(
                        requireContext(),
                        "You cannot register without accepting the terms!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                else -> {                   // Navigation
                    val bundle: RegisterFragment2Args by navArgs()
                    val userName = bundle.userName
                    val mail = bundle.email
                    val phone = bundle.phone


                    val transition = RegisterFragment2Directions.registerTransition2(
                        password = binding.editTextPassword.text.toString(),
                        userName = userName,
                        email = mail
                    )

                    Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT)
                        .show()
                    val user = getWrittenUser()
                    if (user != null)
                    {
                        registerViewmodel.createUser(user)
                        Navigation.findNavController(it).navigate(transition)
                    }
                }
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordLengthMin = 6
        val passwordLengthMax = 32

        return password.length in passwordLengthMin..passwordLengthMax && !password.contains(" ")
    }

    private inner class PasswordStrengthWatcher(
        private val passwordEditText: EditText,
        private val confirmPasswordEditText: EditText,
        private val strengthBars: List<View>
    ) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            val password = s.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val strength = calculatePasswordStrength(password)

            if (strength == 1) {
                passwordEditText.error =
                    "Your password is too weak. Please choose a stronger password."
            } else {
                passwordEditText.error = null
            }

            if (confirmPassword != password) {
                confirmPasswordEditText.error = "Passwords do not match!"
            } else {
                confirmPasswordEditText.error = null
            }
            updateStrengthBars(strength)
        }

        private fun calculatePasswordStrength(password: String): Int {
            var strength = 0
            val hasLowercase = password.any { it.isLowerCase() }
            val hasUppercase = password.any { it.isUpperCase() }
            val hasDigit = password.any { it.isDigit() }
            val hasSpecialChar = password.any { !it.isLetterOrDigit() }

            if (hasLowercase) strength++
            if (hasUppercase) strength++
            if (hasDigit) strength++
            if (hasSpecialChar) strength++

            return strength
        }

        private fun updateStrengthBars(strength: Int) {
            for (i in 0 until strengthBars.size) {
                if (i < strength) {
                    strengthBars[i].background = getColorForStrength(i)
                } else {
                    strengthBars[i].background = getColorForStrength(-1)
                }
            }
        }

        private fun getColorForStrength(strength: Int): Drawable? {
            return when (strength) {
                0 -> ContextCompat.getDrawable(
                    passwordEditText.context,
                    R.color.red
                )
                1 -> ContextCompat.getDrawable(
                    passwordEditText.context,
                    R.color.orange
                )
                2 -> ContextCompat.getDrawable(
                    passwordEditText.context,
                    R.color.duskYellow
                )
                3 -> ContextCompat.getDrawable(
                    passwordEditText.context,
                    R.color.lightGrey
                )
                else -> ContextCompat.getDrawable(
                    passwordEditText.context,
                    android.R.color.transparent
                )
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun requestLocationPermission() {
        val locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                locationPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showToast("Location permission has already been granted.")
            isLocationPermissionGranted = true
            takeLastLocation(requireContext(),requireActivity())
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(locationPermission),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Location permission granted. You can get a location.")
                isLocationPermissionGranted = true
            } else {
                showToast("Location permission denied. You need to allow location from settings.")
                isLocationPermissionGranted = false
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    private fun calculatePasswordStrength(password: String): Int {
        var strength = 0
        val hasLowercase = password.any { it.isLowerCase() }
        val hasUppercase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        if (hasLowercase) strength++
        if (hasUppercase) strength++
        if (hasDigit) strength++
        if (hasSpecialChar) strength++

        return strength
    }

    private fun initLocationServices() {
        flpc = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
    }

    // APPLICATION PERMISSION THIS IS THE PART WHERE GPS PERMISSIONS WILL BE
    fun takeLastLocation(context: Context, activity: Activity) {
        var sehir = ""
        var adres = ""
        var enlem = ""
        var boylam = ""
        var locationData: LocationData
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Location Error")
            alertDialog.setMessage("Please turn on location feature.")
            alertDialog.setPositiveButton("Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity?.startActivity(intent)
            }
            alertDialog.show()
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100
            )
        }

        mMap?.isMyLocationEnabled = true
        locationTask = flpc.lastLocation
        flpc.lastLocation.addOnSuccessListener(activity) { location ->
            if (location != null) {
                try {

                    val geocoder = Geocoder(context, Locale.getDefault())
                    val adress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    var currentLatLong = LatLng(location.latitude, location.longitude)
                    sehir = adress?.get(0)?.adminArea.toString()
                    adres = adress?.get(0)?.getAddressLine(0).toString()
                    enlem = currentLatLong.latitude.toString()
                    boylam = currentLatLong.longitude.toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    locationData = LocationData(enlem, boylam, adres, sehir)
                    thisData = locationData
                    binding.textViewLocation.text = adres

                }
            }
        }
    }

    fun getWrittenUser() : NewUser? {
        val email = userCredentials?.email
        val username = userCredentials?.username
        val password = binding.editTextPassword.text.toString()
        val fullname = userCredentials?.fullname
        val phone = userCredentials?.phone
        val location = thisData?.location
        val city = thisData?.city
        val longitude = thisData?.longitude
        val latitude = thisData?.latitude


        if (email != null && username != null && fullname != null && phone != null &&
            location != null && city != null && longitude != null && latitude != null) {

            val nameAndSurname = fullname.split(" ")
            val name = nameAndSurname.getOrElse(0) { "" }
            val surname = nameAndSurname.getOrElse(1) { "" }

            return NewUser(email, username, password, name, surname, phone, location, city, longitude, latitude)

        } else {

            return null
        }
    }
}