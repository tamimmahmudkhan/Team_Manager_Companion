package com.example.teammanagercompanion

import android.content.Context
import android.graphics.Typeface
import android.opengl.Visibility
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.toolbox.JsonArrayRequest
import com.example.teammanagercompanion.databinding.FragmentTableBinding
import org.json.JSONArray
import org.json.JSONObject

class TableItemAdapter(private val tableType: Int, context: Context, private val hostActivityAction: HostActivityAction) : RecyclerView.Adapter<TableItemAdapter.ViewHolder>() {

    interface HostActivityAction{
        fun onClick(tableType: Int, jsonObject: JSONObject)
    }

    val volley = MySingleton.getInstance(context).requestQueue
    var values = JSONArray()
    val URL = "http://192.168.0.120:8000"
    val GET_TEAMS_URL = URL + "/data-team"
    val GET_STAFF_URL = URL + "/data-staff"
    val GET_PROJECT_URL = URL + "/data-project"

    init {
        var url = ""
        when(tableType){
            0 -> url = GET_STAFF_URL
            1 -> url = GET_TEAMS_URL
            2 -> url = GET_PROJECT_URL
        }
        volley.add(
            JsonArrayRequest(
                url,
                {jsonArray -> values= jsonArray
                notifyDataSetChanged()
                },
                {error -> error.printStackTrace()}
            )
        )
        volley.start()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentTableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values.getJSONObject(position)
        holder.name.typeface = Typeface.DEFAULT_BOLD
        holder.rootView.setOnClickListener { hostActivityAction.onClick(tableType, item) }
        when (tableType){
            TableActivity.TABLE_STAFF -> {
                holder.name.text = "Name: ${item.getString("fname")} ${item.getString("lname")}"
                holder.position.text = "Position: ${item.getString("position")}"
                holder.team.text = "Team: ${item.getString("team")}"
                holder.project.text = "Project: ${item.getString("project")}"
            }
            TableActivity.TABLE_PROJECT -> {
                with(holder){
                    name.text = item.getString("name")
                    this.position.text = "Project: ${item.getString("status")}" // Position textview is used to display status of project for project table.
                    team.text = item.getString("team")
                    project.visibility =View.INVISIBLE
                }
            }
            TableActivity.TABLE_TEAM -> {
                with(holder){
                    name.text = item.getString("name")
                    this.position.text = "Project: ${item.getString("project")}"
                    team.text = "Members: ${item.getString("members")}"
                    project.visibility = View.INVISIBLE
                }}
        }
    }

    override fun getItemCount(): Int = values.length()

    inner class ViewHolder(binding: FragmentTableBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.name
        val position = binding.position
        val team = binding.team
        val project = binding.project
        val rootView = binding.itemRoot
    }

}