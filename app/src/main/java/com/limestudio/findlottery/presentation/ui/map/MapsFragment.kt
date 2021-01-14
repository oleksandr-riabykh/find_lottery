package com.limestudio.findlottery.presentation.ui.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.extensions.showAlert
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.tickets.list.MODE_VIEW
import com.limestudio.findlottery.presentation.ui.tickets.list.TicketAdapter
import kotlinx.android.synthetic.main.bottom_sheet_map.*
import java.util.*


const val SELECTED_USER = "selected_user"

class MapsFragment : BaseFragment() {
    private val viewModel: MapsViewModel by viewModels { viewModelFactory }
    private lateinit var viewAdapter: TicketAdapter
    private var geoCoder: Geocoder? = null
    private val callback = OnMapReadyCallback { googleMap ->

        val bangkok = LatLng(13.756385, 100.502118)
        googleMap.addMarker(MarkerOptions().position(bangkok).title("Bangkok city"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bangkok))
        loadCityTickets(bangkok)
        googleMap.setOnCameraMoveListener {
            loadCityTickets(googleMap.cameraPosition.target)
        }
        googleMap.setOnInfoWindowClickListener { marker ->
//            navigateTo(
//                R.id.navigation_seller,
//                R.id.navigation_map,
//                false,
//                Bundle().apply {
//                    putString(SELECTED_USER, marker.title)
//                    putString("title", marker.title)
//                }
//            )

            requireActivity().showAlert(marker.title, "Do you want to contact the seller?") {

            }
        }
//        googleMap.isMyLocationEnabled = true // check location permission
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        geoCoder = Geocoder(context, Locale.getDefault())
    }

    var sheetBehavior: BottomSheetBehavior<*>? = null

    private fun loadCityTickets(location: LatLng) {
        if (Geocoder.isPresent()) {
            val addresses: List<Address>? =
                geoCoder?.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                Log.d("current_camera_location", ": $addresses")
                val city = addresses[0].locality ?: "bangkok"
                viewModel.loadAllCityTickets(city)
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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        // bottom sheet
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)

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
        mapFragment?.getMapAsync { map ->
            val builder = LatLngBounds.Builder()
            map.clear()
            users.forEach { user ->
                map.apply {
                    val latLng = LatLng(
                        user.location?.latitude ?: 0.0,
                        user.location?.longitude ?: 0.0
                    )
                    val position = MarkerOptions().position(
                        latLng
                    ).title("${user.name} ${user.lastName}")
                    builder.include(latLng)
                    addMarker(position)
                }
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 5, 5, 1))
                map.animateCamera(CameraUpdateFactory.zoomTo(5f))
            }
        }
    }
}