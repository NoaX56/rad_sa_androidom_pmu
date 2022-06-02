package com.example.pmuprojekat.apihandlers

import com.beust.klaxon.json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL

class NorthwindApiHandler {
    val client=OkHttpClient()
    val baseUrl="http://94.156.189.137/api/"
    val mediaType= "application/json; charset=utf-8".toMediaType()

    fun getRequest(sUrl: String): String? {
        var result: String? = null
        try {
            // Create URL
            val url = URL(baseUrl+sUrl)   // Build request
            val request = Request.Builder().url(url).build()   // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }
        return result
    }

    fun postRequest(sUrl: String,data:String):String?{
        var result: String? = null
        try {
            // Create URL
            val url = URL(baseUrl+sUrl)   // Build request
            val body=data.toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()   // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }
        return result
    }

    fun deleteRequest(sUrl: String):String?{
        var result: String? = null
        try {
            // Create URL
            val url = URL(baseUrl+sUrl)   // Build request
            val request = Request.Builder()
                .url(url)
                .delete()
                .build()   // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }
        return result
    }

    fun putRequest(sUrl: String,data:String):String?{
        var result: String? = null
        try {
            // Create URL
            val url = URL(baseUrl+sUrl)   // Build request
            val body=data.toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .put(body)
                .build()   // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }
        return result
    }

}