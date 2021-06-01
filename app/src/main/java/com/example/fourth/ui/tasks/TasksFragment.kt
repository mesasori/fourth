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
import com.example.fourth.R
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks.view.*

class TasksFragment : Fragment(), View.OnClickListener {

    private lateinit var tasksViewModel: TasksViewModel
    var stateOfTasks = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tasks, container, false)
        root.nextMonth.setOnClickListener {
            if (stateOfTasks) {
                stateOfTasks = false
                nextView.setBackgroundColor(resources.getColor(R.color.color_red))
                thisView.setBackgroundColor(resources.getColor(R.color.white))
                nextMonth.setTextColor(resources.getColor(R.color.color_red))
                thisMonth.setTextColor(resources.getColor(R.color.gg))
            }
        }

        root.thisMonth.setOnClickListener {
            if (!stateOfTasks) {
                stateOfTasks = true
                thisView.setBackgroundColor(resources.getColor(R.color.color_red))
                nextView.setBackgroundColor(resources.getColor(R.color.white))
                thisMonth.setTextColor(resources.getColor(R.color.color_red))
                nextMonth.setTextColor(resources.getColor(R.color.gg))

            }
        }

        return root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.nextMonth -> {
                Log.e("DSF", "ADSFBz")

            }
            R.id.thisMonth -> {

            }
        }
    }

    //private fun init
}