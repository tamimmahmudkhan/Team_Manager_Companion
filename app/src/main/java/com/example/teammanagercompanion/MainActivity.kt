package com.example.teammanagercompanion

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.parseAsHtml
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import org.json.JSONObject

import java.net.URL
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext


class MainActivity : AppCompatActivity(){

    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var volley : RequestQueue
//    private lateinit var client : OkHttpClient
    private val TAG = "MainActivity"
//    private var url = URL("http://192.168.0.120:8000/")
    private var url = URL("http://10.0.2.2:8000/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button = findViewById<Button>(R.id.button)
        email = findViewById(R.id.eml)
        password = findViewById(R.id.pwd)
        HttpsTrustManager.allowAllSSL()

//        client = OkHttpClient()

        volley = MySingleton.getInstance(this).requestQueue

        volley.start()

//        run()

        button.setOnClickListener {

            Log.d(TAG, "onCreate: Login with ${url}verify?email=${email.text}&password=${password.text}")
            volley.add(
                StringRequest(
                    "${url}verify?email=${email.text}&password=${password.text}",
                    { response ->
//                        if (response.has("email")){
//                            if (response.getString("email").equals(email.text)){
                                startActivity(Intent(this@MainActivity, TableActivity::class.java))
//                            }
//                        }
                    },
                    { error -> Log.d(TAG, "onCreate: Error is : ${error.printStackTrace()}")}
                )
            )
        }

        button = findViewById(R.id.login_reg_btn)

        button.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegeditActivity::class.java).apply {
                putExtra(RegeditActivity.MODE_KEY, RegeditActivity.MODE_REGISTER)
            }, Bundle().apply { putInt(RegeditActivity.MODE_KEY, RegeditActivity.MODE_REGISTER) })
        }
    }

//    fun run() {
//        val request = Request.Builder()
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.use {
//                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
//
//                    for ((name, value) in response.headers) {
//                        Log.d(TAG, "onResponse: Name:$name Value:$value ${response.body}")
//                    }
//
//                    println(response.body!!.string())
//                }
//            }
//        })
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}