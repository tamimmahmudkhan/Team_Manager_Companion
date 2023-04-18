package com.example.teammanagercompanion

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest

import com.example.teammanagercompanion.placeholder.PlaceholderContent.PlaceholderItem
import com.example.teammanagercompanion.databinding.FragmentChatBinding
import io.socket.client.IO
import org.json.JSONArray

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ChatAdapter(context: Context) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var messages: JSONArray = JSONArray()

    val URL = "http://10.0.2.2:8000"
    val GET_CHAT_URL = "$URL/chat"
    private val TAG = "ChatAdapter"
    val requestQueue = MySingleton.getInstance(context).requestQueue

    init {
        refreshMessages()
    }

    fun refreshMessages() {
        requestQueue.add(
            JsonArrayRequest(
                GET_CHAT_URL,
                { chats ->
                    messages = chats
                    notifyDataSetChanged()
                },
                { error -> error.printStackTrace() }
            )
        )
        requestQueue.start()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = messages.getJSONObject(position)
        holder.chatView.text = "${item.getString("name")}: ${item.getString("message")}"
    }

    override fun getItemCount(): Int = messages.length()

    inner class ViewHolder(binding: FragmentChatBinding) : RecyclerView.ViewHolder(binding.root) {
        val chatView: TextView = binding.chatMssg

        override fun toString(): String {
            return super.toString() + " '"
        }
    }

}