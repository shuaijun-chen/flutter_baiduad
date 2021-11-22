package android.src.main.kotlin.com.gstory.flutter_baiduad.splash

import android.app.Activity
import android.content.Context
import com.gstory.flutter_baiduad.splash.SplashAdView
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

/**
 * @Author: gstory
 * @CreateDate: 2021/11/22 5:54 下午
 * @Description: 描述
 */

internal class SplashAdViewFactory(private val messenger: BinaryMessenger, private val activity: Activity) : PlatformViewFactory(
        StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, id: Int, args: Any): PlatformView {
        val params = args as Map<String?, Any?>
        return SplashAdView(activity,messenger, id, params)
    }
}