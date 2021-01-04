package com.limestudio.findlottery.presentation.ui.tickets.draws

import com.limestudio.findlottery.data.exceptions.ExceptionModel
import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.presentation.base.BaseScreenState


sealed class DrawsState : BaseScreenState() {
    data class OnLoadCompleted(val tickets: List<Draw>) : DrawsState()
    data class OnDrawCreated(val draw: Draw) : DrawsState()
    data class OnWinCombinationsReceived(val winText: String) : DrawsState()
    data class OnDrawDeleted(val draw: Draw) : DrawsState()
    data class ShowProgress(val shouldShow: Boolean) : DrawsState()
    data class OnShowMessage(val error: ExceptionModel) : DrawsState()
}