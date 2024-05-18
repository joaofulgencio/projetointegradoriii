package com.joao.fulgencio.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.joao.fulgencio.fragmentos.adapter.PunchAdapter
import com.joao.fulgencio.fragmentos.databinding.FragmentRegisterBinding
import com.joao.fulgencio.fragmentos.databinding.FragmentReportBinding
import com.joao.fulgencio.fragmentos.session.SessionManager
import com.joao.fulgencio.fragmentos.viewModel.ReportViewModel

class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PunchAdapter
    private val viewModel : ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.supportActionBar?.title = "Relatório de Ponto"
        adapter = PunchAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.reportSuccess.observe(viewLifecycleOwner, Observer { relatorios ->
            Log.i("ReportFragment", "Relatórios recebidos: $relatorios")
            relatorios?.let {
                adapter.relatorio.clear()
                adapter.relatorio.addAll(it)
                adapter.notifyDataSetChanged()
                if (relatorios.isEmpty()) {
                    binding.textViewTitle.text = "Nenhum ponto lançado"
                }
            }
        })

        viewModel.fetchReport(SessionManager.getEmail())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}