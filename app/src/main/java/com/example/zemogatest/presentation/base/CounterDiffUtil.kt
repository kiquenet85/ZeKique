package com.example.zemogatest.presentation.base

import androidx.recyclerview.widget.DiffUtil

class CounterDiffUtil(oldEmployeeList: List<Identifier>, newEmployeeList: List<Identifier>) :
    DiffUtil.Callback() {

    private val oldCounterList: List<Identifier> = oldEmployeeList
    private val newCounterList: List<Identifier> = newEmployeeList

    override fun getOldListSize(): Int {
        return oldCounterList.size
    }

    override fun getNewListSize(): Int {
        return newCounterList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCounterList[oldItemPosition].getId() == newCounterList[newItemPosition].getId()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee: Identifier = oldCounterList[oldItemPosition]
        val newEmployee: Identifier = newCounterList[newItemPosition]
        return oldEmployee == newEmployee
    }
}

interface Identifier {
    fun getId() : String
}