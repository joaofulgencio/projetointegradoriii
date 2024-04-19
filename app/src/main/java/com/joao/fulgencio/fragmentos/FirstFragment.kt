package com.joao.fulgencio.fragmentos

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


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val viewModel by viewModels<LoginViewModel>()
    val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
//        val bundle = Bundle()
//        bundle.putString("example", "João")
//        _binding!!.btnFragamento2.setOnClickListener {
//            findNavController().navigate(R.id.action_firstFragment_to_secondFragment, bundle)
//        }
        param()
        return _binding!!.root
    }

    private fun param() {
        val acao = FirstFragmentDirections.actionFirstFragmentToSecondFragment("João")
        _binding!!.btnFragamento2.setOnClickListener{
            val matricula = _binding!!.edtLogin.text.toString()
            val senha = _binding!!.edtPassword.text.toString()
            viewModel.login(matricula, senha)
        }
        viewModel.loginSuccess.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                success ->
                    if (success == true) {
                        findNavController().navigate(acao)
                    } else {
                        Toast.makeText(context, "Matrícula ou senha inválidos", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            FirstFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}