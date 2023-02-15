package hr.fika.budgeapp.common.analytics

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import hr.fika.budgeapp.common.analytics.model.Event

object AnalyticsManager {

    fun logEvent(event: Event) {
        Firebase.analytics.logEvent(event.getEventName(), null)
    }

    fun logNumberedEvent(event: Event, bundle: Bundle) {
        Firebase.analytics.logEvent(event.getEventName(), bundle)
    }
}