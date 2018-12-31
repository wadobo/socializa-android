package com.wadobo.socializa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.facebook.*
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.authenticated_activity.*


class AuthenticatedActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authenticated_activity)

        var btnLogout = findViewById<Button>(R.id.btnLogout)
        var textLogin = findViewById<TextView>(R.id.textLogin)

        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            getFacebookEmail(accessToken)
        }

        btnLogout.setOnClickListener {
            // Logout
            if (AccessToken.getCurrentAccessToken() != null) {
                facebookLogout()
            }
        }

    }

    fun getFacebookEmail(token: AccessToken) {
        val request = GraphRequest.newMeRequest(token) { obj, _ ->
            if (obj.has("email")) {
                textLogin.text = obj.get("email").toString()
            } else {
                facebookLogout()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "email")
        request.parameters = parameters
        request.executeAsync()
    }

    fun facebookLogout() {
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE,
            GraphRequest.Callback {
                AccessToken.setCurrentAccessToken(null)
                LoginManager.getInstance().logOut()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }).executeAsync()
    }
}

