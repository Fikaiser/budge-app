package hr.fika.budgeapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import hr.fika.budgeapp.common.notification.NotificationKeys
import hr.fika.budgeapp.network.NotificationRepository


private const val NOTIFICATION_ID = 883
private const val CHANNEL_NAME = "paymentChannel"
private const val CHANNEL_DESC = "Notifications for pending payments"

class PendingPaymentWorker(
    private val context: Context,
    private val params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val accountId = params.inputData.getInt(NotificationKeys.ACCOUNT_ID.key, 0)
        if (accountId != 0) {
            val shouldShow = NotificationRepository.getPendingPaymentStatus(accountId)
            if (shouldShow) {
                createNotificationChannel(context, CHANNEL_NAME, CHANNEL_DESC)
                createNotification(
                    context,
                    "Pending Payment Alert",
                    "You have pending payments due",
                    NOTIFICATION_ID
                )
                return Result.success()
            }
        }
        return Result.failure()
    }
}

