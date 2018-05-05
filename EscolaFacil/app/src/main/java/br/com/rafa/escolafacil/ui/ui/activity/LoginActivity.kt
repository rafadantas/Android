package br.com.rafa.escolafacil.ui.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import br.com.rafa.escolafacil.R
import br.com.rafa.escolafacil.ui.constants.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUser: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth:FirebaseAuth
    private lateinit var checkConecta: CheckBox
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var presenceRef: DatabaseReference? = null
    private val tagErro = "LoginActivity"
    //parte do login com google
    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 1
    lateinit var signOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getUserSP("userToken");

        FirebaseDatabase.getInstance().getReference("disconnectmessage")

        //parte do login com google
        val signIn = findViewById<View>(R.id.signInBtn) as SignInButton
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        signOut = findViewById<View>(R.id.signOutBtn) as Button
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn.setOnClickListener {
            view: View? -> signIn()
        }
    }
    private fun logInit(){
        txtUser=findViewById(R.id.txtUser)
        txtPassword=findViewById(R.id.txtPassword)
        progressBar = findViewById(R.id.progressBar)
        auth= FirebaseAuth.getInstance()
        checkConecta = findViewById(R.id.btnCheckBox)
        mAuthListener = FirebaseAuth.AuthStateListener {  }
    }
    override fun onStart() {
        super.onStart()
        if(FirebaseDatabase.getInstance() != null){
            FirebaseDatabase.getInstance().goOnline()
        }else{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
    }
    fun forgotPassword(view:View){
        startActivity(Intent(this, ForgotPassActivity::class.java))
    }
    fun register(view:View){
        startActivity(Intent(this, RegisterActivity::class.java))
    }
    fun login(view:View){
        loginUser()
    }
    private fun loginUser(){
        val user:String=txtUser.text.toString()
        val password:String=txtPassword.text.toString()
        if (!TextUtils.isEmpty(user)&& !TextUtils.isEmpty(password)){
            progressBar.visibility=View.VISIBLE
            auth.signInWithEmailAndPassword(user, password)
                    .addOnCompleteListener(this){
                        task ->
                        if (task.isSuccessful){
                            Log.d(tagErro, "signInWithEmail:success")
                            val userId = auth!!.currentUser!!.uid
                            val userToken = auth!!.currentUser!!.getIdToken(true)
                            if(checkConecta.isChecked){
                                saveUserSP(Constants.SP_TOKEN_USER, value = userToken.toString())
                            }
                            action()
                        }else{
                            Toast.makeText(this, "Erro na autenticação", Toast.LENGTH_LONG).show()
                            progressBar.visibility=View.INVISIBLE

                        }
                    }
        }
    }
    private fun action(){
        startActivity(Intent(this, ListaEscolasActivity::class.java))
    }
    private fun updateUI() {
        val intent = Intent(this@LoginActivity, ListaEscolasActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    private fun saveUserSP(key:String, value:String){
        val pref = this.getSharedPreferences("br.com.rafa.escolafacil.ui.ui.activity.activity_login", android.content.Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()

    }
    private fun getUserSP(key: String) {
        val pref = this.getSharedPreferences("br.com.rafa.escolafacil.ui.ui.activity.activity_login", android.content.Context.MODE_PRIVATE)
        val result = pref.getString(key, "")
        if (result == "") {
            logInit()
        } else {
            intent = Intent(this@LoginActivity, ListaEscolasActivity::class.java)
            startActivity(intent)
        }
    }

    //parte do login com google
    private fun signIn () {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult (task)
        }else {
            Toast.makeText(this, "Problem in execution order :(", Toast.LENGTH_LONG).show()
        }
    }
    private fun handleResult (completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            updateUI (account)
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
    private fun updateUI (account: GoogleSignInAccount) {
        val dispTxt = findViewById<View>(R.id.dispTxt) as TextView
        dispTxt.text = account.displayName
        startActivity(Intent(this, ListaEscolasActivity::class.java))
        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener {
            view: View? ->  mGoogleSignInClient.signOut().addOnCompleteListener {
            task: Task<Void> -> if (task.isSuccessful) {
            dispTxt.text = " "
            signOut.visibility = View.INVISIBLE
            signOut.isClickable = false
        }
        }
        }
    }
}
