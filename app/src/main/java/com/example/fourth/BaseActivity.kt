package com.example.fourth

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar


open class BaseActivity : Activity() {

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val tv = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
            tv.setTextColor(resources.getColor(R.color.white))
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
            tv.setTextColor(resources.getColor(R.color.color_main_light))
        }
        snackBar.show()
    }

    fun showProgressDialog(element: MaterialCardView) {
        element.visibility = View.VISIBLE
    }

    fun hideProgressDialog(element: MaterialCardView) {
        element.visibility = View.INVISIBLE
    }


}