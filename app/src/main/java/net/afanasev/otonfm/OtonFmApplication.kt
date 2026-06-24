package net.afanasev.otonfm

import android.app.Application
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import net.afanasev.otonfm.data.status.StatusRepository

class OtonFmApplication : Application() {

    val statusRepository by lazy { StatusRepository() }

    override fun onCreate() {
        super.onCreate()

        Purchases.logLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.ERROR
        Purchases.configure(
            PurchasesConfiguration.Builder(this, BuildConfig.REVENUECAT_API_KEY).build()
        )
    }
}
