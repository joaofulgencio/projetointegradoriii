package com.joao.fulgencio.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.joao.fulgencio.fragmentos.databinding.FragmentSecondBinding
import com.joao.fulgencio.fragmentos.views.InputDialogFragment
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SecondFragment : Fragment() {
    var _binding: FragmentSecondBinding? = null
    val binding get() = _binding!!
    private val args: SecondFragmentArgs by navArgs()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val calendarView = binding.calendarView
        calendarView.setOnDateChangeListener{ view, year, month, day ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            val inputDialog = InputDialogFragment()
            inputDialog.show(childFragmentManager, "inputDialog")
        }


    return binding.root
    }

    private fun isDateInCurrentWeek(date: Calendar): Boolean {
        val currentWeek = Calendar.getInstance()
        val startOfWeek = Calendar.getInstance()
        startOfWeek.set(Calendar.DAY_OF_WEEK, currentWeek.firstDayOfWeek)
        val endOfWeek = Calendar.getInstance()
        endOfWeek.set(Calendar.DAY_OF_WEEK, currentWeek.firstDayOfWeek)
        endOfWeek.add(Calendar.DAY_OF_MONTH, 6) // Last day of the week

        return date.after(startOfWeek) && date.before(endOfWeek)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SecondFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}