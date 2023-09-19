package com.hcn.dynamicvpn.ui.guide

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.hcn.dynamicvpn.base.BaseViewModel
import com.hcn.dynamicvpn.ui.main.MainActivity

class GuideViewModel :BaseViewModel() {
    var liveStartToMain= MutableLiveData<Boolean>()
    var isCurrentPage: Boolean = false

    fun jumpPageModel(activity: AppCompatActivity){
        if (!isCurrentPage) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
        activity.finish()
    }
}