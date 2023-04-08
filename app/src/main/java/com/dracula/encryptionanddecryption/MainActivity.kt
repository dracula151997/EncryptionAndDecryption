package com.dracula.encryptionanddecryption

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private val cryptoManager by lazy { CryptoManager() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val decryptBtn = findViewById<Button>(R.id.decrypt_btn)
        val encryptBtn = findViewById<Button>(R.id.encrypt_btn)
        val plainTextEditText = findViewById<EditText>(R.id.edit_text)
        val encryptedTextView = findViewById<TextView>(R.id.encrypted_txt)

        encryptBtn.setOnClickListener {
            val text = plainTextEditText.text.toString()
            val byteArray = text.encodeToByteArray()
            val file = File(filesDir, "secret.txt")
            if (!file.exists())
                file.createNewFile()
            val fos = FileOutputStream(file)
            val encryptedText = cryptoManager.encrypt(bytes = byteArray, outputStream = fos)
                .decodeToString()
            encryptedTextView.text = encryptedText

        }

        decryptBtn.setOnClickListener {
            val file = File(filesDir, "secret.txt")
            val text = cryptoManager.decrypt(
                FileInputStream(file)
            ).decodeToString()
            plainTextEditText.setText(text)
        }


    }
}