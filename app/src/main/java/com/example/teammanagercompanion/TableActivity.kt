package com.example.teammanagercompanion

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class TableActivity : AppCompatActivity(), ServiceConnection {

    private lateinit var pager: ViewPager
    private lateinit var tablePagerAdapter: TablePagerAdapter
    private lateinit var tabs: TabLayout
    private lateinit var chatService: ChatService.ChatServer
    private val TAG = "TableActivity"

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        Log.d(TAG, "onServiceConnected: Connected")
        chatService = p1 as ChatService.ChatServer
        tablePagerAdapter = TablePagerAdapter(supportFragmentManager, chatService)
        pager.adapter = tablePagerAdapter
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        TODO("Not yet implemented")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)

        val intent = Intent(this, ChatService::class.java)
        startService(intent)

        bindService(
            intent,
            this,
            Service.BIND_AUTO_CREATE
        )

        pager = findViewById(R.id.table_pager)
        tabs = findViewById(R.id.tab_layout)
        tabs.setupWithViewPager(pager)
    }

    companion object{
        const val TABLE_STAFF = 0
        const val TABLE_TEAM = 1
        const val TABLE_PROJECT = 2
    }
}