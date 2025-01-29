package com.arttt95.aulafirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.aulafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
//            atualizarRemoverDados()

//            listarDados()

//            cadastroUsuario()
            logarUsuario()

        }

    }

    private fun salvarDadosUsuario(nome: String,idade: String) {

        val idUsuarioLogado = autenticacao.currentUser?.uid

        if(idUsuarioLogado != null) {

            val dados = mapOf(
                "nome" to nome,
                "idade" to idade
                // Poderia ser salvo vários outros dados em 'cadastroUsuario -> addOnSuccesListener'
            )

            bancoDados
                .collection("usuarios")
                .document(idUsuarioLogado)
                .set(dados)

        }

    }

    private fun listarDados() {

        salvarDadosUsuario("Itachi Uchiha", "34")

    }

    private fun atualizarRemoverDados() {

        val dados = mapOf(
            "nome" to "Minato",
            "idade" to "42",
//            "cpf" to "42582332852"
        )

        /*val idUsuarioLogado = autenticacao.currentUser?.uid

        if(idUsuarioLogado != null) {
            val referenciaUsuarioLogado = bancoDados
                .collection("usuarios")
                .document(idUsuarioLogado)

            referenciaUsuarioLogado.set(dados)
                .addOnSuccessListener {
                    exibirMensagem("Usuário logado foi inserido no db")
                }.addOnFailureListener { exception ->
                    exibirMensagem("Erro ao inserir usuário logado no db. Code: ${exception.message}")
                }
        }*/

        val referenciaMinato = bancoDados
            .collection("usuarios")
//            .document("1")

        ////////////////////
        // RESETING DATAS //
        ////////////////////
        /*referenciaAna.set(dados)
            .addOnSuccessListener {
                exibirMensagem("Usuário alterado com sucesso")
            }.addOnFailureListener { exception ->
                exibirMensagem("Erro ao atualizar usuário. Code: ${exception.message}")
            }*/

        ////////////////////
        // UPDATING DATAS //
        ////////////////////
        /*referenciaPessi.update("idade", "37")
            .addOnSuccessListener {
                exibirMensagem("Usuário alterado com sucesso")
            }.addOnFailureListener { exception ->
                exibirMensagem("Erro ao atualizar usuário. Code: ${exception.message}")
            }*/

        ////////////////////
        // DELETING DATAS //
        ////////////////////
        /*referenciaPessi.delete()
            .addOnSuccessListener {
                exibirMensagem("Usuário removido com sucesso!")
            }.addOnFailureListener { exception ->
                exibirMensagem("Erro ao remover usuário. Code: ${exception.message}")
            }*/

        ////////////////////////////////
        // ADDING DOCUMENT PATH ON DB //
        ////////////////////////////////
        referenciaMinato.add( dados )
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
        val email = "papai-cristiano@teste.com"
        val senha = "Siuu@777"
        val nome = "Cristiano Penaldo"
        val idade = "39"

        // Tela de cadastro do App
        autenticacao.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { authResult ->

                val email = authResult.user?.email
                val id = authResult.user?.uid
                // Aqui poderia ser mais dados do usuário no database
                salvarDadosUsuario(nome, idade)

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