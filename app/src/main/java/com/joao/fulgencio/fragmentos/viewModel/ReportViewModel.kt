package com.joao.fulgencio.fragmentos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao.fulgencio.fragmentos.http.ktor.KtorClient
import com.joao.fulgencio.fragmentos.viewModel.event.Event
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import com.joao.fulgencio.fragmentos.adapter.Relatorio as AdapterRelatorio
class ReportViewModel: ViewModel() {
    data class Relatorio (val email: String = "", val dataDoPonto: String = "", val entrada: String = "", val saida: String = "")
    fun Relatorio.toAdapterRelatorio(): AdapterRelatorio {
        return AdapterRelatorio(email = this.email, dataDoPonto = this.dataDoPonto, entrada = this.entrada, saida = this.saida)
    }

    val reportSuccess = MutableLiveData<List<AdapterRelatorio>>()
    val errorEvent = MutableLiveData<Event<String>>()

    fun fetchReport(email : String?) {
        viewModelScope.launch {
            try {
                val response: HttpResponse = KtorClient.client.get("http://10.0.2.2:8080/relatorio/$email")
                response.takeIf { it.status == HttpStatusCode.OK }?.let {
                    val relatorios: List<Relatorio> = it.body()
                    val adapterRelatorios = relatorios.map { relatorio -> relatorio.toAdapterRelatorio() }
                    reportSuccess.postValue(adapterRelatorios)
                } ?: run {
                    errorEvent.postValue(Event("statusCode não foi 200"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorEvent.postValue(Event("Falha ao buscar"))
            }
        }
    }

}