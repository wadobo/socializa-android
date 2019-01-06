package com.wadobo.socializa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.Arrays


class MainActivity : Activity() {
    private lateinit var callbackManager : CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnFacebook = findViewById<Button>(R.id.btnFacebook)
        btnFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"))
        }

        // Check facebook
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            goToLogin()
        }

        // Login
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    if (loginResult.recentlyDeniedPermissions.contains("email")) {
                        LoginManager.getInstance().logOut()
                        Toast.makeText(applicationContext, R.string.email_facebook_perms, Toast.LENGTH_LONG).show()
                        return
                    }
                    goToLogin()
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                }
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun goToLogin() {
        val intent = Intent(applicationContext, AuthenticatedActivity::class.java)
        startActivity(intent)
        finish()
    }
}
