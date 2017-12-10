package org.ligi.kontinuumapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kontinuum.model.WorkPackage

class WorkPackageAdapter(private val list: List<WorkPackage>) : RecyclerView.Adapter<WorkPackageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkPackageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workpackage, parent, false)
        return WorkPackageViewHolder(view)
    }

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: WorkPackageViewHolder, position: Int) = holder.bind(list[position])
}