package com.limestudio.findlottery.presentation.ui.tickets.draws

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.BuddhistCalendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.svprogresshud.SVProgressHUD
import com.limestudio.findlottery.R
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.extensions.showToast
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.extensions.showWinAlert
import com.limestudio.findlottery.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_draws.*
import java.util.*


const val SELECTED_DRAW = "selected_draw"

class DrawsFragment : BaseFragment() {
    private val viewModel: DrawsViewModel by viewModels { viewModelFactory }
    private lateinit var viewAdapter: DrawsAdapter
    private lateinit var hudSync: SVProgressHUD

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hudSync = SVProgressHUD(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_draws, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViews()
        initStateListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadData()
    }

    private fun initAdapter() {
        viewAdapter = DrawsAdapter({
            navigateTo(
                R.id.navigation_ticketsFragment,
                R.id.navigation_drawsFragment,
                false,
                Bundle().apply {
                    putParcelable(SELECTED_DRAW, it)
                    putString("title", it.date)
                }
            )
        }, { viewModel.deleteDraw(it) })
    }

    private fun initViews() {
        recycler.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        addDraw?.setOnClickListener {
            if (hudSync.isShowing) hudSync.dismiss()
            val c: android.icu.util.Calendar = BuddhistCalendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    viewModel.createFutureDraw(calendar.time)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.colorAccent, null))
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.colorAccent, null))
        }
    }

    private fun initStateListener() {
        viewModel.state.observe(this) { item ->
            when (item) {
                is DrawsState.OnLoadCompleted -> {
                    viewAdapter.setData(item.tickets)
                    hudSync.dismiss()
                }
                is DrawsState.OnDrawDeleted -> {
                    if (hudSync.isShowing) hudSync.dismiss()
                }
                is DrawsState.OnShowMessage -> {
                    showToast(item.error.messageId)
                    hudSync.dismiss()
                }
                is DrawsState.OnWinCombinationsReceived -> {
//                    NotificationManager().showDefaultWinNotification(requireContext())
                    showWinAlert()
                }
                is DrawsState.OnDrawCreated -> {
                    navigateTo(
                        R.id.navigation_ticketsFragment,
                        R.id.navigation_drawsFragment,
                        false,
                        Bundle().apply {
                            putParcelable(SELECTED_DRAW, item.draw)
                            putString("title", item.draw.date)
                        }
                    )
                }
                is DrawsState.ShowProgress -> {
                    if (item.shouldShow && !hudSync.isShowing) hudSync.show() else hudSync.dismiss()
                }

                else -> {
                    if (hudSync.isShowing) hudSync.dismiss()
                    showWarning(R.string.operation_not_implemented)
                }
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (hudSync.isShowing) hudSync.dismiss()
            showWarning(error.message)
        }
    }
}