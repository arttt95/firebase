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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
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

//            salvarDados()
            atualizarRemoverDados()

//            cadastroUsuario()
//            logarUsuario()

        }

    }

    private fun atualizarRemoverDados() {

        val dados = mapOf(
            "nome" to "Menino Ney",
            "idade" to "33",
//            "cpf" to "524..."
        )

        val referenciaPessi = bancoDados
            .collection("usuarios")
            .document("2")

        ////////////////////
        // RESETING DADOS //
        ////////////////////
        /*referenciaAna.set(dados)
            .addOnSuccessListener {
                exibirMensagem("Usuário alterado com sucesso")
            }.addOnFailureListener { exception ->
                exibirMensagem("Erro ao atualizar usuário. Code: ${exception.message}")
            }*/

        ////////////////////
        // UPDATING DADOS //
        ////////////////////
        /*referenciaPessi.update("idade", "37")
            .addOnSuccessListener {
                exibirMensagem("Usuário alterado com sucesso")
            }.addOnFailureListener { exception ->
                exibirMensagem("Erro ao atualizar usuário. Code: ${exception.message}")
            }*/

        ////////////////////
        // DELETING DADOS //
        ////////////////////
        referenciaPessi.delete()
            .addOnSuccessListener {
                exibirMensagem("Usuário removido com sucesso!")
            }.addOnFailureListener { exception ->
                exibirMensagem("Erro ao remover usuário. Code: ${exception.message}")
            }


    }

    private fun salvarDados() {

        val dados = mapOf(
            "nome" to "Pessi",
            "idade" to "36",
            "cpf" to "425..."

        )

        bancoDados
            .collection("usuarios")
            .document("2")
            .set(dados)
            .addOnSuccessListener { // Void -> apenas para exibir mensagem
                exibirMensagem("Usuário salvo com sucesso!")
            }.addOnFailureListener { exception ->
                exibirMensagem("Erro ao salvar usuário. Code: ${exception.message}")
            }

    }

    override fun onStart() {
        super.onStart()

//        verificarUsuarioLogado()

    }

    private fun verificarUsuarioLogado() {

//        autenticacao.signOut() // Deslogando o usuário

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

    private fun logarUsuario() {

        // Dados inseridos pelo usuário
        val email = "que-mira-bobo@teste.com"
        val senha = "?Argentina10"

        // Simulando uma tela de login com email e senha
        autenticacao.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener { authResult ->

                val id = authResult.user?.uid

                binding.textResultado.text = "Sucesso ao logar usuário com id: $id"

                startActivity(
                    Intent(this, PrincipalActivity::class.java)
                )

            }.addOnFailureListener { exception ->
                val mensagemErro = exception.message
                binding.textResultado.text = "Erro: $mensagemErro"
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