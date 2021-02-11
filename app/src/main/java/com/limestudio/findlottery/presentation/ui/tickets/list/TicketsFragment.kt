package com.limestudio.findlottery.presentation.ui.tickets.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.svprogresshud.SVProgressHUD
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.limestudio.findlottery.R
import com.limestudio.findlottery.ads.AdsManager
import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.presentation.base.BaseDialogFragment
import com.limestudio.findlottery.presentation.ui.profile.ARG_SELECTED_USER_NAME
import com.limestudio.findlottery.presentation.ui.profile.ARG_VIEW_MODE
import com.limestudio.findlottery.presentation.ui.tickets.add.AddTicketActivity
import com.limestudio.findlottery.presentation.ui.tickets.draws.SELECTED_DRAW
import kotlinx.android.synthetic.main.tickets_fragment.*

class TicketsFragment : BaseDialogFragment() {

    private val viewModel: TicketsViewModel by viewModels { viewModelFactory }

    private lateinit var viewAdapter: TicketAdapter
    private lateinit var hudSync: SVProgressHUD

    companion object {
        fun newInstance(bundle: Bundle) = TicketsFragment().apply {
            arguments = bundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tickets_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewAdapter = TicketAdapter(
            if (arguments?.getBoolean(ARG_VIEW_MODE) == true) MODE_VIEW else MODE_EDIT,
            { },
            { viewModel.deleteTicket(it) })
        hudSync = SVProgressHUD(requireContext())
        recycler.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        initStateListener()
        val userName = arguments?.getString(ARG_SELECTED_USER_NAME)
        if (arguments?.getBoolean(ARG_VIEW_MODE) == true) addTicket.visibility = View.GONE
        if (userName != null && userName.isNotEmpty()) viewAdapter.setUserName(userName)
        addTicket?.setOnClickListener {
            viewModel.checkInterstitial()
        }
    }

    override fun onStart() {
        super.onStart()
        arguments?.getParcelable<Draw>(SELECTED_DRAW)?.let {
            viewModel.loadData(it)
        }
            ?: showWarning(R.string.no_date_argument)
    }

    private fun initStateListener() {
        viewModel.state.observe(this) { item ->
            when (item) {
                is TicketsState.OnLoadCompleted -> {
                    viewAdapter.setData(item.tickets)
                }
                is TicketsState.OnTicketDeleted -> {
                }
                is TicketsState.OnShowMessage -> {
                    showWarning(item.error.message)
                }
                is TicketsState.ShowInterstitial -> {
                    if (item.shouldShow) loadInterstitial() else AddTicketActivity.start(
                        requireActivity(),
                        arguments?.getParcelable(SELECTED_DRAW)
                    )
                }
                else -> showWarning(R.string.operation_not_implemented)
            }
        }
        viewModel.error.observe(viewLifecycleOwner, { error ->
            showWarning(error.message)
        })
    }

    private fun loadInterstitial() {
        if (!hudSync.isShowing) hudSync.show()
        val manager = AdsManager.newBuilder().initGoogleAd(requireContext()).build()
        manager.loadGoogleAd(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                if (hudSync.isShowing) hudSync.dismiss()
                manager.showGoogleAd()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                if (hudSync.isShowing) hudSync.dismiss()
                AddTicketActivity.start(requireActivity(), arguments?.getParcelable(SELECTED_DRAW))
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                if (hudSync.isShowing) hudSync.dismiss()
                AddTicketActivity.start(requireActivity(), arguments?.getParcelable(SELECTED_DRAW))
            }
        })
    }
}