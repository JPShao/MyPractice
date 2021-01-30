package com.fscloud.lib_base.ui.webview

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.fscloud.lib_base.R
import com.fscloud.lib_base.constant.Mapper
import com.fscloud.lib_base.ui.BaseFragment
import com.qmuiteam.qmui.widget.webview.QMUIWebViewClient
import kotlinx.android.synthetic.main.layout_webview.*

class WebViewFragment() : BaseFragment<Any?, Any?>() {


    constructor(jsInterface: MyJSInterface) : this() {
        this.jsInterface = jsInterface
    }

    private var jsInterface: MyJSInterface? = null
    private var url: String = ""

    override fun getLayoutId(): Int = R.layout.layout_webview

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        if (arguments != null) {
            url = requireArguments().getString(Mapper.H5, "")
        }
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress >= 100) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                }
            }


            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                callback?.invoke(origin, true, false)
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }


        }

//        mWebView.webViewClient = object : WebViewClient() {
//            override fun onReceivedSslError(
//                view: WebView?,
//                handler: SslErrorHandler?,
//                error: SslError?
//            ) {
//                super.onReceivedSslError(view, handler, error)
//             //   handler?.cancel() //默认的处理方式，WebView变成空白页
//                handler?.proceed()//接受证书
//               // handleMessage(Message msg) //其他处理
//            }
//        }

        mWebView.webViewClient =  QMUIWebViewClient(false,false)


            progressBar.progress = mWebView.progress
        // mWebView.isEnabled = false
        //添加与js的交互接口,起的名称与js代码中的接口名称要一致
        //mWebView.addJavascriptInterface(JavascriptCloseInterface(requireActivity()), "question")

        mWebView.requestFocusFromTouch()
        mWebView.requestFocus() //获取焦点
        val webSetting = mWebView.settings
        webSetting.useWideViewPort = true
        webSetting.loadWithOverviewMode = true
        webSetting.domStorageEnabled = true
        webSetting.defaultTextEncodingName = "UTF-8"
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webSetting.allowFileAccessFromFileURLs = true
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webSetting.allowUniversalAccessFromFileURLs = true
        //允许js调用
        webSetting.javaScriptEnabled = true
        //支持通过JS打开新窗口
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        //在File域下，能够执行任意的JavaScript代码，同源策略跨域访问能够对私有目录文件进行访问等
        webSetting.allowFileAccess = true
        //控制页面的布局(使所有列的宽度不超过屏幕宽度)
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        //是否允许在WebView中访问内容URL（Content Url），默认允许
        webSetting.allowContentAccess = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 两者都可以
            // 设置安全的来源
            webSetting.mixedContentMode = webSetting.mixedContentMode
        }
        //设置应用缓存
        //webSetting.setAppCacheEnabled(true)
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        //DOM存储API是否可用
        webSetting.domStorageEnabled = true
        //定位是否可用
        webSetting.setGeolocationEnabled(true)
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        mWebView.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                    mWebView.goBack() //后退
                    return@setOnKeyListener true //已处理
                }
            }
            false
        }
        mWebView.settings.setSupportZoom(true)
        mWebView.settings.builtInZoomControls = true
        //隐藏Zoom缩放按钮
        mWebView.settings.displayZoomControls = true
        mWebView.loadUrl(url)
        //添加js调本地的方法
        if (jsInterface != null) {
            mWebView.addJavascriptInterface(jsInterface!!, "jsInterface")
        }
    }

    override fun lazyLoad() {
    }

    override fun onDestroyView() {
        val parent = mWebView.parent
        if (parent != null) {
            (parent as ViewGroup).removeView(mWebView)
        }
        mWebView.stopLoading()
        //清除缓存
        mWebView.clearCache(true)
        mWebView.clearFormData()
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        mWebView.settings.javaScriptEnabled = false
        mWebView.clearHistory()
        mWebView.clearView()
        mWebView.removeAllViews()
        mWebView.destroy()
        super.onDestroyView()
        // 如果先调用destroy()方法，则会命中if (isDestroyed()) return
        // 这一行代码，需要先onDetachedFromWindow()，再
        // destory()

    }


}