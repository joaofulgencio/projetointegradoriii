package com.joao.fulgencio.fragmentos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joao.fulgencio.fragmentos.databinding.PunchItemBinding

data class Relatorio(val email: String = "", val dataDoPonto: String = "", val entrada: String = "", val saida: String = "")

class PunchAdapter(val relatorio: MutableList<Relatorio>) : RecyclerView.Adapter<PunchAdapter.PunchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PunchViewHolder {
        val binding = PunchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PunchViewHolder(binding)
    }

    override fun getItemCount(): Int = relatorio.size

    override fun onBindViewHolder(holder: PunchViewHolder, position: Int) {
        holder.bind(relatorio[position])
    }
    inner class PunchViewHolder(val binding: PunchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(relatorio: Relatorio) {
            with(relatorio) {
                binding.txtIvEmail.text = "Email: $email"
                binding.txtDataDoPonto.text = "Data do ponto: $dataDoPonto"
                binding.txtEntrada.text = "Hora de entrada: $entrada"
                binding.txtSaida.text = "Hora de saída: $saida"
            }
        }
    }
}

