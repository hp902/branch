package com.example.branch.base

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.branch.utils.Constants
import com.example.branch.utils.LoadingDialog
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {

    protected val sharedPreferences: SharedPreferences by inject()

    private var mDelayHandler: Handler? = null

    private val mRunnable: Runnable = Runnable {
        (supportFragmentManager.findFragmentByTag(LoadingDialog::class.java.simpleName) as? LoadingDialog)?.dismissAllowingStateLoss()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
        initListener()
        mDelayHandler = Handler(Looper.getMainLooper())
    }

    abstract fun initView()

    abstract fun initData()

    abstract fun initListener()

    fun showLoadingDialog(status: Boolean) {
        val fm = supportFragmentManager
        if (status) {
            LoadingDialog.newInstance().show(fm, LoadingDialog::class.java.simpleName)
        } else {
            if (mDelayHandler != null) {
                mDelayHandler?.postDelayed(mRunnable, Constants.DIALOG_DELAY)
            }
        }
    }
}



