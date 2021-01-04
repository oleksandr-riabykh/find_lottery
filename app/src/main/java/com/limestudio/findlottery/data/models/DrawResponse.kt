package com.limestudio.findlottery.data.models

import org.json.JSONObject
import java.security.Timestamp

data class DrawResponse(val jsonString: String) {

    var id: Int = 0
    var prize1: String = ""
    var prize1Close: String = ""
    var prize2: String = ""
    var prize3: String = ""
    var prize4: String = ""
    var prize5: String = ""
    var prizeFirst3: String = ""
    var prizeLast2: String = ""
    var prizeLast3: String = ""
    var timestamp: Timestamp? = null

    init {
        if (jsonString.isNotEmpty()) {
            try {
                val jsonObject = JSONObject(jsonString)
                prize1 = jsonObject.getString("prize1")
                prize1Close = jsonObject.getString("prize1Close")
                prize2 = jsonObject.getString("prize2")
                prize3 = jsonObject.getString("prize3")
                prize4 = jsonObject.getString("prize4")
                prize5 = jsonObject.getString("prize5")
                prizeFirst3 = jsonObject.getString("prizeFirst3")
                prizeLast2 = jsonObject.getString("prizeLast2")
                prizeLast3 = jsonObject.getString("prizeLast3")
            } catch (e: Exception) {
                // case when field is missing, will be handled on view model
            }
        }
    }
}