package com.limestudio.findlottery.ads

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.lottery.scanner.ads.IAdsManager

class AdsManager
private constructor() : IAdsManager {
    private var interstitialGoogleAd: InterstitialAd? = null

    //region Public Interface
    override fun destroyGoogleAd() {
        interstitialGoogleAd = null
    }

    override fun loadGoogleAd(listener: AdListener?) {
        if (interstitialGoogleAd != null) {
            interstitialGoogleAd?.adListener = listener
            interstitialGoogleAd?.loadAd(AdRequest.Builder().build())
        } else {
            listener?.onAdFailedToLoad(INIT_ADD_ERROR_CODE)
        }
    }

    override fun showGoogleAd() {
        try {
            interstitialGoogleAd?.show()
        } catch (ex: Exception) {
        }
    }

    private fun initGoogleInterstitial(context: Context) {
        interstitialGoogleAd = InterstitialAd(context)
        interstitialGoogleAd?.setAdUnitId(GOOGLE_ADS_INTERSTITIAL)
    }


    //endregion
    //region Builder
    inner class Builder() {
        fun initGoogleAd(context: Context): Builder {
            MobileAds.initialize(context) {}
            initGoogleInterstitial(context)
            return this
        }

        fun build(): AdsManager {
            return this@AdsManager
        }
    }

    companion object {
        private const val INIT_ADD_ERROR_CODE = -1
        private const val GOOGLE_ADS_INTERSTITIAL = "ca-app-pub-2875545079088709/6779447998"
        fun newBuilder(): Builder {
            return AdsManager().Builder()
        }
    }
    //endregion Builder
}