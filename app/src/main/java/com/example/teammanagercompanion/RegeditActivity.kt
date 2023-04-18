package com.example.teammanagercompanion

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.math.ln


class RegeditActivity : AppCompatActivity() {

    private lateinit var fnameText: EditText
    private lateinit var lnameText: EditText
    private lateinit var dobPicker: EditText
    private lateinit var emailText: EditText
    private lateinit var passText: EditText
    private lateinit var workText: EditText
    private lateinit var absentText: EditText
    private lateinit var positionText: EditText
    private lateinit var teamText: EditText
    private lateinit var projectText: EditText
    private lateinit var submitBtn: Button

    private lateinit var volley: RequestQueue

//    val URL = "http://192.168.0.120:8000"
    val URL = "http://10.0.2.2:8000"
    val REGISTER_URL = "$URL/register"
    val STAFFEDIT_URL = "$URL/staff-edit"
    val TEAMEDIT_URL = "$URL/team-edit"
    val PROJECTEDIT_URL = "$URL/project-edit"
    private val TAG = "RegeditActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regedit)

        volley = MySingleton.getInstance(this).requestQueue

        fnameText = findViewById(R.id.fname_text)
        lnameText = findViewById(R.id.lname_text)
        dobPicker = findViewById(R.id.dob_text)
        emailText = findViewById(R.id.email_text)
        passText = findViewById(R.id.password_text)

        val mode = intent.getIntExtra(MODE_KEY, MODE_REGISTER)
        var json = JSONObject()

        if (mode == MODE_EDIT){

            workText = findViewById(R.id.work_num)
            absentText = findViewById(R.id.absent_num)
            positionText = findViewById(R.id.position_text)
            projectText = findViewById(R.id.project_text)
            teamText = findViewById(R.id.team_text)

            json = JSONObject(intent.getStringExtra(RegeditActivity.EDIT_ITEM)!!)

            when (intent.getIntExtra(EDIT_TYPE, TableActivity.TABLE_STAFF)){
                TableActivity.TABLE_STAFF -> {
                    fnameText.setText(json.getString("fname"))
                    lnameText.setText(json.getString("lname"))
                    dobPicker.setText(json.getString("dob"))
                    emailText.setText(json.getString("email"))
                    passText.setText(json.getString("password"))

                    workText.setText(json.getString("workDays"))
                    workText.visibility = View.VISIBLE
                    absentText.setText(json.getString("absentDays"))
                    absentText.visibility = View.VISIBLE
                    positionText.setText(json.getString("position"))
                    positionText.visibility = View.VISIBLE
                    projectText.setText(json.getString("project"))
                    projectText.visibility = View.VISIBLE
                    teamText.setText(json.getString("team"))
                    teamText.visibility = View.VISIBLE

                }
                TableActivity.TABLE_PROJECT -> {
                    fnameText.setText(json.getString("name"))
                    lnameText.setText(json.getString("status"))
                    emailText.setText(json.getString("team"))
                    dobPicker.visibility = View.GONE
                    passText.visibility = View.GONE
                }
                TableActivity.TABLE_TEAM -> {
                    fnameText.setText(json.getString("name"))
                    lnameText.setText(json.getString("project"))
                    emailText.setText(json.getString("members"))
                    dobPicker.visibility = View.GONE
                    passText.visibility = View.GONE
                }
            }
        }

        submitBtn = findViewById(R.id.register_btn)

//        var PARAMETERIZED_URL = "$REGISTER_URL?fname=${fnameText.text}&lname=${lnameText.text}&dob=${dobPicker.text}&email=${emailText.text}&password=${passText.text}"
//
//        if(mode == MODE_EDIT){
//            PARAMETERIZED_URL = "$EDIT_URL?fname=${fnameText.text}&lname=${lnameText.text}&dob=${dobPicker.text}&email=${emailText.text}&password=${passText.text}&workdays=${workText.text}&absentdays=${absentText.text}&position=${positionText.text}&project=${projectText.text}&team=${teamText.text}"
//        }

        submitBtn.setOnClickListener {
            val mson = JSONObject()
            mson.apply {
                put("fname", fnameText.text.toString())
                put("lname", fnameText.text.toString())
                put("dob", fnameText.text.toString())
                put("email", fnameText.text.toString())
                put("password", fnameText.text.toString())
                if (mode == MODE_EDIT){
                    put("name", "${fnameText.text} ${lnameText.text}")
                    put("id", json.getString("_id"))
                    put("workdays", workText.text.toString())
                    put("absentdays", absentText.text.toString())
                    put("position", positionText.text.toString())
                    put("project", projectText.text.toString())
                    put("team", teamText.text.toString())
            }
            }

            var urlBuilder = Uri.parse(REGISTER_URL).buildUpon()

            if (mode == MODE_EDIT) {
                when(intent.getIntExtra(EDIT_TYPE, TableActivity.TABLE_STAFF)){
                    TableActivity.TABLE_STAFF -> urlBuilder = Uri.parse(STAFFEDIT_URL).buildUpon()
                    TableActivity.TABLE_TEAM -> urlBuilder = Uri.parse(TEAMEDIT_URL).buildUpon()
                    TableActivity.TABLE_PROJECT -> urlBuilder = Uri.parse(PROJECTEDIT_URL).buildUpon()
                }
            }

            urlBuilder.apply {
                appendQueryParameter("dateofbirth", dobPicker.text.toString())
                appendQueryParameter("email", emailText.text.toString())
                appendQueryParameter("password", passText.text.toString())
                if (mode == MODE_EDIT){
                    when (intent.getIntExtra(EDIT_TYPE, TableActivity.TABLE_STAFF)){
                        TableActivity.TABLE_STAFF -> {
                            appendQueryParameter("id", json.getString("_id"))
                            appendQueryParameter("name", "${fnameText.text} ${lnameText.text}")
                            appendQueryParameter("workdays", workText.text.toString())
                            appendQueryParameter("absentdays", absentText.text.toString())
                            appendQueryParameter("position", positionText.text.toString())
                            appendQueryParameter("project", projectText.text.toString())
                            appendQueryParameter("team", teamText.text.toString())
                        }
                        TableActivity.TABLE_TEAM -> {
                            appendQueryParameter("name", fnameText.text.toString())
                            appendQueryParameter("members", emailText.text.toString())
                            appendQueryParameter("project", lnameText.text.toString())
                        }
                        TableActivity.TABLE_PROJECT -> {
                            appendQueryParameter("name", fnameText.text.toString())
                            appendQueryParameter("status", lnameText.text.toString())
                            appendQueryParameter("team", emailText.text.toString())
                        }
                    }
                }else{
                    appendQueryParameter("fname", fnameText.text.toString())
                    appendQueryParameter("lname", lnameText.text.toString())
                }
            }

//            region Volley Version
            volley.add(
                JsonObjectRequest(
                    Request.Method.POST,
                    urlBuilder.toString(),
                    mson,
                    {Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                    finish()},
                    {
                        it.printStackTrace()
                        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()}
                )
            )
            volley.start()
//            endregion Volley Version

//            region OkHttpVersion
//            val client = OkHttpClient()
//
//            val formBodyBuilder = FormBody.Builder()
//                .add("fname", fnameText.text.toString())
//                .add("lname", fnameText.text.toString())
//                .add("dob", fnameText.text.toString())
//                .add("email", fnameText.text.toString())
//                .add("password", fnameText.text.toString())
//
//            if (mode == MODE_EDIT){
//                formBodyBuilder
//                    .add("workdays", workText.text.toString())
//                    .add("absentdays", absentText.text.toString())
//                    .add("position", positionText.text.toString())
//                    .add("project", projectText.text.toString())
//                    .add("team", teamText.text.toString())
//            }
//
//            val postData: RequestBody =
//                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
//            val request = okhttp3.Request.Builder()
//                .url(urlBuilder.toString())
//                .method("POST", postData)
//                .build()

//
//            client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    Log.d(TAG, "onFailure: failed boiz")
//                    e.printStackTrace()
//                    this@RegeditActivity.runOnUiThread {
//                        Toast.makeText(this@RegeditActivity, "Failed!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    this@RegeditActivity.runOnUiThread {
//                        Log.d(TAG, "onResponse: success boiz")
//                        Log.d(TAG, "onResponse: ${response.code()}")
//                        Toast.makeText(this@RegeditActivity, "Success!", Toast.LENGTH_SHORT).show()
//                        finish()
//                    }
//                }
//            })
//            endregion OkHttpVersion
        }

    }

    companion object {
        const val MODE_KEY="mode_key"
        const val MODE_REGISTER = 0
        const val MODE_EDIT = 1
        const val EDIT_TYPE = "type_edit"
        const val EDIT_ITEM = "item_edit"
    }
}