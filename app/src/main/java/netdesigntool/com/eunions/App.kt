package netdesigntool.com.eunions

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference

@HiltAndroidApp
class App: Application()

{
    companion object{
        lateinit var cont : WeakReference<Context>
        fun getAppContext() : Context? = cont.get()
    }

    override fun onCreate() {
        super.onCreate()
        cont=WeakReference(applicationContext)
    }
}