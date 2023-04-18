package com.example.teammanagercompanion

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2

class TablePagerAdapter(fm: FragmentManager, service: ChatService.ChatServer) : FragmentStatePagerAdapter(fm) {

    private val chatService = service

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> TableFragment.newInstance(TableActivity.TABLE_STAFF)
            1 -> TableFragment.newInstance(TableActivity.TABLE_TEAM)
            2 -> TableFragment.newInstance(TableActivity.TABLE_PROJECT)
            3 -> ChatFragment.newInstance(chatService).apply {
                chatService.setMessageReceiver(this)
            }
            else -> Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Staff"
            1 -> "Teams"
            2 -> "Projects"
            3 -> "Chat"
            else -> "Null"
        }
    }
}