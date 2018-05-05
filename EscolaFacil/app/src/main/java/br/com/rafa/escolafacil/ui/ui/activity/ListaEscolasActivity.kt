package br.com.rafa.escolafacil.ui.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.LinearLayout
import br.com.rafa.escolafacil.R
import br.com.rafa.escolafacil.ui.model.Escola
import br.com.rafa.escolafacil.ui.model.Escolas
import br.com.rafa.escolafacil.ui.ui.adapter.AdapterEscola
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_lista_escolas.*


class ListaEscolasActivity : AppCompatActivity() {
    //Botao sair
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    lateinit var signOut: Button

    //novo recyclerView
    //lateinit var mRecyclerView : RecyclerView
    //lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_escolas)
        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        //novo recycler
        //mRecyclerView = findViewById(R.id.recycler)

        //mDatabase = FirebaseDatabase.getInstance().getReference("Escola")

      /*logRecyclerView()


        //}
    private fun logRecyclerView(){


        var RecyclerViewAdapter = object : FirebaseRecyclerAdapter<Escola, EscolaViewHolder>(


                Escola::class.java.
                R.layout.content_item.
                EscolaViewHolder::class.java.
                mDatabase
        ){



            override fun onBindViewHolder(holder: EscolaViewHolder, position: Int, model: Escola) {
            }
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EscolaViewHolder {
            }
            override fun populateViewHolder(viewHolder: EscolaViewHolder, model: Escola?, position: Int) {
            }
        }
    }*/


                //botao sair
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        signOut = findViewById<View>(R.id.signOutBtn) as Button
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val escolas = ArrayList<Escolas>()

        escolas.add(Escolas(name = "EEEFM Liliosa de Paiva Leite",
                inep = 250398453,
                categoria = "Escola Estadual",
                thumbnail = R.drawable.escola1))
        escolas.add(Escolas(name = "EEEFM Gonçalves Dias",
                inep = 520548451,
                categoria = "Escola Estadual",
                thumbnail = R.drawable.escola2))
        escolas.add(Escolas(name = "EMEF Auguto dos Anjos",
                inep = 987522451,
                categoria = "Escola Municipal",
                thumbnail = R.drawable.escola3))

        val adapter = AdapterEscola(escolas)
        recyclerView.adapter = adapter


        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener { view: View? ->
            mGoogleSignInClient.signOut().addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    signOut.visibility = View.INVISIBLE
                    signOut.isClickable = false
                }
            }
        }
    }
    }
