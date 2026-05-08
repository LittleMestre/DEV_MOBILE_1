package com.example.listacomprasinteligente

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.listacomprasinteligente.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CICLO", "MainActivity - onCreate chamado")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ProductRepository.initialize(this)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_products -> {
                    replaceFragment(ProdutosFragment())
                    true
                }
                R.id.nav_cart -> {
                    replaceFragment(CarrinhoFragment())
                    true
                }
                R.id.nav_about -> {
                    replaceFragment(SobreFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_products
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        Log.d("CICLO", "MainActivity - onStart chamado")
    }

    override fun onResume() {
        super.onResume()
        Log.d("CICLO", "MainActivity - onResume chamado")
    }

    override fun onPause() {
        super.onPause()
        Log.d("CICLO", "MainActivity - onPause chamado")
    }

    override fun onStop() {
        super.onStop()
        Log.d("CICLO", "MainActivity - onStop chamado")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("CICLO", "MainActivity - onDestroy chamado")
    }
}
