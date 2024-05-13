package com.joao.fulgencio.fragmentos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao.fulgencio.fragmentos.http.ktor.KtorClient
import com.joao.fulgencio.fragmentos.viewModel.event.Event
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterViewModel : ViewModel() {
    val registerSuccess = MutableLiveData<Event<Boolean>>()

    fun register(cpf: String, email: String, nome: String, departamento: String, senha: String) {
        viewModelScope.launch {
            try {
                val response = KtorClient.client.post("http://10.0.2.2:8080/registrar") {
                    contentType(ContentType.Application.Json)
                    setBody(RegisterRequest(cpf, email, nome, departamento, senha))
                }
                registerSuccess.postValue(Event(response.status == HttpStatusCode.OK))
            } catch (e: Exception) {
                e.printStackTrace()
                registerSuccess.postValue(Event(false))
            }
        }
    }

    data class RegisterRequest(val cpf: String, val email: String, val nome: String, val departamento: String, val senha: String)
}
