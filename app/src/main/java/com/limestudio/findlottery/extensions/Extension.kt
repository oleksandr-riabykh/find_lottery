package com.limestudio.findlottery.extensions

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.limestudio.findlottery.R
import com.limestudio.findlottery.constants.DATE_FORMAT
import com.prathameshmore.toastylibrary.Toasty
import kotlinx.android.synthetic.main.notification_alert.view.*
import java.text.SimpleDateFormat
import java.util.*

fun RecyclerView.ViewHolder.showAlert(
    messageId: Int,
    positiveId: Int,
    negativeId: Int,
    onClickOk: () -> Unit,
    onClickNo: () -> Unit
) {
    val dialogClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                onClickOk()
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                onClickNo()
            }
        }
    }

    val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
    val dialog = builder.setMessage(messageId).setPositiveButton(positiveId, dialogClickListener)
        .setNegativeButton(negativeId, dialogClickListener)
        .create()
    dialog.show()
    dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
        .setTextColor(itemView.context.resources.getColor(R.color.colorAccent, null))
    dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        .setTextColor(itemView.context.resources.getColor(R.color.colorAccent, null))
}

fun Date.toDateFormat(locale: Locale? = null): String {
    val sdf = SimpleDateFormat(DATE_FORMAT, locale ?: Locale.getDefault())
    return sdf.format(this)
}

fun String.toDateFormat(locale: Locale? = null) =
    SimpleDateFormat(DATE_FORMAT, locale ?: Locale.getDefault()).parse(this)

fun Activity.showToast(textToShow: String) {
    Toasty(this).primaryToasty(this, textToShow, Toasty.LENGTH_SHORT, Toasty.CENTER);
}

fun Activity.showToast(textToShowId: Int) {
    Toasty(this).primaryToasty(this, getString(textToShowId), Toasty.LENGTH_SHORT, Toasty.CENTER);
}

fun Activity.showWarning(textToShow: String) {
    Toasty(this).warningToasty(this, textToShow, Toasty.LENGTH_SHORT, Toasty.CENTER);
}

fun Activity.showWarning(textToShowId: Int) {
    Toasty(this).warningToasty(this, getString(textToShowId), Toasty.LENGTH_SHORT, Toasty.CENTER);
}

fun Fragment.showToast(textToShow: String) {
    Toasty(requireContext()).primaryToasty(
        requireContext(),
        textToShow,
        Toasty.LENGTH_SHORT,
        Toasty.CENTER
    );
}

fun Fragment.showToast(textToShowID: Int) {
    Toasty(requireContext()).primaryToasty(
        requireContext(),
        getString(textToShowID),
        Toasty.LENGTH_SHORT,
        Toasty.CENTER
    );
}

fun Fragment.showWarning(textToShow: String) {
    Toasty(requireContext()).warningToasty(
        requireContext(),
        textToShow,
        Toasty.LENGTH_SHORT,
        Toasty.CENTER
    );
}

fun Fragment.showWarning(textToShowId: Int) {
    Toasty(requireContext()).warningToasty(
        requireContext(),
        getString(textToShowId),
        Toasty.LENGTH_SHORT,
        Toasty.CENTER
    );
}

fun Fragment.showWinAlert() {
    val builder = AlertDialog.Builder(requireContext())
    val customLayout: View = layoutInflater.inflate(R.layout.win_notification, null)
    builder.setView(customLayout).create().show()
}

fun Activity.showDatMatrixAlert(onClickOk: () -> Unit) {
    val builder = AlertDialog.Builder(this)
    val customLayout: View = layoutInflater.inflate(R.layout.notification_scanner_hint, null)
    builder.setView(customLayout)
    builder.setPositiveButton(android.R.string.ok) { _, _ ->
        onClickOk()
    }
    val alert = builder.create()
    alert.show()
    alert.getButton(DatePickerDialog.BUTTON_POSITIVE)
        .setTextColor(resources.getColor(R.color.colorAccent, null))
    alert.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        .setTextColor(resources.getColor(R.color.colorAccent, null))
}

fun Activity.showAlert(title: String, body: String, onClickOk: () -> Unit) {
    val builder = AlertDialog.Builder(this)
    val customLayout: View = layoutInflater.inflate(R.layout.notification_alert, null)
    customLayout.textView?.text = title
    customLayout.body?.text = body
    builder.setView(customLayout)
    builder.setPositiveButton(android.R.string.ok) { _, _ ->
        onClickOk()
    }
    val alert = builder.create()
    alert.show()
    alert.getButton(DatePickerDialog.BUTTON_POSITIVE)
        .setTextColor(resources.getColor(R.color.colorAccent, null))
    alert.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        .setTextColor(resources.getColor(R.color.colorAccent, null))
}

fun Date.isDayBeforeFuture(): Boolean {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    return this.after(calendar.time)
}


fun Date.upcomingStatus(): Int {
    return if (this.after(Date())) R.string.upcoming_t else 0
}


fun View.hideKeyboard() {
    val imm: InputMethodManager =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}