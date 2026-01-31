import com.webinane.salam.data.local.NotificationPreference
import com.webinane.salam.scheduler.AlarmScheduler
import com.webinane.salam.scheduler.AndroidAlarmScheduler
import com.webinane.salam.ui.viewmodel.PrayerTimesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NotificationPreference(androidContext()) }
    single { com.webinane.salam.util.NotificationHelper(androidContext()) }
    single<AlarmScheduler> { AndroidAlarmScheduler(androidContext()) }
    viewModel { PrayerTimesViewModel(get()) }
}
