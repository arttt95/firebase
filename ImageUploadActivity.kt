package com.arttt95.aulafirebase

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.aulafirebase.databinding.ActivityImageUploadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.UUID
import kotlin.uuid.Uuid

class ImageUploadActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityImageUploadBinding.inflate(layoutInflater)
    }

    private val armazenamento by lazy {
        FirebaseStorage.getInstance()
    }

    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    private val idUsuarioLogado = autenticacao.currentUser?.uid

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
    }

    private var uriImagemSelecionada: Uri? = null // Uri -> android.net

    private var bitmapImagemSelecionada: Bitmap? = null

    private val abrirGaleria = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if(uri != null) {
            binding.imgSelecionada.setImageURI(uri)

            uriImagemSelecionada = uri

            Toast.makeText(this, "Img selecionada!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Nenhuma img selecionada!", Toast.LENGTH_SHORT).show()
        }
    }

    private val abrirCamera = registerForActivityResult(
//        ActivityResultContracts.GetContent()
        ActivityResultContracts.StartActivityForResult()
    ) { resultadoActivity ->

         if(resultadoActivity.resultCode == RESULT_OK) {
             bitmapImagemSelecionada = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                resultadoActivity.data?.extras?.getParcelable("data", Bitmap::class.java)
            } else {
                resultadoActivity.data?.extras?.getParcelable("data")
            }
        } else {
            exibirMensagem("Erro no StartActivityForResult()")
         }

        binding.imgSelecionada.setImageBitmap(bitmapImagemSelecionada)

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

        binding.btnGaleria.setOnClickListener {

            abrirGaleria.launch("image/*") // Mime Type

        }

        binding.btnCamera.setOnClickListener {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            abrirCamera.launch(intent)

        }

        binding.btnUpload.setOnClickListener {

//            uploadGaleria()
            uploadCamera()

        }

        binding.btnRecuperar.setOnClickListener {

            recuperarImagemFirebase()

        }

    }

    private fun recuperarImagemFirebase() {

        if(idUsuarioLogado != null) {

            // Armazenando e recuperando img Database -> Picasso
            armazenamento

                .getReference("fotos")
                .child(idUsuarioLogado)
                .child("foto.jpg")
                .downloadUrl
                .addOnSuccessListener { urlFirebase ->
//                    binding.imgRecuperada.setImageURI(urlFirebase)
                    Picasso.get()
                        .load(urlFirebase)
                        .into(binding.imgRecuperada)
                }

        }

    }

    private fun uploadGaleria() {

        /*
        -> videos
        -> fotos
            -> < id_usuario_logado >
                -> foto1.jpg
        */

        val nomeImagem = UUID.randomUUID().toString()

        if(uriImagemSelecionada != null && idUsuarioLogado != null) {

            armazenamento
                .getReference("fotos")
                .child(idUsuarioLogado)
                .child("foto.jpg")
                .putFile( uriImagemSelecionada!! )
                .addOnSuccessListener { task ->

                    Toast.makeText(
                        this,"Sucesso ao realizar upload da img.",
                        Toast.LENGTH_SHORT).show()
                    task.metadata?.reference?.downloadUrl
                        ?.addOnSuccessListener { urlFirebase ->
                            Toast.makeText(
                                this,
                                urlFirebase.toString(),
                                Toast.LENGTH_SHORT).show()

                            salvarUrlImagem(urlFirebase)

                        }

                }.addOnFailureListener { exception ->

                    Toast.makeText(
                        this,
                        "Erro ao realizar upload da img. Code: ${exception.message}",
                        Toast.LENGTH_SHORT).show()

                }

        }

    }

    private fun uploadCamera() {

        /*
        -> videos
        -> fotos
            -> < id_usuario_logado >
                -> foto1.jpg
        */

//        val nomeImagem = UUID.randomUUID().toString()

        val outPutStream = ByteArrayOutputStream()

        bitmapImagemSelecionada?.compress( // Convertendo bitmap para ByteArray -> OutPutStream
            Bitmap.CompressFormat.JPEG,
            80,
            outPutStream
        )

        if(bitmapImagemSelecionada != null && idUsuarioLogado != null) {

            armazenamento
                .getReference("fotos")
                .child(idUsuarioLogado)
                .child("foto.jpg")
                .putBytes( outPutStream.toByteArray() )
                .addOnSuccessListener { task ->

                    Toast.makeText(
                        this,"Sucesso ao realizar upload da img.",
                        Toast.LENGTH_SHORT).show()
                    task.metadata?.reference?.downloadUrl
                        ?.addOnSuccessListener { urlFirebase ->
                            Toast.makeText(
                                this,
                                urlFirebase.toString(),
                                Toast.LENGTH_SHORT).show()

                            salvarUrlImagem(urlFirebase)

                        }

                }.addOnFailureListener { exception ->

                    Toast.makeText(
                        this,
                        "Erro ao realizar upload da img. Code: ${exception.message}",
                        Toast.LENGTH_SHORT).show()

                }

        }

    }

    // Criar um registro para a url ds img no Firestore Database
    private fun salvarUrlImagem(url: Uri) {

        if(idUsuarioLogado != null) {

            val referenciaUsuarioLogado = bancoDados
                .collection("usuarios")
                .document(idUsuarioLogado)

            referenciaUsuarioLogado
                .update("imagensUrls", FieldValue.arrayUnion(url.toString()))
                .addOnSuccessListener { // Void -> apenas para exibir mensagem
                    exibirMensagem("Url da img salva com sucesso!")
                }.addOnFailureListener { e ->

                    if(e is FirebaseFirestoreException &&
                        e.code == FirebaseFirestoreException.Code.NOT_FOUND) {

                        // Criar o document com o array inicial
                        referenciaUsuarioLogado.set(
                            mapOf(
                                "imagensUrls" to listOf(url.toString())
                            )
                        ).addOnSuccessListener {
                            exibirMensagem("Url da img salva com sucesso -> Failure")
                        }.addOnFailureListener { e ->
                            exibirMensagem("Erro ao salvar url da img -> Failure")
                        }

                    } else {

                        exibirMensagem("Erro ao salvar url da img -> If/Else")

                    }

                }


            /*.set(dados)
                .addOnSuccessListener { // Void -> apenas para exibir mensagem
                    exibirMensagem("Url da img salva com sucesso!")
                }.addOnFailureListener { exception ->
                    exibirMensagem("Erro ao salvar url da img. Code: ${exception.message}")
                }*/

        } else {

            exibirMensagem("Usuário não autenticado")

        }

    }

    private fun exibirMensagem(texto: String) {
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show()
    }

}