package com.gstory.flutter_baiduad.fullvideo

import android.annotation.SuppressLint
import android.content.Context
import com.baidu.mobads.sdk.api.FullScreenVideoAd
import com.baidu.mobads.sdk.api.RewardVideoAd
import com.gstory.flutter_baiduad.FlutterBaiduAdEventPlugin
import com.gstory.flutter_baiduad.rewardad.RewardAd
import com.gstory.flutter_tencentad.LogUtil

/**
 * @Author: gstory
 * @CreateDate: 2021/11/30 3:56 下午
 * @Description: 描述
 */

@SuppressLint("StaticFieldLeak")
object FullVideoAd : FullScreenVideoAd.FullScreenVideoAdListener {
    private val TAG = "FullVideoAd"
    private lateinit var context: Context
    private var fullScreenVideoAd: FullScreenVideoAd? = null
    @SuppressLint("StaticFieldLeak")
    private var codeId: String? = null
    private var useSurfaceView: Boolean? = false

    fun init(context: Context, params: Map<*, *>) {
        this.context = context
        this.codeId = params["androidId"] as String
        this.useSurfaceView = params["useSurfaceView"] as Boolean
        loadFullVideoAd()
    }

    private fun loadFullVideoAd() {
        fullScreenVideoAd = FullScreenVideoAd(context, codeId,this)
        fullScreenVideoAd?.load()
    }

    fun showFullVideoAd() {
        if(fullScreenVideoAd == null || !fullScreenVideoAd?.isReady!!){
//            var map: MutableMap<String, Any?> = mutableMapOf("adType" to "fullVideoAd", "onAdMethod" to "onUnReady")
//            FlutterBaiduAdEventPlugin.sendContent(map)
            return
        }
        fullScreenVideoAd?.show()
    }

    //广告展示回调
    override fun onAdShow() {
        LogUtil.d("$TAG 全屏视频广告展示")
    }

    //点击时回调
    override fun onAdClick() {
        LogUtil.d("$TAG 全屏视频广告展示")
    }

    //关闭回调，附带播放进度
    override fun onAdClose(p0: Float) {
        LogUtil.d("$TAG 全屏视频广告关闭回调，附带播放进度 $p0")
    }

    //广告加载失败
    override fun onAdFailed(p0: String?) {
        LogUtil.d("$TAG 全屏视频广告加载失败 $p0")
    }

    //视频物料缓存成功
    override fun onVideoDownloadSuccess() {
        LogUtil.d("$TAG 全屏视频广告视频物料缓存成功")
    }

    //视频物料缓存失败
    override fun onVideoDownloadFailed() {
        LogUtil.d("$TAG 全屏视频广告视频物料缓存失败")
    }

    //播放完成回调
    override fun playCompletion() {
        LogUtil.d("$TAG 全屏视频广告播放完成回调")
    }

    //广告跳过回调，附带播放进度
    override fun onAdSkip(p0: Float) {
        LogUtil.d("$TAG 全屏视频广告跳过回调，附带播放进度 $p0")
    }

    //广告加载成功
    override fun onAdLoaded() {
        LogUtil.d("$TAG 全屏视频广告加载成功")
        showFullVideoAd()
    }
}