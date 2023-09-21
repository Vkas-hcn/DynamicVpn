package sdfhg

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import dy.na.mic.base.BaseViewModel
import sdfhh.CKNQ

class CKOA : BaseViewModel() {
    var liveStartToMain = MutableLiveData<Boolean>()
    var isCurrentPage: Boolean = false
    var isAdShowType: Int = 0
    fun jumpPageModel(activity: AppCompatActivity) {
        if (!isCurrentPage) {
            val intent = Intent(activity, CKNQ::class.java)
            activity.startActivity(intent)
        }
        activity.finish()
    }
}