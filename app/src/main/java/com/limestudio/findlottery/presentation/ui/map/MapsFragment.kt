package com.limestudio.findlottery.presentation.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import com.limestudio.findlottery.extensions.*
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.services.locationservice.LocationService
import com.limestudio.findlottery.presentation.ui.profile.seller.SellerProfileDialogFragment
import com.limestudio.findlottery.presentation.ui.tickets.list.MODE_VIEW
import com.limestudio.findlottery.presentation.ui.tickets.list.TicketAdapter
import kotlinx.android.synthetic.main.bottom_sheet_map.*
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.notification_alert.view.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

const val DEFAULT_CITY_NAME = "krungthep"
const val ZOOM_LEVEL = 10f

class MapsFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    private val viewModel: MapsViewModel by viewModels { viewModelFactory }
    private lateinit var viewAdapter: TicketAdapter
    private var geoCoder: Geocoder? = null
    private lateinit var clusterManager: ClusterManager<SellerItem>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val DEFAULT_CITY_LOCATION = LatLng(13.756385, 100.502118)

    private val callback = OnMapReadyCallback { googleMap ->
        handleMapCallback(googleMap, true)
    }

    private val callbackDenied = OnMapReadyCallback { googleMap ->
        handleMapCallback(googleMap, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        geoCoder = Geocoder(context, Locale.getDefault())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    companion object {
        private const val ACCESS_FINE_LOCATION = 500
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

        viewAdapter = TicketAdapter(MODE_VIEW, {
            search_view.hideKeyboard()
            viewModel.ticketSelected(it)
            (BottomSheetBehavior.from(bottom_sheet) as BottomSheetBehavior<*>).state =
                BottomSheetBehavior.STATE_COLLAPSED
        }, { })

        checkLocationPermission()
        initStateListener()
        initUI()
    }

    private fun initUI() {
        val sheetBehavior = BottomSheetBehavior.from(bottom_sheet) as BottomSheetBehavior<*>
        sheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        search_view.hideKeyboard()
                    }
                    else -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        search_view?.doOnTextChanged { text, _, _, _ ->
            text?.let { viewModel.filterTickets(it.toString()) }
        }
        search_view.setOnFocusChangeListener { _, hasFocus ->
            sheetBehavior.state =
                if (hasFocus) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        }
        search_view.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        search_view.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        tickets_recycler.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }


    @SuppressLint("MissingPermission")
    private fun handleMapCallback(googleMap: GoogleMap, isLocationEnabled: Boolean) {
        clusterManager = ClusterManager(context, googleMap)
        googleMap.setOnInfoWindowClickListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterItemInfoWindowClickListener { item ->
            SellerProfileDialogFragment.newInstance(item.userId)
                .show(childFragmentManager, "profile")
//            requireActivity().showAlert(
//                item.title,
//                getString(R.string.contact_seller_warming)
//            ) {
//                SellerProfileDialogFragment.newInstance(item.userId)
//                    .show(childFragmentManager, "profile")
//            }
        }
        if (isLocationEnabled) {
            googleMap.isMyLocationEnabled = isLocationEnabled
            googleMap.uiSettings.isCompassEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
            moveCameraToCurrentLocation(googleMap)
        }
        googleMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
            clusterManager.cluster()
            loadCityTickets(googleMap.cameraPosition.target)
        }
    }

    private fun initStateListener() {
        viewModel.state.observe(this) { item ->
            when (item) {
                is MapState.OnShowMessage -> {
                    showWarning(item.error.message)
                }
                is MapState.OnTicketSelected -> {

                    val mapFragment =
                        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                    mapFragment?.getMapAsync { googleMap ->
                        googleMap.zoomCamera(
                            LatLng(
                                item.user.location?.latitude ?: 0.0,
                                item.user.location?.longitude ?: 0.0
                            ), 25f
                        )
                    }
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
        clusterManager.clearItems()
        val builder = LatLngBounds.Builder()
        val offsetItems = users.mapIndexed { index, user ->
            val latLng = LatLng(
                user.location?.latitude ?: DEFAULT_CITY_LOCATION.latitude,
                user.location?.longitude ?: DEFAULT_CITY_LOCATION.longitude
            )
            builder.include(latLng)
            val offset = index / 0.05
            val lat = latLng.latitude//.plus(offset)
            val lng = latLng.longitude//.plus(offset)
            SellerItem(
                lat, lng, "${user.name} ${user.lastName}",
                user.phoneNumber ?: "",
                user.id
            )
        }
        clusterManager.addItems(offsetItems)
        clusterManager.cluster()
    }

    private fun loadCityTickets(location: LatLng) {
        if (Geocoder.isPresent()) {
            try {
                val addresses: List<Address>? =
                    geoCoder?.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses?.isNotEmpty() == true) {
                    val city =
                        if (addresses[0].locality.isNullOrEmpty()) DEFAULT_CITY_NAME else addresses[0].locality
                    city_location?.text = city.capitalize(Locale.ROOT)
                    viewModel.loadAllCityTickets(city)
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    private fun checkLocationPermission() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
            trackingLocation()
        else EasyPermissions.requestPermissions(
            this,
            getString(R.string.location_rationale),
            ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    @AfterPermissionGranted(ACCESS_FINE_LOCATION)
    private fun trackingLocation() {
        val intent = Intent(requireActivity(), LocationService::class.java)
        requireActivity().startService(intent)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(
            callback
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(
            callbackDenied
        )
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

    @SuppressLint("MissingPermission")
    private fun moveCameraToCurrentLocation(googleMap: GoogleMap) {
        var latLng = DEFAULT_CITY_LOCATION
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    latLng = LatLng(
                        it.latitude,
                        it.longitude
                    )
                    googleMap.zoomCamera(latLng)
                } ?: googleMap.zoomCamera(latLng)
            }
    }

    inner class SellerItem(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String,
        val userId: String
    ) : ClusterItem {

        private val position: LatLng
        private val title: String

        private val snippet: String

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String {
            return title
        }


        override fun getSnippet(): String {
            return snippet
        }

        init {
            position = LatLng(lat, lng)
            this.title = title
            this.snippet = snippet
        }
    }
}

