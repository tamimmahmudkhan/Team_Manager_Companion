package com.example.teammanagercompanion

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.json.JSONArray
import org.json.JSONObject

/**
 * A fragment representing a list of Items.
 */
class TableFragment : Fragment(), TableItemAdapter.HostActivityAction{

    private var tableType = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_table_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                when (tableType) {
                    0 -> adapter = TableItemAdapter(TableActivity.TABLE_STAFF, context, this@TableFragment)
                    1 -> adapter = TableItemAdapter(TableActivity.TABLE_TEAM, context, this@TableFragment)
                    2 -> adapter = TableItemAdapter(TableActivity.TABLE_PROJECT, context, this@TableFragment)
                }
            }
        }
        return view
    }

    override fun onClick(tableType:Int, jsonObject: JSONObject) {
        val intent = Intent(context, RegeditActivity::class.java)
        intent.putExtra(RegeditActivity.EDIT_TYPE, tableType)
        intent.putExtra(RegeditActivity.EDIT_ITEM, jsonObject.toString())
        intent.putExtra(RegeditActivity.MODE_KEY, RegeditActivity.MODE_EDIT)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(type: Int) =
            TableFragment().apply {
                tableType = type
            }
    }
}