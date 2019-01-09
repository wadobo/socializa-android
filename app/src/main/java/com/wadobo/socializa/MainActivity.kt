package com.wadobo.socializa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.android.volley.ClientError
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject
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
            SharedApp.api.service.setHeaders(hashMapOf("Authorization" to SharedApp.prefs.access_token))
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
                    val params = JSONObject(hashMapOf(
                        "client_id" to SharedApp.prefs.client_id,
                        "grant_type" to "convert_token",
                        "backend" to "facebook",
                        "token" to loginResult.accessToken.token
                    ))
                    SharedApp.api.post("auth/convert-token/", params) {
                        when (it) {
                            is JSONObject -> {
                                val accessToken = "${it.get("access_token")} ${it.get("token_type")}"
                                SharedApp.prefs.access_token = accessToken
                                SharedApp.api.service.setHeaders(hashMapOf("Authorization" to accessToken))
                                goToLogin()
                            }
                            is ClientError -> {
                                Toast.makeText(applicationContext, R.string.client_error, Toast.LENGTH_LONG).show()
                                LoginManager.getInstance().logOut()
                            }
                            else -> {
                                Toast.makeText(applicationContext, R.string.server_error, Toast.LENGTH_LONG).show()
                                LoginManager.getInstance().logOut()
                            }
                        }
                        goToLogin()
                    }
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
