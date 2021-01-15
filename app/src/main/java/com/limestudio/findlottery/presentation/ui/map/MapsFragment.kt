package com.limestudio.findlottery.presentation.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.extensions.showAlert
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.services.locationservice.LocationService
import com.limestudio.findlottery.presentation.ui.tickets.list.MODE_VIEW
import com.limestudio.findlottery.presentation.ui.tickets.list.TicketAdapter
import kotlinx.android.synthetic.main.bottom_sheet_map.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException
import java.util.*


class MapsFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {
    private val viewModel: MapsViewModel by viewModels { viewModelFactory }
    private lateinit var viewAdapter: TicketAdapter
    private var geoCoder: Geocoder? = null
    private lateinit var clusterManager: ClusterManager<SellerItem>

    private val callback = OnMapReadyCallback { googleMap ->
        handleMapCallback(googleMap, true)
    }

    private val callbackDenied = OnMapReadyCallback { googleMap ->
        handleMapCallback(googleMap, false)
    }

    @SuppressLint("MissingPermission")
    private fun handleMapCallback(googleMap: GoogleMap, isLocationEnabled: Boolean) {
        clusterManager = ClusterManager(context, googleMap)
        googleMap.setOnInfoWindowClickListener(clusterManager)
//        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterItemInfoWindowClickListener { item ->
            requireActivity().showAlert(
                item.title ?: "Seller",
                "Do you want to contact the seller?"
            ) {
            }
        }
        val defaultBangkok = LatLng(13.756385, 100.502118)
        if (isLocationEnabled) {
            googleMap.isMyLocationEnabled = isLocationEnabled
            googleMap.uiSettings.isCompassEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true

        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultBangkok))
        loadCityTickets(defaultBangkok)
        googleMap.setOnCameraIdleListener {
            loadCityTickets(googleMap.cameraPosition.target)
            clusterManager.onCameraIdle()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        geoCoder = Geocoder(context, Locale.getDefault())
    }

    companion object {
        private const val ACCESS_FINE_LOCATION = 500
    }

    private fun loadCityTickets(location: LatLng) {
        if (Geocoder.isPresent()) {
            try {
                try {
                    val addresses: List<Address>? =
                        geoCoder?.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses?.isNotEmpty() == true) {
                        viewModel.loadAllCityTickets(
                            addresses[0].locality ?: "krungthep"
                        )
                    }
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            } catch (e: IOException) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        requestLocationPermission()

        initStateListener()
        search_view?.doOnTextChanged { text, _, _, _ ->
            text?.let { viewModel.filterTickets(it.toString()) }
        }

        search_view.setOnFocusChangeListener { _, hasFocus ->
            (sheetBehavior as BottomSheetBehavior<*>).state =
                if (hasFocus) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        }
        search_view.setOnClickListener {
            (sheetBehavior as BottomSheetBehavior<*>).state = BottomSheetBehavior.STATE_EXPANDED
        }
        search_view.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val imm: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        viewAdapter = TicketAdapter(MODE_VIEW, { }, { })
        tickets_recycler.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initStateListener() {
        viewModel.state.observe(this) { item ->
            when (item) {
                is MapState.OnShowMessage -> {
                    showWarning(item.error.message)
                }
                else -> showWarning(R.string.operation_not_implemented)
            }
        }
        viewModel.error.observe(viewLifecycleOwner, { error ->
            showWarning(error.message)
        })
        viewModel.filteredTickets.observe(viewLifecycleOwner, { tickets ->
            viewAdapter.setData(tickets)
        })
        viewModel.filteredUsers.observe(viewLifecycleOwner, { users ->
            onUsersUpdated(users)
        })
    }

    private fun onUsersUpdated(users: List<User>) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            try {
                clusterManager.clearItems()
                val builder = LatLngBounds.Builder()
                val offsetItems = users.mapIndexed { index, user ->
                    val latLng = LatLng(
                        user.location?.latitude ?: 0.0,
                        user.location?.longitude ?: 0.0
                    )
                    builder.include(latLng)
                    val offset = index / 60.0
                    val lat = latLng.latitude.plus(offset)
                    val lng = latLng.longitude.plus(offset)
                    SellerItem(
                        lat, lng, "${user.name} ${user.lastName}",
                        user.phoneNumber ?: ""
                    )
                }
                clusterManager.addItems(offsetItems)
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        builder.build(),
                        10
                    )
                )
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    private fun requestLocationPermission() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
            trackingLocation()
        else EasyPermissions.requestPermissions(
            this,
            "Please, enable location service in the application. It require enable all the application functionality. ",
            ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    @AfterPermissionGranted(ACCESS_FINE_LOCATION)
    private fun trackingLocation() {
        val intent = Intent(requireActivity(), LocationService::class.java)
        requireActivity().startService(intent)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callbackDenied)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val locationPermission = permissions.find { it == Manifest.permission.ACCESS_FINE_LOCATION }
        if (requestCode != ACCESS_FINE_LOCATION || locationPermission == null) return
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    inner class SellerItem(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String
    ) : ClusterItem {

        private val position: LatLng
        private val title: String
        private val snippet: String

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String? {
            return title
        }

        override fun getSnippet(): String? {
            return snippet
        }

        init {
            position = LatLng(lat, lng)
            this.title = title
            this.snippet = snippet
        }
    }

}

