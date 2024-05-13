package com.joao.fulgencio.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.joao.fulgencio.fragmentos.databinding.FragmentLoginBinding
import com.joao.fulgencio.fragmentos.session.SessionManager
import com.joao.fulgencio.fragmentos.viewModel.LoginViewModel


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val viewModel by viewModels<LoginViewModel>()
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Cria uma nova transição de entrada para o eixo X para o forward
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        // Cria uma nova transição de retorno para o eixo X para o backward
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.supportActionBar?.title = "Home Page"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        if (SessionManager.isUserLoggedIn()) {
            navigateToPunchFragment()
        } else {
            configureLogin()
            configureCadastrar()
        }

        return binding.root
    }

    private fun configureLogin() {
        _binding!!.btnLogin.setOnClickListener {
            val matricula = _binding!!.edtLoginEmail.text.toString()
            val senha = _binding!!.edtLoginPassword.text.toString()
            viewModel.login(matricula, senha)
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { success ->
                if (success) {
                    SessionManager.saveSessionData()
                    navigateToPunchFragment()
                } else {
                    Toast.makeText(context, "Matrícula ou senha inválidos", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun configureCadastrar() {
        _binding!!.btnCadastrar.setOnClickListener {
            navigateToRegisterFragment()
        }
    }

    private fun navigateToPunchFragment() {
        val acao = LoginFragmentDirections.actionLoginFragmentToPunchFragment()
        findNavController().navigate(acao)
    }

    private fun navigateToRegisterFragment() {
        val acao = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(acao)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}