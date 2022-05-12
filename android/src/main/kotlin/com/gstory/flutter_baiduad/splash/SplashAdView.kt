package com.gstory.flutter_baiduad.splash

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.baidu.mobads.sdk.api.RequestParameters
import com.baidu.mobads.sdk.api.SplashAd
import com.baidu.mobads.sdk.api.SplashInteractionListener
import com.gstory.flutter_baiduad.FlutterBaiduAdConfig
import com.gstory.flutter_tencentad.Log2Util
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

/**
 * @Author: gstory
 * @CreateDate: 2021/11/22 6:20 下午
 * @Description: 描述
 */

class SplashAdView(var activity: Activity,
                   messenger: BinaryMessenger?,
                   id: Int,
                   params: Map<String?, Any?>
) :
        PlatformView, SplashInteractionListener {

    private val TAG = "SplashAdView"

    private var mContainer: FrameLayout? = null
    private var channel: MethodChannel

    //广告所需参数
    private var codeId: String = params["androidId"] as String
    private var appSid: String = params["appSid"] as String
    private var fetchDelay: Int = params["fetchDelay"] as Int
    private var displayDownloadInfo: Boolean = params["displayDownloadInfo"] as Boolean
    private var limitClick: Boolean = params["limitClick"] as Boolean
    private var displayClick: Boolean = params["displayClick"] as Boolean
    private var popDialogDownLoad: Boolean = params["popDialogDownLoad"] as Boolean

    private var splashAd: SplashAd? = null

    init {
        mContainer = FrameLayout(activity)
        mContainer?.layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        mContainer?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        channel = MethodChannel(messenger, FlutterBaiduAdConfig.splashAdView + "_" + id)
        loadSplashAd()
    }

    private fun loadSplashAd() {
        val parameters = RequestParameters.Builder()
        // 请求超时时间 默认超时时间为4200，单位：毫秒
        parameters.addExtra(SplashAd.KEY_TIMEOUT, fetchDelay.toString())
        // 是否显示下载类广告的“隐私”、“权限”等字段（value为String类型） 默认值为true
        parameters.addExtra(SplashAd.KEY_DISPLAY_DOWNLOADINFO, displayDownloadInfo.toString())
        // 是否限制点击区域，默认不限制
//        parameters.addExtra(SplashAd.KEY_LIMIT_REGION_CLICK, limitClick.toString())
//        // 是否展示点击引导按钮，默认不展示，若设置可限制点击区域，则此选项默认打开
//        parameters.addExtra(SplashAd.KEY_DISPLAY_CLICK_REGION, displayClick.toString())
        // 用户点击开屏下载类广告时，是否弹出Dialog
        // 此选项设置为true的情况下，会覆盖掉 {SplashAd.KEY_DISPLAY_DOWNLOADINFO} 的设置
        parameters.addExtra(SplashAd.KEY_POPDIALOG_DOWNLOAD, popDialogDownLoad.toString())
        splashAd = SplashAd(activity, codeId, parameters.build(), this)
        splashAd?.setAppSid(appSid)
        splashAd?.loadAndShow(mContainer)
    }


    override fun getView(): View {
        return mContainer!!
    }


    //广告请求成功
    override fun onADLoaded() {
        Log2Util.e("$TAG  开屏广告请求成功")
    }

    //广告加载失败
    override fun onAdFailed(p0: String?) {
        Log2Util.e("$TAG  开屏广告加载失败 $p0")
        var map: MutableMap<String, Any?> = mutableMapOf("code" to 0, "message" to p0)
        channel.invokeMethod("onFail", map)
    }

    //广告落地页关闭
    override fun onLpClosed() {
        Log2Util.e("$TAG  开屏广告落地页关闭")
    }

    //广告成功展示
    override fun onAdPresent() {
        channel.invokeMethod("onShow", "")
    }

    //广告关闭
    override fun onAdDismissed() {
        Log2Util.e("$TAG  开屏广告关闭")
        channel.invokeMethod("onClose", "")
    }

    //广告被点击
    override fun onAdClick() {
        Log2Util.e("$TAG  开屏广告被点击")
        channel.invokeMethod("onClick", "")
    }

    //广告缓存成功
    override fun onAdCacheSuccess() {
        Log2Util.e("$TAG  开屏广告缓存成功")
    }

    //广告缓存失败
    override fun onAdCacheFailed() {
        Log2Util.e("$TAG  开屏广告缓存失败")
    }

    override fun dispose() {
        splashAd?.destroy()
        splashAd = null
    }

}