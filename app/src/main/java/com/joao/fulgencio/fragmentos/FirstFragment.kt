package com.joao.fulgencio.fragmentos

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.joao.fulgencio.fragmentos.databinding.FragmentFirstBinding
import com.joao.fulgencio.fragmentos.viewModel.LoginViewModel


private const val SESSION_PREF = "session_pref"
private const val LOGGED_IN = "logged_in"
private const val LOGIN_TIME = "login_time"
private const val SESSION_DURATION = 15 * 60 * 1000 // 15 minutos em milissegundos
class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var sharedPreferences: SharedPreferences
    val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences(SESSION_PREF, Context.MODE_PRIVATE)

        if (isUserLoggedIn()) {
            navigateToSecondFragment()
        } else {
            configureLogin()
        }

        return _binding!!.root
    }

    private fun configureLogin() {
        _binding!!.btnFragamento2.setOnClickListener {
            val matricula = _binding!!.edtLogin.text.toString()
            val senha = _binding!!.edtPassword.text.toString()
            viewModel.login(matricula, senha)
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { success ->
                if (success) {
                    saveSessionData()
                    navigateToSecondFragment()
                } else {
                    Toast.makeText(context, "Matrícula ou senha inválidos", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    private fun isUserLoggedIn(): Boolean {
        val loggedIn = sharedPreferences.getBoolean(LOGGED_IN, false)
        val loginTime = sharedPreferences.getLong(LOGIN_TIME, 0)
        val currentTime = System.currentTimeMillis()

        return loggedIn && (currentTime - loginTime < SESSION_DURATION)
    }

    private fun saveSessionData() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOGGED_IN, true)
        editor.putLong(LOGIN_TIME, System.currentTimeMillis())
        editor.apply()
    }

    private fun navigateToSecondFragment() {
        val acao = FirstFragmentDirections.actionFirstFragmentToSecondFragment("João")
        findNavController().navigate(acao)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}