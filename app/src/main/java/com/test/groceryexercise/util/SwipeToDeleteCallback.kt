package com.test.groceryexercise.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteCallback(private val context: Context) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT)
    }

    var mHelper: ItemTouchHelper? = null
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }

    abstract fun doDelete(viewHolder: RecyclerView.ViewHolder)

    open val title: String = "Remove"
    open val message: String = "Are you sure you want to remove?"
    open val keepButton: String = "Keep"
    open val removeButton: String = "Remove"
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d("myApp","Starting swipe")
        if (direction == ItemTouchHelper.RIGHT) {
            val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(removeButton, DialogInterface.OnClickListener { _, _ ->
                    doDelete(viewHolder)
                })
                .setNegativeButton(keepButton, DialogInterface.OnClickListener { _, _ ->
                    mHelper?.startSwipe(viewHolder)
                })
            builder.create().show()
        }
    }

}
