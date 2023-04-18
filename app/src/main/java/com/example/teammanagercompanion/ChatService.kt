package com.example.teammanagercompanion

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ChatService : Service() {
    lateinit var socket: Socket
    lateinit var okhttp: OkHttpClient
    lateinit var chatServer: ChatServer
    val URL = "http://192.168.0.120:8000"
    val GET_CHAT_URL = "$URL/chat"
    private val TAG = "ChatService"

    override fun onCreate() {
        super.onCreate()

        socket = IO.socket(GET_CHAT_URL)
        socket.connect()

        okhttp = OkHttpClient()

        chatServer = ChatServer(this)

        socket.on("refresh") {
            chatServer.onMessageReceived()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return chatServer
    }

    override fun onDestroy() {
        socket.disconnect()
        socket.off("message")
        super.onDestroy()
    }

    interface MessageReceiver{
        fun onMessageReceived()
    }

    class ChatServer(service: ChatService): Binder(){
        val chat = service
        private lateinit var messageReceiver: MessageReceiver
        private val TAG = "ChatService"

        fun setMessageReceiver(receiver: MessageReceiver){
            this.messageReceiver = receiver
        }

        fun onMessageReceived(){
            messageReceiver.onMessageReceived()
        }

        fun sendMessage(message:String) {

            val formBody = FormBody.Builder().add("mssg", message).build()
            val jsonBody = JSONObject()
            jsonBody.put("mssg", message)

            val request = okhttp3.Request.Builder().url("${chat.GET_CHAT_URL}?mssg=$message")
//                .method("POST", RequestBody.create(
//                MediaType.parse(jsonBody.toString()), jsonBody.toString()))
                .build()

            chat.okhttp.newCall(request).enqueue( object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "onFailure: Error Failure!")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        messageReceiver.onMessageReceived()
                        Log.d(TAG, "onResponse: Success")
                    }
                }
            })

//            val requestQueue = MySingleton.getInstance(chat.applicationContext).requestQueue
////            val mssg = JSONObject()
////            mssg.put("mssg", message)
//            requestQueue.add(
//                JsonObjectRequest(
//                    "${chat.GET_CHAT_URL}?mssg=$message",
//                    { messageReceiver.onMessageReceived()},{error -> error.printStackTrace()}
//                )
//            )
//            requestQueue.start()
//            chat.socket.emit("send", message)
        }
    }
}