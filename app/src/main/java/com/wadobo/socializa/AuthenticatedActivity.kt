package com.wadobo.socializa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.facebook.AccessToken
import com.facebook.login.LoginManager


class AuthenticatedActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authenticated_activity)

        var btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}

