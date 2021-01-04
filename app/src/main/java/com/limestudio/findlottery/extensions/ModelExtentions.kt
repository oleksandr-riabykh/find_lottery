package com.limestudio.findlottery.extensions

import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.data.models.DrawResponse
import com.limestudio.findlottery.data.models.Ticket
import java.util.*

fun Draw.getWinCombinationList(ticket: Ticket): List<Int> {
    val winList = arrayListOf<Int>()
    if (rawData.isEmpty()) return winList
    val responseData = DrawResponse(rawData)
    if (responseData.prize1.contains(ticket.numbers)) winList.add(R.string.won_prize_one_t)
    if (responseData.prize1Close.contains(ticket.numbers)) winList.add(R.string.won_prize_one_close_t)
    if (responseData.prize2.contains(ticket.numbers)) winList.add(R.string.won_prize_two_t)
    if (responseData.prize3.contains(ticket.numbers)) winList.add(R.string.won_prize_three_t)
    if (responseData.prize4.contains(ticket.numbers)) winList.add(R.string.won_prize_four_t)
    if (responseData.prize5.contains(ticket.numbers)) winList.add(R.string.won_prize_five_t)
    if (responseData.prizeFirst3.contains(
            ticket.numbers.substring(
                0,
                3
            )
        )
    ) winList.add(R.string.won_prize_first_three_t)
    if (responseData.prizeLast2.contains(
            ticket.numbers.substring(
                4,
                6
            )
        )
    ) winList.add(R.string.won_prize_last_2_t)
    if (responseData.prizeLast3.contains(
            ticket.numbers.substring(
                3,
                6
            )
        )
    ) winList.add(R.string.won_prize_last_3_t)
    return winList
}

fun Ticket.hasWinCombination(draw: Draw): Boolean {
    if (Date(this.timestamp).after(Date())) return false
    val drawResult = DrawResponse(draw.rawData)
    if (drawResult.prize1.contains(this.numbers)) return true
    if (drawResult.prize1Close.contains(this.numbers)) return true
    if (drawResult.prize2.contains(this.numbers)) return true
    if (drawResult.prize3.contains(this.numbers)) return true
    if (drawResult.prize4.contains(this.numbers)) return true
    if (drawResult.prize5.contains(this.numbers)) return true
    if (drawResult.prizeFirst3.contains(this.numbers.substring(0, 3))) return true
    if (drawResult.prizeLast2.contains(this.numbers.substring(4, 6))) return true
    return drawResult.prizeLast3.contains(this.numbers.substring(3, 6))
}

fun Ticket.isTicketWon(): Boolean = status?.isNotEmpty() == true
