package com.wadobo.socializa.api

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


enum class CallResponse {
    DICT, LIST
}


class ServiceVolley(context: Context) {
    val basePath = "http://socializa.wadobo.com/api/v1.0/"
    val queue = Volley.newRequestQueue(context)
    var svHeaders = hashMapOf<String, String>("Content-Type" to "application/json")

    fun setHeaders(values: Map<String, String>) {
        svHeaders.putAll(values)
    }

    fun call(method: Int, path: String, params: JSONObject, out: CallResponse=CallResponse.DICT, completionHandler: (response: Any) -> Unit) {
        if (out == CallResponse.LIST) {
            val jsonReq = object : JsonArrayRequest(method, basePath + path, null,
                Response.Listener<JSONArray> { response ->
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    completionHandler(error)
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    return svHeaders
                }
            }
            queue.add(jsonReq)
        } else {
            val jsonReq = object : JsonObjectRequest(method, basePath + path, params,
                Response.Listener { response ->
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    completionHandler(error)
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    return svHeaders
                }
            }
            queue.add(jsonReq)
        }
    }
}


class APIController constructor(serviceInjection: ServiceVolley) {

    var service = serviceInjection

    fun post(path: String, params: JSONObject, completionHandler: (response: Any) -> Unit) {
        service.call(Request.Method.POST, path, params, completionHandler = completionHandler)
    }

    fun patch(path: String, params: JSONObject, completionHandler: (response: Any) -> Unit) {
        service.call(Request.Method.PATCH, path, params, completionHandler = completionHandler)
    }

    fun delete(path: String, params: JSONObject, completionHandler: (response: Any) -> Unit) {
        service.call(Request.Method.DELETE, path, params, completionHandler = completionHandler)
    }

    fun put(path: String, params: JSONObject, completionHandler: (response: Any) -> Unit) {
        service.call(Request.Method.PUT, path, params, completionHandler = completionHandler)
    }

    fun get(path: String, params: JSONObject, out: CallResponse, completionHandler: (response: Any) -> Unit) {
        service.call(Request.Method.GET, path, params, out = out, completionHandler = completionHandler)
    }
}
