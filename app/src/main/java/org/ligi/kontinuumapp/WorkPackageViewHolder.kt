package org.ligi.kontinuumapp

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.View
import kontinuum.model.StageInfo
import kontinuum.model.StageStatus
import kontinuum.model.WorkPackage
import kontinuum.model.WorkPackageStatus
import kotlinx.android.synthetic.main.item_workpackage.view.*

fun WorkPackage.isSuccess() = stageInfoList.fold(true, { b: Boolean, stageInfo: StageInfo -> b && stageInfo.status != StageStatus.ERROR })
class WorkPackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(workPackage: WorkPackage) {
        itemView.commit_msg_text.text = workPackage.branch + ":" + workPackage.commitMessage


        itemView.status_image.setImageResource(when (workPackage.workPackageStatus) {
            WorkPackageStatus.PROCESSING ->
                R.drawable.ic_build_black_24dp
            WorkPackageStatus.PENDING ->
                R.drawable.ic_hourglass_empty_black_24dp

            WorkPackageStatus.FINISHED ->
            if(workPackage.isSuccess()) R.drawable.ic_sentiment_satisfied_black_24dp else R.drawable.ic_sentiment_dissatisfied_black_24dp

        })

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
