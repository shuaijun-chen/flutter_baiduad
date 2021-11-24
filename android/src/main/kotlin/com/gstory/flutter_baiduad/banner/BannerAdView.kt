package com.gstory.flutter_baiduad.banner

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.baidu.mobads.sdk.api.AdSize
import com.baidu.mobads.sdk.api.AdView
import com.baidu.mobads.sdk.api.AdViewListener
import com.gstory.flutter_baiduad.FlutterBaiduAdConfig
import com.gstory.flutter_baiduad.rewardad.RewardAd
import com.gstory.flutter_tencentad.LogUtil
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import org.json.JSONObject

/**
 * @Author: gstory
 * @CreateDate: 2021/11/22 5:54 下午
 * @Description: 描述
 */

class BannerAdView(var activity: Activity,
                   messenger: BinaryMessenger?,
                   id: Int,
                   params: Map<String?, Any?>) : PlatformView, AdViewListener {

    private val TAG = "BannerAdView"

    private var mContainer: FrameLayout? = null

    private var adView: AdView? = null

    //广告所需参数
    private var codeId: String
    private var appSid: String?
    private var autoplay: Boolean
    private var viewWidth: Double
    private var viewHeight: Double

    private var channel: MethodChannel

    init {
        codeId = params["androidId"] as String
        appSid = params["appSid"] as String
        autoplay = params["autoplay"] as Boolean
        viewWidth = params["viewWidth"] as Double
        viewHeight = params["viewHeight"] as Double
        channel = MethodChannel(messenger, FlutterBaiduAdConfig.bannerAdView + "_" + id)
        LogUtil.e("$TAG  $params")
        loadBannerAd()
    }

    private fun loadBannerAd() {
        mContainer = FrameLayout(activity)
        mContainer?.layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        mContainer?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
//        adView = AdView(activity, null, autoplay, AdSize.Banner, codeId)
        adView = AdView(activity, codeId)
        //支持动态设置APPSID，该信息可从移动联盟获得
        if (!appSid.isNullOrEmpty()) {
            adView?.setAppSid(appSid)
        }
        adView?.setListener(this)
        mContainer?.removeAllViews()
//        val rllp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(viewWidth.toInt(), viewHeight.toInt())
//        rllp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
//        mContainer?.addView(adView, rllp)
        mContainer?.addView(adView)
    }

    override fun getView(): View {
        return mContainer!!
    }

    //广告加载成功回调，表示广告相关的资源已经加载完毕，Ready To Show
    override fun onAdReady(p0: AdView?) {
        if (p0 == null) {
            LogUtil.e("$TAG  Banner广告加载失败 adView不存在或已销毁")
            var map: MutableMap<String, Any?> = mutableMapOf("code" to 0, "message" to "adView不存在或已销毁")
            channel.invokeMethod("onFail", map)
            return
        }
    }

    //当广告展现时发起的回调
    override fun onAdShow(p0: JSONObject?) {
        LogUtil.e("$TAG  Banner广告展现  $p0")
        channel.invokeMethod("onShow", "")
    }

    //当广告点击时发起的回调
    override fun onAdClick(p0: JSONObject?) {
        LogUtil.e("$TAG  Banner广告点击  $p0")
        channel.invokeMethod("onClick", "")
    }

    //	广告加载失败，error对象包含了错误码和错误信息
    override fun onAdFailed(p0: String?) {
        LogUtil.e("$TAG  Banner广告加载失败 $p0")
        var map: MutableMap<String, Any?> = mutableMapOf("code" to 0, "message" to p0)
        channel.invokeMethod("onFail", map)
    }

    override fun onAdSwitch() {
        LogUtil.e("$TAG  Banner onAdSwitch")
    }

    //当广告关闭时调用
    override fun onAdClose(p0: JSONObject?) {
        LogUtil.e("$TAG  Banner广告关闭")
        channel.invokeMethod("onClose", "")
    }

    override fun dispose() {
        adView?.destroy()
        adView = null
    }
}