package com.fscloud.lib_base.ui.webview

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import com.blankj.utilcode.util.LogUtils
import com.fscloud.lib_base.R
import com.fscloud.lib_base.ui.BaseActivity
import com.fscloud.lib_base.utils.AndroidBarUtils
import com.fscloud.lib_base.view.MyOnClickListen
import kotlinx.android.synthetic.main.layout_webview.*
import kotlinx.android.synthetic.main.lib_base_activity_h5.*
import org.jetbrains.anko.startActivity

/**
 * @author shaojunpei
 * @data 2020/9/18
 * @describe 加载H5页面
 */

class H5Activity : BaseActivity() {

    companion object{
        fun launchActivity(context: Context,title: String,url: String){
            context.startActivity<H5Activity>(
                "title" to title,
                "url" to url)
        }
    }

    private var title = ""
    private var url = ""
    private var jsInterface: MyJSInterface? = null
    private lateinit var fragment: WebViewFragment

    override fun layoutId(): Int {
        return R.layout.lib_base_activity_h5
    }

    override fun initData() {
        val bundle = intent.extras
        bundle?.let {
            title = it.getString("title","")
            url = it.getString("url","")
            LogUtils.i("h5链接：$url")
        }
    }

    override fun initView() {
        AndroidBarUtils.setStatusBarColor(this, com.fscloud.lib_base.R.color.white)
        titleLayout.setTitle(title)
        loadFragment()
        setClickEvent()
    }

    override fun start() {
    }

    private fun loadFragment(){
        val bundle = Bundle()
        bundle.putString("url",url)
        fragment = WebViewFragment()
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    private fun setClickEvent(){
        titleLayout.setBackClickEvent(object : MyOnClickListen{
            override fun onClick() {
                if (fragment.mWebView.canGoBack()){
                    fragment.mWebView.goBack()
                }else {
                    finish()
                }
            }
        })
    }


}