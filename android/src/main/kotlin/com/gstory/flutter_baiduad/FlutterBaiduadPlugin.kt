package com.gstory.flutter_baiduad

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import com.baidu.mobads.sdk.api.AdSettings

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import com.baidu.mobads.sdk.api.BDDialogParams

import com.baidu.mobads.sdk.api.BDAdConfig
import com.baidu.mobads.sdk.api.MobadsPermissionSettings
import com.bun.miitmdid.core.MdidSdkHelper
import com.bun.miitmdid.interfaces.IIdentifierListener
import com.bun.miitmdid.interfaces.IdSupplier
import com.gstory.flutter_baiduad.rewardad.RewardAd
import com.gstory.flutter_tencentad.LogUtil
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.os.Process
import android.app.Application.getProcessName
import android.webkit.WebView
import com.gstory.flutter_baiduad.fullvideo.FullVideoAd
import com.gstory.flutter_baiduad.interstitialad.ExpressInsertAd
import com.gstory.flutter_baiduad.interstitialad.InsertAd


/** FlutterBaiduadPlugin */
class FlutterBaiduadPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private var applicationContext: Context? = null
    private var mActivity: Activity? = null
    private var mFlutterPluginBinding: FlutterPlugin.FlutterPluginBinding?  = null

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        mActivity = binding.activity
        Log.e("FlutterUnionadPlugin->", "onAttachedToActivity")
        FlutterBaiduAdViewPlugin.registerWith(mFlutterPluginBinding!!, mActivity!!)
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        mActivity = binding.activity
        Log.e("FlutterUnionadPlugin->", "onReattachedToActivityForConfigChanges")
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_baiduad")
        channel.setMethodCallHandler(this)
        applicationContext = flutterPluginBinding.applicationContext
        mFlutterPluginBinding = flutterPluginBinding
        FlutterBaiduAdEventPlugin().onAttachedToEngine(flutterPluginBinding)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        mActivity = null
        Log.e("FlutterUnionadPlugin->", "onDetachedFromActivityForConfigChanges")
    }

    override fun onDetachedFromActivity() {
        mActivity = null
        Log.e("FlutterUnionadPlugin->", "onDetachedFromActivity")
    }

    private fun getProcessName(context: Context?): String? {
        if (context == null) return null
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        //注册初始化
        if (call.method == "register") {
            val appId = call.argument<String>("androidAppId")
            val appName = call.argument<String>("appName")
            val debug = call.argument<Boolean>("debug")
            val bdAdConfig = BDAdConfig.Builder()
                    .setAppName(appName)
                    .setAppsid(appId)
                    .setHttps(true)
                    .setDialogParams(BDDialogParams.Builder()
                            .setDlDialogType(BDDialogParams.TYPE_BOTTOM_POPUP)
                            .setDlDialogAnimStyle(BDDialogParams.ANIM_STYLE_NONE)
                            .build())
                    .build(applicationContext)
            bdAdConfig.init()
            LogUtil.setAppName("flutter_baiduad")
            LogUtil.setShow(debug!!)
            result.success(true)
            //隐私敏感权限API&限制个性化广告推荐
        } else if (call.method == "privacy") {
            MobadsPermissionSettings.setPermissionReadDeviceID(call.argument<Boolean>("readDeviceID")!!)
            MobadsPermissionSettings.setPermissionLocation(call.argument<Boolean>("location")!!)
            MobadsPermissionSettings.setPermissionStorage(call.argument<Boolean>("storage")!!)
            MobadsPermissionSettings.setPermissionAppList(call.argument<Boolean>("appList")!!)
            MobadsPermissionSettings.setLimitPersonalAds(call.argument<Boolean>("personalAds")!!)
            result.success(true)
            //获取sdk版本
        } else if (call.method == "getSDKVersion") {
            result.success(AdSettings.getSDKVersion())
        } else if (call.method == "getOAID") {
            //获取 oaid
            try {
                MdidSdkHelper.InitSdk(applicationContext, true, object : IIdentifierListener {
                    override fun OnSupport(p0: Boolean, p1: IdSupplier?) {
                        if (p1 == null) {
                            LogUtil.d("MdidSdkHelper初始化失败")
                            result.success("")
                            return
                        }
                        var oaid = p1.oaid
                        mActivity!!.runOnUiThread(Runnable {
                            result.success(oaid)
                        })
                        var vaid = p1.vaid
                        var aaid = p1.aaid
                        LogUtil.d("$p1")
                    }
                })
            }catch (e : Exception){
                result.success("")
            }
            //预加载激励广告
        } else if (call.method == "loadRewardAd") {
            RewardAd.init(applicationContext!!, call.arguments as Map<*, *>)
            //展示激励广告
        } else if (call.method == "showRewardAd") {
            RewardAd.showAd()
            result.success(true)
            //预加载插屏广告
        } else if (call.method == "loadInterstitialAd") {
            ExpressInsertAd.init(mActivity!!,call.arguments as Map<*, *>)
            result.success(true)
            //展示插屏广告
        } else if (call.method == "showInterstitialAd") {
            ExpressInsertAd.showInterstitialAd()
            result.success(true)
            //预加载插屏广告
        } else if (call.method == "loadFullVideoAd") {
            FullVideoAd.init(mActivity!!,call.arguments as Map<*, *>)
            result.success(true)
            //展示插屏广告
        } else if (call.method == "showFullVideoAd") {
            FullVideoAd.showFullVideoAd()
            result.success(true)
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        FlutterBaiduAdEventPlugin().onDetachedFromEngine(binding)
    }
}
