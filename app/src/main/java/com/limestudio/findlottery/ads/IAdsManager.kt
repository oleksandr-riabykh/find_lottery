package com.lottery.scanner.ads

import com.google.android.gms.ads.AdListener

interface IAdsManager {
    fun destroyGoogleAd()
    fun loadGoogleAd(listener: AdListener?)
    fun showGoogleAd()
}