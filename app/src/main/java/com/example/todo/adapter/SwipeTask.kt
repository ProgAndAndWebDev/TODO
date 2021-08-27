package com.example.todo.adapter

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeTask(context: Context?) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    val deleteColor = ContextCompat.getColor(context!!, R.color.deleteColor)
    val completeColor = ContextCompat.getColor(context!!, R.color.completeColor)
    val deleteIcon = R.drawable.ic_baseline_delete_24
    val completeIcon = R.drawable.ic_checked

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeRightBackgroundColor(completeColor)
            .addSwipeRightActionIcon(completeIcon)
            .create()
            .decorate()
    }
}