package it.fox.geofencepoc.broadcastreceivers

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import it.fox.geofencepoc.services.LocationProximityService
import kotlin.properties.Delegates

const val APP_STARTED = "it.fox.geofencepoc.APP_STARTED"

const val BOOT_EVENT = "android.intent.action.BOOT_COMPLETED"
class LocationProximityStarter: BroadcastReceiver() {

    var lastScheduledJob by Delegates.notNull<Int>();
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context!=null && !jobAlreadyScheduled(context))
            scheduleJob(context);
        if (context!=null && !isServiceRunning()) {
            val action = intent?.action
            when (action) {
                APP_STARTED -> startService(context);
                BOOT_EVENT -> startService(context)
            }
        }
    }

    fun scheduleJob(context:Context) {
        val serviceComponent = ComponentName(context, LocationProximityService::class.java);
        val builder = JobInfo.Builder(0, serviceComponent);
        builder.setPeriodic(1000)
            .setPersisted(true)
            .setRequiresCharging(false)
        val jobScheduler = context.getSystemService(JobScheduler::class.java)
        val jobInfo: JobInfo = builder.build();
        jobScheduler.schedule(jobInfo);
        lastScheduledJob=jobInfo.getId();
    }

    fun startService(context:Context){
        context.startForegroundService(Intent(context,LocationProximityService::class.java))
    }

    private fun jobAlreadyScheduled(context: Context): Boolean {
        val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        for (jobInfo in scheduler.allPendingJobs) {
            if (jobInfo.id == lastScheduledJob) {
                return true
            }
        }
        return false
    }

    fun isServiceRunning(): Boolean {
        return LocationProximityService.isRunning
    }
}