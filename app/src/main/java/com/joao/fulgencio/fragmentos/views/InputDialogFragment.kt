package com.joao.fulgencio.fragmentos.views

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.joao.fulgencio.fragmentos.R
import com.joao.fulgencio.fragmentos.session.SessionManager
import com.joao.fulgencio.fragmentos.viewModel.PointViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

class InputDialogFragment : DialogFragment() {
    private val viewModel by viewModels<PointViewModel>()
    private var day: String = ""
    private var month: String = ""
    private var year: String = ""
    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker
    private lateinit var messageInput: EditText
    private lateinit var notificationDateText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_dialog, null)

        startTimePicker = view.findViewById(R.id.startTimePicker)
        endTimePicker = view.findViewById(R.id.endTimePicker)
        messageInput = view.findViewById(R.id.messageInput)
        val notifyDatePicker = view.findViewById<Button>(R.id.notifyDatePicker)
        notificationDateText = view.findViewById(R.id.notificationDateText)
        notifyDatePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(childFragmentManager, "datePicker")
            datePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance(TimeZone.getDefault())
                calendar.timeInMillis = selection

                year = calendar.get(Calendar.YEAR).toString()
                month = (calendar.get(Calendar.MONTH) + 1).toString()
                day = calendar.get(Calendar.DAY_OF_MONTH).toString()
                notificationDateText.setText(datePicker.headerText)
                notifyDatePicker.text = "Data selecionada: " + datePicker.headerText
            }
        }

        builder.setView(view)
            .setTitle("Bater Ponto")
            .setPositiveButton("Salvar") { dialog, id ->
                val message = messageInput.text.toString()
                val notifyDate = notificationDateText.text.toString()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !viewModel.hasExactAlarmPermission()) {
                    requestExactAlarmPermission()
                } else {
                    schedulePoint()
                }
            }
            .setNegativeButton("Cancelar") { dialog, id ->
                dialog.cancel()
            }
        return builder.create()
    }

    private fun schedulePoint() {
        val message = messageInput.text.toString()
        val notifyDate = notificationDateText.text.toString()
        val horaEntrada = formatTime(startTimePicker)
        val horaSaida = formatTime(endTimePicker)
        val data = formatDate(day.toInt(), month.toInt(), year.toInt())
        lifecycleScope.launch {
            viewModel.point(
                SessionManager.getEmail(),
                data,
                horaEntrada,
                horaSaida,
                notifyDate,
                message
            )
        }
    }


    fun formatTime(time: TimePicker): String {
        return String.format("%02d:%02d", time.hour, time.minute)
    }

    fun formatDate(dia: Int, mes: Int, ano: Int): String {
        return String.format("%02d/%02d/%04d", dia, mes, ano)
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivityForResult(intent, REQUEST_EXACT_ALARM_PERMISSION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EXACT_ALARM_PERMISSION) {
            lifecycleScope.launch {
                if (viewModel.hasExactAlarmPermission()) {
                    schedulePoint()
                } else {
                    Snackbar.make(
                        requireView(),
                        "Permissão necessária para agendar notificações exatas não concedida",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_EXACT_ALARM_PERMISSION = 1001
    }
}