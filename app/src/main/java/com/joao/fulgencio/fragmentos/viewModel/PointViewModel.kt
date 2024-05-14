package com.joao.fulgencio.fragmentos.viewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao.fulgencio.fragmentos.http.ktor.KtorClient
import com.joao.fulgencio.fragmentos.receiver.AlarmReceiver
import com.joao.fulgencio.fragmentos.viewModel.event.Event
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PointViewModel(application: Application) : AndroidViewModel(application) {
    val pointSuccess = MutableLiveData<Event<Boolean>>()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun point(email: String, dia: String, mes: String, ano: String,  entrada: String, saida: String, notifyDate: String, message: String) {
        val job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Main + job)


        scope.launch {
            try {
                // Agendar notificação
                withContext(Dispatchers.IO) {
                    scheduleNotification(getApplication(), notifyDate, 0, message)
                }
                // Bater ponto
                val response = withContext(Dispatchers.IO) {
                    Log.d("PointViewModel", "Iniciando chamada para API com email: $email, entrada: $entrada, saida: $saida")
                    postPoint(email, dia, mes, ano, entrada, saida)
                }
                Log.d("PointViewModel", "Resposta da API recebida: ${response.status}")

                pointSuccess.postValue(Event(response.status == HttpStatusCode.OK))
            } catch (e: Exception) {
                Log.e("PointViewModel", "Erro ao bater ponto: ${e.message}")
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    pointSuccess.postValue(Event(false))
                }
            }
        }

        job.invokeOnCompletion { throwable ->
            if (throwable != null) {
                Log.e("PointViewModel", "Job foi cancelado: ${throwable.message}", throwable)
            }
        }
    }

    private suspend fun postPoint(email: String, dia: String, mes: String, ano: String,  entrada: String, saida: String) : HttpResponse {
         return KtorClient.client.post("http://10.0.2.2:8080/baterPonto") {
            contentType(ContentType.Application.Json)
            setBody(BaterPonto(email, dia, mes, ano, entrada, saida))
        }
    }

    data class BaterPonto(val email: String, val dia: String, val mes: String, val ano: String, val entrada: String, val saida: String)



    private fun scheduleNotification(
        context: Context,
        notifyDateString: String,
        triggerTime: Long,
        message: String
    ) {
        val simpleDate = SimpleDateFormat("dd 'de' MMM 'de' yyyy", Locale.getDefault())
        val notifyDate = simpleDate.parse(notifyDateString)
        val calendar = Calendar.getInstance()
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("message", message)
        val alarmIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        if (triggerTime != 0L) {
            if (notifyDate != null) {
                calendar.time = notifyDate
                calendar.set(Calendar.HOUR_OF_DAY, 8)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val triggerTimeMillis = calendar.timeInMillis
                alarmManager?.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, alarmIntent)
            }
        } else {
            val triggerTimeMillis = System.currentTimeMillis() + 10 * 1000
            alarmManager?.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, alarmIntent)
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
