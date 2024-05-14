package com.joao.fulgencio.fragmentos.views

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joao.fulgencio.fragmentos.R
import com.joao.fulgencio.fragmentos.viewModel.PointViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

class InputDialogFragment : DialogFragment() {
    private val viewModel by viewModels<PointViewModel>()
    private var day: String = ""
    private var month: String = ""
    private var year: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_dialog, null)

        val startTimePicker = view.findViewById<TimePicker>(R.id.startTimePicker)
        val endTimePicker = view.findViewById<TimePicker>(R.id.endTimePicker)
        val messageInput = view.findViewById<EditText>(R.id.messageInput)
        val notifyDatePicker = view.findViewById<Button>(R.id.notifyDatePicker)
        val notificationDateText = view.findViewById<EditText>(R.id.notificationDateText)
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
                viewModel.viewModelScope.launch {
                    viewModel.point(
                        "joaooctf@gmail.com",
                        day,
                        month,
                        year,
                        startTimePicker.hour.toString(),
                        endTimePicker.hour.toString(),
                        notifyDate,
                        message
                    )
                }
            }
            .setNegativeButton("Cancelar") { dialog, id ->
                dialog.cancel()
            }
        return builder.create()
    }


}