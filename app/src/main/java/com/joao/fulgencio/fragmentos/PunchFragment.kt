package com.joao.fulgencio.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.joao.fulgencio.fragmentos.location.LocationManager
import com.joao.fulgencio.fragmentos.views.InputDialogFragment
import java.util.Calendar
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import com.joao.fulgencio.fragmentos.databinding.FragmentPunchBinding
import com.joao.fulgencio.fragmentos.viewModel.PointViewModel

private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
private const val REQUEST_POST_NOTIFICATIONS = 1002

class PunchFragment : Fragment() {
    var _binding: FragmentPunchBinding? = null
    val binding get() = _binding!!
    private lateinit var locationManager: LocationManager
    private var toastShown = false
    private val viewModel by viewModels<PointViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val referenceLatitude : Double = -22.8341535
        val referenceLongitude : Double = -47.0528798
        super.onCreate(savedInstanceState)
        // Cria uma nova transição de entrada para o eixo X para o forward
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        // Cria uma nova transição de retorno para o eixo X para o backward
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

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
                    if (distance <= 3000000000000) {
                        showToastOnce("Você está dentro do raio de 3 metros")
                    } else {
                        showToastOnce("Você está fora do raio de 3 metros")
                        disableCalendarView()
                    }
                } else {
                    Toast.makeText(context, "deu ruim, meu chapa", Toast.LENGTH_SHORT).show()
                    disableCalendarView()
                }
            }
        }
    }

    private fun showToastOnce(message: String) {
        // Mostra o toast apenas se ainda não foi mostrado
        if (!toastShown) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            toastShown = true // Atualiza a flag para evitar futuros toasts
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPunchBinding.inflate(inflater, container, false)
        val calendarView = binding.calendarView

        // Verifique a permissão para notificações
        checkNotificationPermission()

        calendarView.setOnDateChangeListener{ view, year, month, day ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            val inputDialog = InputDialogFragment()
            inputDialog.show(childFragmentManager, "inputDialog")
        }

        viewModel.pointSuccess.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { success ->
                if (success) {
                    Toast.makeText(context, "Registro de ponto realizado com sucesso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Erro ao registrar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    return binding.root
    }

    // Verifica e solicita a permissão se necessário
    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_POST_NOTIFICATIONS)
        }
    }

    // Gerencia a resposta da permissão
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(requireContext(), "Permissão de notificações não concedida. Notificações desativadas.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun disableCalendarView() {
        binding.calendarView.setOnDateChangeListener(null)
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
        (activity as? MainActivity)?.supportActionBar?.title = "Bater Ponto"
    }

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val result = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, result)
        return result[0] // Distance in meters
    }


    override fun onDestroyView() {
        super.onDestroyView()
        locationManager.stopLocationUpdates()
        _binding = null
    }
}