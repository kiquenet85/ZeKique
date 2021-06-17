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
        return oldCounterList[oldItemPosition].getIdentifier() == newCounterList[newItemPosition].getIdentifier()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldObject: Identifier = oldCounterList[oldItemPosition]
        val newObject: Identifier = newCounterList[newItemPosition]
        return oldObject == newObject
    }
}

interface Identifier {
    fun getIdentifier(): String
}