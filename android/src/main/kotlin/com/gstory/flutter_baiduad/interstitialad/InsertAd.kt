package com.gstory.flutter_baiduad.interstitialad

import android.app.Activity
import com.baidu.mobads.sdk.api.InterstitialAd
import com.baidu.mobads.sdk.api.InterstitialAdListener
import com.gstory.flutter_baiduad.FlutterBaiduAdEventPlugin
import com.gstory.flutter_tencentad.Log2Util

/**
 * @Author: gstory
 * @CreateDate: 2021/11/24 5:36 下午
 * @Description: 描述
 */

object InsertAd : InterstitialAdListener {
    private val TAG = "InterstitialAd"

    private lateinit var context: Activity

    private var codeId: String? = null
    private var isFullScreen: Boolean? = false

    private var interstitialAd: InterstitialAd? = null

    fun init(context: Activity, params: Map<*, *>) {
        this.context = context
        this.codeId = params["androidId"] as String
        this.isFullScreen = params["isFullScreen"] as Boolean
        loadInterstitialAd()
    }

    //预加载插屏广告
    fun loadInterstitialAd() {
        interstitialAd = InterstitialAd(context,codeId)
        interstitialAd?.setListener(this)
        interstitialAd?.loadAd()
    }

    //显示插屏广告
    fun showInterstitialAd(){
        if(interstitialAd ==null || !interstitialAd?.isAdReady!!){
            var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onUnReady")
            FlutterBaiduAdEventPlugin.sendContent(map)
            return
        }
        interstitialAd?.showAd()
    }

    //插屏广告加载成功
    override fun onAdReady() {
        Log2Util.e("$TAG  插屏广告加载成功")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onReady")
        FlutterBaiduAdEventPlugin.sendContent(map)
    }

    //插屏广告显示
    override fun onAdPresent() {
        Log2Util.e("$TAG  插屏广告显示")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd","onAdMethod" to "onShow")
        FlutterBaiduAdEventPlugin.sendContent(map)
    }

    //插屏广告点击
    override fun onAdClick(p0: InterstitialAd?) {
        Log2Util.e("$TAG  插屏广告点击")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd", "onAdMethod" to "onClick")
        FlutterBaiduAdEventPlugin.sendContent(map)
    }

    //插屏广告关闭
    override fun onAdDismissed() {
        Log2Util.e("$TAG  插屏广告关闭")
    }

    //广告加载失败
    override fun onAdFailed(p0: String?) {
        Log2Util.e("$TAG  插屏加载失败")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "interactAd","onAdMethod" to "onFail","code" to 0 , "message" to p0)
        FlutterBaiduAdEventPlugin.sendContent(map)
    }
}