package netdesigntool.com.eunions

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()

{
    companion object{
        lateinit var cont : Context
        fun getAppContext() : Context = cont
    }

    override fun onCreate() {
        super.onCreate()
        cont = applicationContext
    }
}