import androidx.work.WorkManager
import com.webinane.salam.data.local.PrayerDatabase
import com.webinane.salam.data.local.NotificationPreference
import com.webinane.salam.data.local.DhikrPreference
import com.webinane.salam.data.repository.PrayerRepositoryImpl
import com.webinane.salam.scheduler.AlarmScheduler
import com.webinane.salam.scheduler.AndroidAlarmScheduler
import com.webinane.salam.domain.repository.PrayerRepository
import com.webinane.salam.domain.usecase.GetPrayerTimesUseCase
import com.webinane.salam.domain.usecase.GetCountdownUseCase
import com.webinane.salam.domain.usecase.GetHighlightUseCase
import com.webinane.salam.domain.usecase.SyncPrayerTimesUseCase
import com.webinane.salam.ui.viewmodel.PrayerTimesViewModel
import com.webinane.salam.ui.viewmodel.DhikrViewModel
import com.webinane.salam.ui.viewmodel.IslamicCalendarViewModel
import com.webinane.salam.util.TtsManager
import com.webinane.salam.worker.PrayerSyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { NotificationPreference(androidContext()) }
    single { DhikrPreference(androidContext()) }
    single { com.webinane.salam.util.NotificationHelper(androidContext()) }
    single { TtsManager(androidContext()) }
    single<AlarmScheduler> { AndroidAlarmScheduler(androidContext()) }
    
    // Room
    single { PrayerDatabase.getDatabase(androidContext()) }
    single { get<PrayerDatabase>().prayerDao() }
    
    // Repositories
    single<PrayerRepository> { PrayerRepositoryImpl(get(), get()) }
    
    // Use Cases
    single { GetPrayerTimesUseCase(get()) }
    single { GetCountdownUseCase() }
    single { GetHighlightUseCase() }
    single { SyncPrayerTimesUseCase(get()) }
    
    // WorkManager
    single { WorkManager.getInstance(androidContext()) }
    workerOf(::PrayerSyncWorker)
    
    viewModelOf(::PrayerTimesViewModel)
    viewModelOf(::DhikrViewModel)
    viewModelOf(::IslamicCalendarViewModel)
}
