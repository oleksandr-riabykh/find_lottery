package com.limestudio.findlottery.presentation.ui.tricks

import com.limestudio.findlottery.data.models.Ticket

interface OnListTrickInteractionListener {
    fun onListFragmentInteraction(item: Ticket?)
}