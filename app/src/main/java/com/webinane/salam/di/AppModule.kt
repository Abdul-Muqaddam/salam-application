import androidx.work.WorkManager
import com.webinane.salam.data.local.PrayerDatabase
import com.webinane.salam.data.local.NotificationPreference
import com.webinane.salam.data.local.DhikrPreference
import com.webinane.salam.data.local.DuaPreference
import com.webinane.salam.data.local.PrayerSchedulePreference
import com.webinane.salam.domain.repository.PrayerRepository
import com.webinane.salam.domain.repository.UserRepository
import com.webinane.salam.domain.repository.LocationRepository
import com.webinane.salam.data.repository.PrayerRepositoryImpl
import com.webinane.salam.data.repository.LocationRepositoryImpl
import com.webinane.salam.data.repository.UserRepositoryImpl
import com.webinane.salam.domain.usecase.GetPrayerTimesUseCase
import com.webinane.salam.domain.usecase.GetCountdownUseCase
import com.webinane.salam.domain.usecase.GetHighlightUseCase
import com.webinane.salam.domain.usecase.SyncPrayerTimesUseCase
import com.webinane.salam.scheduler.AlarmScheduler
import com.webinane.salam.scheduler.AndroidAlarmScheduler
import com.webinane.salam.ui.admin.AdminScheduleViewModel
import com.webinane.salam.ui.zakat.ZakatViewModel
import com.webinane.salam.ui.PrayerTimesViewModel
import com.webinane.salam.ui.home.DhikrViewModel
import com.webinane.salam.ui.ramadan.IslamicCalendarViewModel
import com.webinane.salam.ui.ramadan.UserViewModel
import com.webinane.salam.ui.qibla.QiblaViewModel
import com.webinane.salam.util.TtsManager
import com.webinane.salam.worker.PrayerSyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module {
    single { NotificationPreference(androidContext()) }
    single { DhikrPreference(androidContext()) }
    single { DuaPreference(androidContext()) }
    single { PrayerSchedulePreference(androidContext()) }
    single { com.webinane.salam.util.NotificationHelper(androidContext()) }
    single { TtsManager(androidContext()) }
    single { com.webinane.salam.util.LocationManager(androidContext()) }
    single { com.webinane.salam.util.PrayerTimeCalculator() }
    single { com.webinane.salam.util.FCMTokenProvider() }
    single { com.webinane.salam.util.NotificationSender(get()) }
    single { com.webinane.salam.util.AudioPlayerManager(androidContext()) }
    single<AlarmScheduler> { AndroidAlarmScheduler(androidContext()) }
    
    // Room
    single { PrayerDatabase.getDatabase(androidContext()) }
    single { get<PrayerDatabase>().prayerDao() }
    
    // Firebase
    single { com.google.firebase.database.FirebaseDatabase.getInstance() }

    // Retrofit
    single {
        retrofit2.Retrofit.Builder()
            .baseUrl("https://api.alquran.cloud/v1/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .client(
                okhttp3.OkHttpClient.Builder()
                    .addInterceptor(okhttp3.logging.HttpLoggingInterceptor().apply { level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY })
                    .build()
            )
            .build()
    }
    single { get<retrofit2.Retrofit>().create(com.webinane.salam.data.remote.service.QuranApiService::class.java) }

    // Repositories
    single<PrayerRepository> { PrayerRepositoryImpl(get(), get(), get(), get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    single<com.webinane.salam.domain.repository.quran.QuranRepository> { com.webinane.salam.data.repository.quran.QuranRepositoryImpl(get()) }
    single<com.webinane.salam.domain.repository.DuaRepository> { com.webinane.salam.data.repository.DuaRepositoryImpl(get()) }
    
    // Use Cases
    single { GetPrayerTimesUseCase(get()) }
    single { GetCountdownUseCase() }
    single { GetHighlightUseCase() }
    single { SyncPrayerTimesUseCase(get()) }
    single { com.webinane.salam.domain.usecase.quran.GetSurahsUseCase(get()) }
    single { com.webinane.salam.domain.usecase.quran.GetSurahDetailsUseCase(get()) }
    
    // WorkManager
    single { WorkManager.getInstance(androidContext()) }
    worker { PrayerSyncWorker(get(), get(), get()) }
    
    // Metal Price Service
    single { com.webinane.salam.data.remote.MetalPriceService() }
    
    viewModelOf(::PrayerTimesViewModel)
    viewModelOf(::DhikrViewModel)
    viewModelOf(::IslamicCalendarViewModel)
    viewModel { ZakatViewModel(get()) }
    viewModelOf(::QiblaViewModel)
    viewModelOf(::UserViewModel)
    viewModel { com.webinane.salam.ui.quran.QuranViewModel(get(), get(), get()) }
    viewModel { com.webinane.salam.ui.dua.DuaViewModel(get()) }
    viewModel { AdminScheduleViewModel(get(), get(), get(), get()) }
}
