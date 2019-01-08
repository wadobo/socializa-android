package com.wadobo.socializa

import android.app.Application
import com.wadobo.socializa.api.APIController
import com.wadobo.socializa.api.ServiceVolley


class SharedApp : Application() {
    companion object {
        lateinit var prefs: Preferences
        lateinit var api: APIController
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Preferences(applicationContext)
        val service = ServiceVolley(applicationContext)
        api = APIController(service)
    }
}