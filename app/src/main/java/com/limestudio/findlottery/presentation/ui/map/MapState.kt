package com.limestudio.findlottery.presentation.ui.map

import com.limestudio.findlottery.data.exceptions.ExceptionModel
import com.limestudio.findlottery.presentation.base.BaseScreenState


sealed class MapState : BaseScreenState() {
    data class ShowProgress(val shouldShow: Boolean) : MapState()
    data class OnShowMessage(val error: ExceptionModel) : MapState()
}