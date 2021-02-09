package com.limestudio.findlottery.presentation.ui.language

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.limestudio.findlottery.R
import kotlinx.android.synthetic.main.dialog_language.*

class LanguageDialog : DialogFragment() {
    private var languageCallback: LanguageCallback? = null
    private fun setLanguageCallback(languageCallback: LanguageCallback) {
        this.languageCallback = languageCallback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        isCancelable = true
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_language, container, false)
        dialog?.window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        language_thai?.setOnClickListener {
            languageCallback?.onLanguage(THAI_CODE)
            dismissAllowingStateLoss()
        }
        language_english?.setOnClickListener {
            languageCallback?.onLanguage(ENGLISH_CODE)
            dismissAllowingStateLoss()
        }
        language_cancel?.setOnClickListener { dismissAllowingStateLoss() }
    }

    interface LanguageCallback {
        fun onLanguage(languageCode: String)
    }

    companion object {
        const val ENGLISH_CODE = "en"
        const val THAI_CODE = "th"
        fun start(
            fragmentManager: FragmentManager, tag: String?,
            languageCallback: LanguageCallback
        ) {
            val languageDialog = LanguageDialog()
            languageDialog.setLanguageCallback(languageCallback)
            languageDialog.show(fragmentManager, tag)
        }
    }
}