package com.supcoder.fundcrawler

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * SApplication
 *
 * @author lee
 * @date 2017/11/14
 */
class SApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().name("fund")
                .schemaVersion(1).build())
    }
}