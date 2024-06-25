package com.blankss.connect


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankss.connect.data.RegisterRequest
import com.blankss.connect.databinding.ActivityMainBinding
import com.blankss.connect.pkg.ApiClientBuilder
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var oneTapClient: SignInClient? = null
    private lateinit var signNnRequest: BeginSignInRequest
    private val clientApi = ApiClientBuilder().getUserServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        firebaseAuth = Firebase.auth
        oneTapClient = Identity.getSignInClient(this)

        signNnRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                googleSignIn()
            }
        }
    }

    private suspend fun googleSignIn() {
        val result = oneTapClient?.beginSignIn(signNnRequest)?.await()
        val sender = IntentSenderRequest.Builder(result!!.pendingIntent).build()
        activityLauncher.launch(sender)
    }

    private val activityLauncher= registerForActivityResult(
        contract =  ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            try {
                val cred = oneTapClient!!.getSignInCredentialFromIntent(it.data)
                val idToken = cred.googleIdToken
                if (idToken != null) {
                    val firebaseCred = GoogleAuthProvider.getCredential(idToken, null)
                    firebaseAuth.signInWithCredential(firebaseCred).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val userData = it.result.user
                            createUser(userData)

                            Log.i("INFO : ", userData?.email.toString())
                            Log.i("INFO : ", userData?.uid.toString())
                            Log.i("INFO : ", userData?.photoUrl.toString())
                            Toast.makeText(this, "Berhasil", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun createUser(user: FirebaseUser?) {
        val request = user?.displayName?.let {
            user.email?.let { it1 ->
                RegisterRequest(
                    name = it,
                    email = it1,
                    uniqueID = user.uid,
                    picture = user.photoUrl.toString()
                )
            }
        }
        clientApi.createUser(request!!).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(p0: Call<Void>, p1: Response<Void>) {
                startActivity(Intent(
                    this@MainActivity,
                    ListFriendActivity::class.java
                ))
            }

            override fun onFailure(p0: Call<Void>, p1: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) run {
            startActivity(
                Intent(
                this@MainActivity,
                    ListFriendActivity::class.java
            ))
        }
    }
}