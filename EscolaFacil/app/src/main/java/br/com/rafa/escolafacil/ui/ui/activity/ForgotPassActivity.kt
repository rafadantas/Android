package br.com.rafa.escolafacil.ui.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import br.com.rafa.escolafacil.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        txtEmail=findViewById(R.id.txtEmail)
        auth= FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressBar)
    }
    fun send(view:View){
        val email=txtEmail.text.toString()

        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this){
                        task ->
                        if (task.isSuccessful){
                            progressBar.visibility=View.VISIBLE
                            Toast.makeText(this, "Email enviado, verifique seu email", Toast.LENGTH_LONG)
                            startActivity(Intent(this, LoginActivity::class.java))
                        }else{
                            Toast.makeText(this, "Erro ao enviar email", Toast.LENGTH_LONG)
                        }
                    }
        }
    }
}
