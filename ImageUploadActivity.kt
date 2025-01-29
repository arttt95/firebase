package com.arttt95.aulafirebase

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.aulafirebase.databinding.ActivityImageUploadBinding
import com.google.firebase.storage.FirebaseStorage

class ImageUploadActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityImageUploadBinding.inflate(layoutInflater)
    }

    private val armazenamento by lazy {
        FirebaseStorage.getInstance()
    }

    private var uriImagemSelecionada: Uri? = null // Uri -> android.net

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

        binding.btnUpload.setOnClickListener {

            uploadGaleria()

        }

    }

    private fun uploadGaleria() {

        /*
        -> videos
        -> fotos
            -> viagens
                -> foto1.jpg
        */

        if(uriImagemSelecionada != null) {

            armazenamento
                .getReference("fotos")
                .child("viagens")
                .child("foto.jpg")
                .putFile( uriImagemSelecionada!! )
                .addOnSuccessListener { task ->
                    Toast.makeText(this,"Sucesso ao realizar upload da img.", Toast.LENGTH_SHORT).show()
                    task.metadata?.reference?.downloadUrl
                        ?.addOnSuccessListener { uriFirebase ->
                            Toast.makeText(this, uriFirebase.toString(), Toast.LENGTH_SHORT).show()
                        }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this,"Erro ao realizar upload da img. Code: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}