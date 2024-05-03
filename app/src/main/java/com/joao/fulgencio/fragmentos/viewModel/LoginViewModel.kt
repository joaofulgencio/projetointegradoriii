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

class LoginViewModel() : ViewModel() {
val loginSuccess = MutableLiveData<Event<Boolean>>()

    fun login(matricula: String, senha: String) {
        viewModelScope.launch {
            try {
//                val response = KtorClient.client.post("http://172.16.228.154:8080/login") {
//                    contentType(ContentType.Application.Json)
//                    setBody(LoginRequest(matricula, senha))
//                }
                loginSuccess.postValue(Event(HttpStatusCode.OK == HttpStatusCode.OK))
            } catch (e: Exception) {
                e.printStackTrace()
                loginSuccess.postValue(Event(false))
            }
        }
    }

    data class LoginRequest(val matricula: String, val senha: String)

}