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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

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
//            pesquisarDados()

//            cadastroUsuario()
//            logarUsuario()

        }

    }

    private fun pesquisarDados() {

        val referenciaUsuarios = bancoDados
            .collection("usuarios")

//            .whereEqualTo("nome", "Adulto Ney") // Um valor apenas
//            .whereNotEqualTo("nome", "Adulto Ney") // Um valor
//            .whereIn("nome", listOf("Adulto Ney", "Lionel Pessi")) // Vários valores
//            .whereNotIn("nome", listOf("Adulto Ney", "Lionel Pessi"))
//            .whereArrayContains("conhecimentos", "php")

        // >,  >=, <, <=
//            .whereGreaterThan("idade", "34") // Maior do que...
//            .whereGreaterThanOrEqualTo("idade", "37") // Maior ou igual a...
//            .whereLessThan("idade", "34") // Menor que...
//            .whereLessThanOrEqualTo("idade", "37") // Menor ou igual a...

//            .whereGreaterThanOrEqualTo("idade", "32")
//            .whereLessThanOrEqualTo("idade", "38")
//            .orderBy("idade", Query.Direction.ASCENDING) // 0..10 & A..Z
            .orderBy("idade", Query.Direction.DESCENDING) // 10..0 & Z..A

        referenciaUsuarios
            .addSnapshotListener { querySnapshot, error ->

                val listaDocuments = querySnapshot?.documents

                var listaResultado = ""

                listaDocuments?.forEach { documentSnapshot ->

                    val dados = documentSnapshot?.data

                    if(dados != null) {

                        val nome = dados["nome"]
                        val idade = dados["idade"]
                        listaResultado += "Usuário: $nome | Idade: $idade\n"

                    }

                }

                binding.textResultado.text = listaResultado

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

//        salvarDadosUsuario("Itachi Uchiha", "34")

        val idUsuarioLogado = autenticacao.currentUser?.uid

        if(idUsuarioLogado != null) {
            val referenciaUsuarioLogado = bancoDados
                .collection("usuarios")
//                .document(idUsuarioLogado)

            /*referenciaUsuarioLogado
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val dados = documentSnapshot.data

                    if(dados != null) {
                        val nome = dados["nome"]
                        val idade = dados["idade"]
                        val texto = "Nome: $nome | Idade: $idade"

                        binding.textResultado.text = texto
                    }

                }.addOnFailureListener {

                }*/

            referenciaUsuarioLogado
                /*.addSnapshotListener { documentSnapshot, error ->

                    val dados = documentSnapshot?.data

                    if (dados != null) {
                        val nome = dados["nome"]
                        val idade = dados["idade"]
                        val texto = "Nome: $nome | Idade: $idade"

                        binding.textResultado.text = texto

                    }



                }*/
                .addSnapshotListener { querySnapshot, error ->

                    val listaDocuments = querySnapshot?.documents

                    var listaResultado = ""

                    listaDocuments?.forEach { documentSnapshot ->

                        val dados = documentSnapshot?.data

                        if(dados != null) {

                            val nome = dados["nome"]
                            val idade = dados["idade"]
                            listaResultado += "Usuário: $nome | Idade: $idade\n"

                        }

                    }

                    binding.textResultado.text = listaResultado

                }
        }

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

        logarUsuario()
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
        // Pessi login
//        val email = "que-mira-bobo@teste.com"
//        val senha = "P@ss?word!"

        // Menino Ney login
        val email = "king-of-carnaval@teste.com"
        val senha = "@resenha10?"

        // Simulando uma tela de login com email e senha
        autenticacao.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener { authResult ->

                val id = authResult.user?.uid

                binding.textResultado.text = "Sucesso ao logar usuário com id: $id"

                startActivity(
                    Intent(this, ImageUploadActivity::class.java)
                )

            }.addOnFailureListener { exception ->
                val mensagemErro = exception.message
                binding.textResultado.text = "Erro: $mensagemErro"
            }
    }

    private fun cadastroUsuario() {

        // Dados digitados pelo usuário
        val email = "genjutsu@teste.com"
//        val senha = "@resenha10?" // Menino Ney pass
//        val senha = "P@ss?word!" // Lionel Pessi pass
        val senha = "P@ss?word!"

        val nome = "Itachi Uchiha"
        val idade = "31"

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