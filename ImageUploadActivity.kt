package com.arttt95.aulafirebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.aulafirebase.databinding.ActivityImageUploadBinding

class ImageUploadActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityImageUploadBinding.inflate(layoutInflater)
    }

    private val abrirGaleria = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if(uri != null) {
            binding.imgSelecionada.setImageURI(uri)
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

    }
}