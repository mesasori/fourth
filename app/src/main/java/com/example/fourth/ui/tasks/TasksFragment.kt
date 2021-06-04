package com.example.fourth.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.fourth.R
import com.example.fourth.models.Constants
import com.example.fourth.models.MessageInfo
import com.example.fourth.ui.messages.MessageExample
import com.example.fourth.ui.tasks.support.TaskInfo
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_task.view.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks.view.*
import kotlinx.android.synthetic.main.view_messages.view.*
import kotlinx.android.synthetic.main.view_task.view.*
import kotlinx.android.synthetic.main.view_task.view.task_description

class TasksFragment : Fragment(), View.OnClickListener {

    private lateinit var tasksViewModel: TasksViewModel
    var stateOfTasks = true
    private lateinit var myTasksAdapter: FirebaseRecyclerAdapter<TaskInfo, TaskHolder>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tasks, container, false)
        root.nextMonth.setOnClickListener(this)
        root.thisMonth.setOnClickListener(this)
        initRecyclerView(root)


        return root
    }

    class TaskHolder(v: View): RecyclerView.ViewHolder(v) {
        val time = v.task_time
        val description = v.task_description
        val title = v.task_title
        val image = v.task_image
    }

    private fun initRecyclerView(root: View) {
        recyclerView = root.findViewById<RecyclerView>(R.id.task_recycler)
        val ref = Firebase.database.reference.child("tasks").child(FirebaseAuth.getInstance().currentUser!!.uid)

        val options = FirebaseRecyclerOptions.Builder<TaskInfo>()
            .setQuery(ref, TaskInfo::class.java)
            .build()

        myTasksAdapter = object : FirebaseRecyclerAdapter<TaskInfo, TaskHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_task, parent, false)
                return TaskHolder(view)
            }

            override fun onBindViewHolder(holder: TaskHolder, position: Int, model: TaskInfo) {
                holder.description.text = model.description
                holder.time.text = model.time
                holder.title.text = model.title
            }

            override fun getItemCount(): Int {
                return super.getItemCount()
            }

        }

        recyclerView.adapter = myTasksAdapter
        recyclerView.smoothScrollToPosition(myTasksAdapter.itemCount)
        myTasksAdapter.startListening()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.nextMonth -> {
                if (stateOfTasks) {
                    stateOfTasks = false
                    nextView.setBackgroundColor(resources.getColor(R.color.color_red))
                    thisView.setBackgroundColor(resources.getColor(R.color.white))
                    nextMonth.setTextColor(resources.getColor(R.color.color_red))
                    thisMonth.setTextColor(resources.getColor(R.color.gg))
                }

            }
            R.id.thisMonth -> {
                if (!stateOfTasks) {
                    stateOfTasks = true
                    thisView.setBackgroundColor(resources.getColor(R.color.color_red))
                    nextView.setBackgroundColor(resources.getColor(R.color.white))
                    thisMonth.setTextColor(resources.getColor(R.color.color_red))
                    nextMonth.setTextColor(resources.getColor(R.color.gg))

                }
            }
        }
    }

    //private fun init
}