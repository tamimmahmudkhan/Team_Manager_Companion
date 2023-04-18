package com.example.teammanagercompanion

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.teammanagercompanion.placeholder.PlaceholderContent
import org.json.JSONArray
import org.json.JSONObject

/**
 * A fragment representing a list of Items.
 */
class ChatFragment : Fragment() , ChatService.MessageReceiver{

    lateinit var rootView: View
    lateinit var chatList: RecyclerView
    lateinit var sendBtn: Button
    lateinit var chatInput:EditText
    lateinit var chatServer: ChatService.ChatServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMessageReceived() {
        (chatList.adapter as ChatAdapter).refreshMessages()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_chat_list, container, false)
        chatList = rootView.findViewById(R.id.chat_list)
        sendBtn = rootView.findViewById(R.id.send_btn)
        chatInput = rootView.findViewById(R.id.chat_input)
        sendBtn.setOnClickListener {
            chatServer.sendMessage(chatInput.text.toString())
        }
        // Set the adapter
        with(chatList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatAdapter(context)
        }
        return rootView
    }

    companion object {
        fun newInstance(chatServer: ChatService.ChatServer) =
            ChatFragment().apply {
                this.chatServer = chatServer
            }
    }
}