package org.ligi.kontinuumapp

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.View
import kontinuum.model.WorkPackage
import kotlinx.android.synthetic.main.item_workpackage.view.*

class WorkPackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(workPackage: WorkPackage) {
        itemView.commit_msg_text.text = workPackage.branch + ":" + workPackage.commitMessage
        itemView.status_text.text = workPackage.workPackageStatus.name
        itemView.project_text.text = workPackage.project

        itemView.time_text.text = workPackage.epochSeconds.toTime()

        val stageInfoText = workPackage.stageInfoList.map {
            it.stage + " " + it.status + " " +

                    if (it.endEpochSeconds != null) {
                        (it.endEpochSeconds!! - it.startEpochSeconds).toString() + "s"
                    } else {
                        it.startEpochSeconds.toTime()
                    }

        }.joinToString("\n")
        itemView.stateinfo_text.text = stageInfoText
    }


    fun Long.toTime() = DateUtils.getRelativeDateTimeString(itemView.context, this * 1000,
            DateUtils.SECOND_IN_MILLIS,
            DateUtils.WEEK_IN_MILLIS,
            0
    )!!

}
