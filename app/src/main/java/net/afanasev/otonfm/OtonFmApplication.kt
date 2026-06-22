package net.afanasev.otonfm

import android.app.Application
import net.afanasev.otonfm.data.images.ImagesRepository
import net.afanasev.otonfm.data.status.StatusRepository

class OtonFmApplication : Application() {
    val statusRepository by lazy { StatusRepository() }
    val imagesRepository by lazy { ImagesRepository() }
}
