package com.gstory.flutter_baiduad

import android.app.Activity
import android.src.main.kotlin.com.gstory.flutter_baiduad.splash.SplashAdViewFactory
import com.gstory.flutter_baiduad.banner.BannerAdViewFactory
import io.flutter.embedding.engine.plugins.FlutterPlugin

/**
 * @Author: gstory
 * @CreateDate: 2021/11/22 5:50 下午
 * @Description: 描述
 */

object FlutterBaiduAdViewPlugin {
    fun registerWith(binding: FlutterPlugin.FlutterPluginBinding, activity: Activity) {
        //注册banner广告
        binding.platformViewRegistry.registerViewFactory(FlutterBaiduAdConfig.bannerAdView, BannerAdViewFactory(binding.binaryMessenger, activity))
        //注册splash广告
        binding.platformViewRegistry.registerViewFactory(FlutterBaiduAdConfig.splashAdView, SplashAdViewFactory(binding.binaryMessenger,activity))
//        //注册Express广告
//        binding.platformViewRegistry.registerViewFactory(FlutterBaiduAdConfig.nativeExpressAdView, NativeExpressAdViewFactory(binding.binaryMessenger,activity))
    }
}