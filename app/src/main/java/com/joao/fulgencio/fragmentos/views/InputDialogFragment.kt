package com.joao.fulgencio.fragmentos.views

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joao.fulgencio.fragmentos.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InputDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
            datePicker.addOnPositiveButtonClickListener {
                notificationDateText.setText(datePicker.headerText)
            }
        }

        builder.setView(view)
            .setTitle("Preencha os dados das atividades do dia")
            .setPositiveButton("Salvar") { dialog, id ->
                val message = messageInput.text.toString()
                val notifyDate = notificationDateText.text.toString()

                // Retorna os dados usando setFragmentResult
                setFragmentResult(
                    "inputRequestKey",
                    Bundle().apply {
                        putString("message", message)
                        putString("notifyDate", notifyDate)
                        // Adicione mais dados conforme necessário
                    })
            }
            .setNegativeButton("Cancelar") { dialog, id ->
                dialog.cancel()
            }
        return builder.create()
    }
}