package com.example.doggallery

import android.app.Application

class DogApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDependencies.init(this)
    }
}