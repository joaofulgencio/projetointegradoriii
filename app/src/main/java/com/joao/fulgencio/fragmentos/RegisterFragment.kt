package com.joao.fulgencio.fragmentos

import android.os.Bundle
import android.se.omapi.Session
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.joao.fulgencio.fragmentos.databinding.FragmentRegisterBinding
import com.joao.fulgencio.fragmentos.session.SessionManager
import com.joao.fulgencio.fragmentos.viewModel.RegisterViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val viewModel by viewModels<RegisterViewModel>()
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        configureRegistration()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.supportActionBar?.title = "Cadastro"
    }

    private fun configureRegistration() {
        binding.btnCadastrar.setOnClickListener {
            val cpf = binding.edtCpf.text.toString()
            val email = binding.edtEmail.text.toString()
            val nome = binding.edtNome.text.toString()
            val departamento = binding.edtDepartamento.text.toString()
            val senha = binding.edtRegistrarPassword.text.toString()

            viewModel.register(cpf, email, nome, departamento, senha)
        }

        viewModel.registerSuccess.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { success ->
                if (success) {
                    SessionManager.saveSessionData()
                    Toast.makeText(context, "Registro realizado com sucesso", Toast.LENGTH_SHORT).show()
                    navigateToPunchFragment()
                } else {
                    Toast.makeText(context, "Erro ao registrar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToPunchFragment() {
        val acao = RegisterFragmentDirections.actionRegisterFragmentToPunchFragment()
        findNavController().navigate(acao)
    }

}