package com.gstory.flutter_baiduad.interstitialad

import android.annotation.SuppressLint
import android.app.Activity
import com.baidu.mobads.sdk.api.ExpressInterstitialAd
import com.baidu.mobads.sdk.api.ExpressInterstitialListener
import com.gstory.flutter_baiduad.FlutterBaiduAdEventPlugin
import com.gstory.flutter_tencentad.Log2Util

/**
 * @Author: gstory
 * @CreateDate: 2021/11/24 5:36 下午
 * @Description: 模板插屏
 */

@SuppressLint("StaticFieldLeak")
object ExpressInsertAd : ExpressInterstitialListener {
    private val TAG = "InterstitialAd"

    private lateinit var context: Activity

    private var codeId: String? = null
    private var isFullScreen: Boolean? = false

    private var interstitialAd: ExpressInterstitialAd? = null

    fun init(context: Activity, params: Map<*, *>) {
        this.context = context
        this.codeId = params["androidId"] as String
        this.isFullScreen = params["isFullScreen"] as Boolean
        loadInterstitialAd()
    }

    //预加载插屏广告
    private fun loadInterstitialAd() {
        interstitialAd = ExpressInterstitialAd(context, codeId)
        interstitialAd?.setLoadListener(this)
        // 设置下载弹窗，默认为false
        interstitialAd?.setDialogFrame(true);
        interstitialAd?.load()
    }

    //显示插屏广告
    @SuppressLint("StaticFieldLeak")
    fun showInterstitialAd() {
        Log2Util.e("$TAG  showInterstitialAd  ${interstitialAd?.isReady}")
        if (interstitialAd == null || !interstitialAd?.isReady!!) {
            var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onUnReady")
            FlutterBaiduAdEventPlugin.sendContent(map)
            Log2Util.e("$TAG  素材未准备好")
            return
        }
        interstitialAd?.show()
    }


    //插屏广告加载成功
    override fun onADLoaded() {
        Log2Util.e("$TAG  插屏广告加载成功")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onReady")
        FlutterBaiduAdEventPlugin.sendContent(map)
    }

    //插屏广告点击
    override fun onAdClick() {
        Log2Util.e("$TAG  插屏广告点击")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onClick")
        FlutterBaiduAdEventPlugin.sendContent(map)
    }

    //广告被关闭
    override fun onAdClose() {
        Log2Util.e("$TAG  插屏广告点击")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onClose")
        FlutterBaiduAdEventPlugin.sendContent(map)
    }

    //广告请求失败
    override fun onAdFailed(p0: Int, p1: String?) {
        Log2Util.e("$TAG  插屏加载失败")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onFail", "code" to p0, "message" to p1)
        FlutterBaiduAdEventPlugin.sendContent(map)
    }

    override fun onNoAd(p0: Int, p1: String?) {
        Log2Util.e("$TAG  模版插屏无广告 $p0  $p1")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onFail", "code" to p0, "message" to p1)
        FlutterBaiduAdEventPlugin.sendContent(map)
    }

    //	广告曝光成功
    override fun onADExposed() {
        Log2Util.e("$TAG  模版插屏曝光")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onExpose")
        FlutterBaiduAdEventPlugin.sendContent(map)
    }

    //广告曝光失败
    override fun onADExposureFailed() {
        Log2Util.e("$TAG  模版插屏曝光失败")
    }

    //	视频缓存成功
    override fun onVideoDownloadSuccess() {
        Log2Util.e("$TAG  模版插屏视频缓存成功")
    }

    //	视频缓存失败
    override fun onVideoDownloadFailed() {
        Log2Util.e("$TAG  模版插屏视频缓存失败")
    }

    override fun onLpClosed() {
        Log2Util.e("$TAG  模版插屏onLpClosed")
    }
}