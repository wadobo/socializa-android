package com.wadobo.socializa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject
import java.util.*

class MainActivity : Activity() {

    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppEventsLogger.activateApp(application)

        setContentView(R.layout.activity_main)

        // Check token facebook for login
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            goToLogin()
        }

        val btnLoginFacebook = findViewById<Button>(R.id.btnLoginFacebook)
        val tag = findViewById<TextView>(R.id.tag)

        btnLoginFacebook.setOnClickListener {
            // Login
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        goToLogin()
                    }

                    override fun onCancel() {
                        tag.text = getString(R.string.cancel)

                    }

                    override fun onError(error: FacebookException) {
                        tag.text = getString(R.string.error)

                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun goToLogin() {
        val intent = Intent(applicationContext, AuthenticatedActivity::class.java)
        startActivity(intent)
        finish()
    }
}
