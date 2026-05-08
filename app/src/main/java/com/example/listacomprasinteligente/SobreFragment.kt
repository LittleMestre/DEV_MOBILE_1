package com.example.listacomprasinteligente

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.listacomprasinteligente.databinding.FragmentSobreBinding

class SobreFragment : Fragment() {

    private var _binding: FragmentSobreBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CICLO", "SobreFragment - onCreate chamado")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("CICLO", "SobreFragment - onCreateView chamado")
        _binding = FragmentSobreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("CICLO", "SobreFragment - onResume chamado")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CICLO", "SobreFragment - onDestroyView chamado")
        _binding = null
    }
}
