package guilherme.nogueira.bcavehicleapp

import android.app.Application
import guilherme.nogueira.bcavehicleapp.di.appModule
import guilherme.nogueira.domain.di.domainModule
import guilherme.nogueira.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                domainModule,
                appModule,
            )
        }
    }
}