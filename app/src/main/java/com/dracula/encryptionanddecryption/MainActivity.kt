package com.dracula.encryptionanddecryption

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.dataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val cryptoManager by lazy { CryptoManager() }
    private val Context.dataStore by dataStore(
        fileName = "user-settings.json",
        serializer = UserSettingsSerializer(cryptoManager)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datastore)
        val usernameEt = findViewById<EditText>(R.id.username_et)
        val passwordET = findViewById<EditText>(R.id.password_et)
        val saveBtn = findViewById<Button>(R.id.save_btn)
        val loadBtn = findViewById<Button>(R.id.load_btn)
        val dataStoreText = findViewById<TextView>(R.id.data_store)

        saveBtn.setOnClickListener {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    dataStore.updateData {
                        UserSettings(
                            username = usernameEt.text.toString(),
                            password = passwordET.text.toString()
                        )
                    }
                }
            }

        }

        loadBtn.setOnClickListener {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    dataStore.data.catch {
                        Log.d(TAG, "onCreate: ${it.localizedMessage}")
                    }.onEach {
                        dataStoreText.text = it.toString()
                    }.launchIn(lifecycleScope)
                }
            }
        }
        /*      val decryptBtn = findViewById<Button>(R.id.decrypt_btn)
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
              }*/


    }
}