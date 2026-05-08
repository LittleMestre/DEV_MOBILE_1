package com.example.listacomprasinteligente

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.listacomprasinteligente.databinding.FragmentCarrinhoBinding

class CarrinhoFragment : Fragment() {

    private var _binding: FragmentCarrinhoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CICLO", "CarrinhoFragment - onCreate chamado")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("CICLO", "CarrinhoFragment - onCreateView chamado")
        _binding = FragmentCarrinhoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("CICLO", "CarrinhoFragment - onResume chamado")
        val cartLines = ProductRepository.getCartLines(requireContext())
        binding.textCartItems.text = if (cartLines.isEmpty()) {
            getString(R.string.cart_empty)
        } else {
            cartLines.joinToString(separator = "\n")
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("CICLO", "CarrinhoFragment - onPause chamado")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CICLO", "CarrinhoFragment - onDestroyView chamado")
        _binding = null
    }
}
