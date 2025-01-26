package com.arttt95.aulafirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.aulafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        verificarUsuarioLogado()

    }

    private fun verificarUsuarioLogado() {
        val usuario = autenticacao.currentUser
        val id = usuario?.uid

        if(usuario != null) {
            exibirMensagem("Usuário logado com id: $id")
            startActivity(
                Intent(this, PrincipalActivity::class.java)
            )
        } else {
            exibirMensagem("Não tem usuário logado")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnExecutar.setOnClickListener {

            cadastroUsuario()

        }

    }

    private fun cadastroUsuario() {

        // Dados digitados pelo usuário
        val email = "que-mira-bobo@teste.com"
        val senha = "?Argentina10"

        // Tela de cadastro do App
        autenticacao.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { authResult ->

                val email = authResult.user?.email
                val id = authResult.user?.uid

//              exibirMensagem("Sucesso ao cadastrar usuário: $id - $email")
                binding.textResultado.text = "Sucesso: $id - $email"

            }.addOnFailureListener { exception ->

                val mensagemErro = exception.message
                binding.textResultado.text = "Erro: $mensagemErro"

            }

    }

    private fun exibirMensagem(texto: String) {
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show()
    }
}