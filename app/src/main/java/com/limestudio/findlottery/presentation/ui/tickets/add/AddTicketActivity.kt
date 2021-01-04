package com.limestudio.findlottery.presentation.ui.tickets.add

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.extensions.showDatMatrixAlert
import com.limestudio.findlottery.extensions.showToast
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.extensions.toDateFormat
import com.limestudio.findlottery.presentation.Injection
import com.limestudio.findlottery.presentation.ui.tickets.draws.SELECTED_DRAW
import kotlinx.android.synthetic.main.activity_add_ticket.*
import kotlinx.android.synthetic.main.activity_add_ticket.set
import java.util.*

class AddTicketActivity : AppCompatActivity() {

    private val viewModel: AddTicketViewModel by viewModels { Injection.provideViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ticket)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initViews()
        initUIListeners()
        initStateListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                showToast(R.string.scan_cancelled)
            } else {
                parseScanResult(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initViews() {
        title = Date(
            intent.extras?.getParcelable<Draw>(SELECTED_DRAW)?.timestamp
                ?: System.currentTimeMillis()
        ).toDateFormat()
    }

    @SuppressLint("SetTextI18n")
    private fun initUIListeners() {
        saveButton.setOnClickListener {
            val draw = intent.extras?.getParcelable<Draw>(SELECTED_DRAW)
            viewModel.saveTicket(
                Ticket(
                    userId = Firebase.auth.currentUser?.uid ?: "",
                    drawId = draw?.id ?: "",
                    timestamp = draw?.timestamp ?: System.currentTimeMillis(),
                    date = draw?.date ?: "00",
                    numbers = ticketNumber?.text?.toString() ?: "",
                    set = set?.text?.toString() ?: "",
                    progress = progress?.text?.toString() ?: ""
                )
            )
        }
        scanButton.setOnClickListener {
            showDatMatrixAlert {
                val integrator = IntentIntegrator(this)
                integrator.setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX)
                integrator.setOrientationLocked(false)
                integrator.setPrompt(getString(R.string.scanner_prompt))
                integrator.initiateScan()
            }
        }
        set?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validateSet(s?.toString())
            }
        })
        ticketNumber?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validateTicketNumber(s?.toString())
            }
        })
        progress?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validateGroup(s?.toString())
            }
        })
    }

    private fun parseScanResult(contents: String) {
        val splicedString = contents.split("-")
        if (splicedString.size != 4) {
            showWarning(R.string.incorrect_qr)
            return
        }
        set?.setText(splicedString[1])
        progress?.setText(splicedString[2])
        ticketNumber?.setText(splicedString[3])
    }

    private fun initStateListener() {
        viewModel.state.observe(this) { item ->
            when (item) {
                is AddTicketState.OnSaveSuccess -> {
                    finish()
                }
                is AddTicketState.ShowMessageError -> {
                    showWarning(item.exception.messageId)
                }
                is AddTicketState.InputNumberValid -> {
                    ticketNumber.error =
                        if (!item.isValid) "Input is not valid, must have 6 digits " else null
                }
                is AddTicketState.InputGroupValid -> {
                    progress.error =
                        if (!item.isValid) "Input is not valid, must have 2 digits " else null
                }
                is AddTicketState.InputSetValid -> {

                    set.error =
                        if (!item.isValid) "Input is not valid, must have 2 digits " else null
                }
                else -> showWarning(R.string.operation_not_implemented)
            }
        }
    }

    companion object {
        fun start(context: Context, draw: Draw?) {
            val intent = Intent(context, AddTicketActivity::class.java)
            intent.putExtra(SELECTED_DRAW, draw)
            context.startActivity(intent)
        }
    }
}