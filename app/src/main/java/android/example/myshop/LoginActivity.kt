package android.example.myshop

import android.content.Intent
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {


    private lateinit var sign_up_ui_btn: MaterialCardView
    private lateinit var sign_in_ui_btn: MaterialCardView
    private lateinit var sign_in_ui: MaterialCardView
    private lateinit var sign_up_ui: MaterialCardView
    private lateinit var sign_up_btn: MaterialCardView
    private lateinit var sign_in_btn: MaterialCardView
    private lateinit var sign_in_back_btn: ImageView
    private lateinit var sign_up_back_btn: ImageView
    private lateinit var open_sign_up: TextView
    private lateinit var open_sign_in: TextView
    private lateinit var sign_up_fullName: TextInputEditText
    private lateinit var sign_up_pasword: TextInputEditText
    private lateinit var sign_in_pasword: TextInputEditText
    private lateinit var sign_up_email: TextInputEditText
    private lateinit var sign_in_email: TextInputEditText
    private lateinit var sign_in_google: MaterialCardView
    private lateinit var sign_up_google: MaterialCardView
    private lateinit var sign_in_facebook: MaterialCardView
    private lateinit var sign_up_facebook: MaterialCardView
    private lateinit var sign_up_twitter: MaterialCardView
    private lateinit var sign_in_twitter: MaterialCardView
    private lateinit var auth: FirebaseAuth
    private val TAG: String = "LoginActivity"
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 123
    //private lateinit var authStateChangeListener: FirebaseAuth.AuthStateListener

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.mischka, theme)
        }


        sign_in_ui = findViewById(R.id.sign_in_ui);
        sign_up_ui = findViewById(R.id.sign_up_ui)
        sign_in_ui_btn = findViewById(R.id.login_ui_btn)
        sign_up_ui_btn = findViewById(R.id.sign_up_ui_btn)
        sign_in_back_btn = findViewById(R.id.back_btn)
        sign_up_back_btn = findViewById(R.id.sign_up_ui_back_btn)
        open_sign_in = findViewById(R.id.open_sign_in)
        open_sign_up = findViewById(R.id.open_sign_up)
        sign_up_email = findViewById(R.id.username)
        sign_in_email = findViewById(R.id.sign_in_username)
        sign_in_pasword = findViewById(R.id.sign_in_password)
        sign_up_pasword = findViewById(R.id.password)
        sign_up_fullName = findViewById(R.id.full_name)
        sign_in_btn = findViewById(R.id.sign_in_btn)
        sign_up_btn = findViewById(R.id.sign_up_btn)
        sign_in_google = findViewById(R.id.sign_in_google)
        sign_up_google = findViewById(R.id.sign_up_google)
        sign_in_facebook = findViewById(R.id.sign_in_facebook)
        sign_up_facebook = findViewById(R.id.sign_up_facebook)
        sign_in_twitter = findViewById(R.id.sign_in_twitter)
        sign_up_twitter = findViewById(R.id.sign_up_twitter)

        open_sign_up.setOnClickListener {
            load_down_ui(sign_in_ui)
            load_up_ui(sign_up_ui)
        }

        open_sign_in.setOnClickListener {
            load_down_ui(sign_up_ui)
            load_up_ui(sign_in_ui)
        }


        sign_in_ui_btn.setOnClickListener {
            load_up_ui(sign_in_ui)
        }

        sign_up_ui_btn.setOnClickListener {
            load_up_ui(sign_up_ui)
        }

        sign_up_back_btn.setOnClickListener {
            load_down_ui(sign_up_ui)
        }

        sign_in_back_btn.setOnClickListener {
            load_down_ui(sign_in_ui)
        }


        sign_in_btn.setOnClickListener {
            val email: String = sign_in_email.text.toString().trim()
            val password: String = sign_in_pasword.text.toString().trim()
            if(email.isEmpty()) {
                Toast.makeText(this, "Please Enter the Email", Toast.LENGTH_SHORT).show()
            } else if(password.isEmpty()) {
                Toast.makeText(this, "Please Enter the Password", Toast.LENGTH_SHORT).show()
            } else {
                signInUser(email, password)
            }
        }

        sign_up_btn.setOnClickListener {
            val name: String = sign_up_fullName.text.toString()
            val email: String = sign_up_email.text.toString().trim()
            val password: String = sign_up_pasword.text.toString().trim()
            if(name.isEmpty()) {
                Toast.makeText(this, "Please Enter the Full Name", Toast.LENGTH_SHORT).show()
            } else if(email.isEmpty()) {
                Toast.makeText(this, "Please Enter the Email", Toast.LENGTH_SHORT).show()
            } else if(password.isEmpty()) {
                Toast.makeText(this, "Please Enter the Password", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(name, email, password)
            }
        }

        sign_up_google.setOnClickListener {
            signUpWithGoogle()
        }

    }

    private fun signUpWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
            }
        }
    }


    override fun onStart() {
        super.onStart()
       /* val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }*/
    }

    override fun onStop() {
        super.onStop()
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startMainActivity()
        }
    }

    private fun load_down_ui(view: View) {
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_out)
        view.startAnimation(animation)
        view.visibility = View.GONE
    }

    private fun load_up_ui(view: View) {
        view.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(this, R.anim.up_from_bottom_1000_ms)
        view.startAnimation(animation)
    }


    fun startMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}