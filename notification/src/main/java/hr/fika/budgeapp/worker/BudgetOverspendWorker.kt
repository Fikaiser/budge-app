package hr.fika.budgeapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import hr.fika.budgeapp.common.notification.NotificationKeys
import hr.fika.budgeapp.network.NotificationRepository

private const val NOTIFICATION_ID = 187
private const val CHANNEL_NAME = "overspendChannel"
private const val CHANNEL_DESC = "Notifications for budget overspend"

class BudgetOverspendWorker(
    private val context: Context,
    private val params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val userId = params.inputData.getInt(NotificationKeys.USER_ID.key, 0)
        if (userId != 0) {
            val shouldShow = NotificationRepository.getOverspendStatus(userId)
            if (shouldShow) {
                createNotificationChannel(context, CHANNEL_NAME, CHANNEL_DESC)
                createNotification(
                    context,
                    "Budget Overspend Alert",
                    "Some of your budgets are off target due to overspend",
                    NOTIFICATION_ID
                )
                return Result.success()
            }
        }
        return Result.failure()
    }
}