package com.joao.fulgencio.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.navArgs
import com.joao.fulgencio.fragmentos.databinding.FragmentSecondBinding
import com.joao.fulgencio.fragmentos.location.LocationManager
import com.joao.fulgencio.fragmentos.views.InputDialogFragment
import java.util.Calendar
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location

private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

class SecondFragment : Fragment() {
    var _binding: FragmentSecondBinding? = null
    val binding get() = _binding!!
    private val args: SecondFragmentArgs by navArgs()
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val referenceLatitude : Double = -22.8341535
        val referenceLongitude : Double = -47.0528798
        super.onCreate(savedInstanceState)
        locationManager = LocationManager(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            locationManager.getLocation { location ->
                if (location != null ) {
                    val distance = calculateDistance(
                        location.latitude,
                        location.longitude,
                        referenceLatitude,
                        referenceLongitude)
                    if (distance <= 3) {
                        Toast.makeText(context, "Você está dentro do raio de 3 metros", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Você está fora do raio de 3 metros", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "deu ruim, meu chapa", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
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

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val result = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, result)
        return result[0] // Distance in meters
    }

}