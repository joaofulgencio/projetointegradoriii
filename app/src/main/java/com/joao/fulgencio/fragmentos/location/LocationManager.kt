package com.joao.fulgencio.fragmentos.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationManager(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, // Prioridade para obter a localização precisa
        10_000 // Intervalo de 10 segundos para solicitações
    ).apply {
        setMinUpdateIntervalMillis(5_000) // Intervalo mínimo de 5 segundos para atualizações mais rápidas
    }.build()
    private var locationCallback: LocationCallback? = null

    fun getLocation(onLocationReceived: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    onLocationReceived(locationResult.lastLocation)
                }
            }

            locationCallback?.let {
                fusedLocationClient.requestLocationUpdates(locationRequest, it, null)
            }
        } else {
            onLocationReceived(null)
        }
    }

    fun stopLocationUpdates() {
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }

}