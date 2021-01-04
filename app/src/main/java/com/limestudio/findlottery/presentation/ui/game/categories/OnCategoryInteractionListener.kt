package com.limestudio.findlottery.presentation.ui.game.categories

import com.limestudio.findlottery.data.models.User

interface OnCategoryInteractionListener {
    fun onCategoryClicked(item: User)
}