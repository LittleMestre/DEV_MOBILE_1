package com.example.listacomprasinteligente

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listacomprasinteligente.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserBinding
    private lateinit var securityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        securityPreferences = SecurityPreferences(this)
        binding.btnEnter.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btn_enter) {
            handleEnter()
        }
    }

    private fun handleEnter() {
        val userName = binding.editName.text.toString().trim()

        if (userName.isEmpty()) {
            Toast.makeText(this, "Digite seu nome para continuar.", Toast.LENGTH_SHORT).show()
            return
        }

        securityPreferences.storeString(Constants.KEY_USER_NAME, userName)
        Toast.makeText(this, "Bem-vindo, $userName!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
