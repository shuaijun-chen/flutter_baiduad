import 'package:flutter/material.dart';
import 'package:flutter_baiduad/flutter_baiduad.dart';

/// @Author: gstory
/// @CreateDate: 2021/11/24 2:30 下午
/// @Email gstory0404@gmail.com
/// @Description: 开屏广告

class SplashPage extends StatefulWidget {
  const SplashPage({Key? key}) : super(key: key);

  @override
  _SplashPageState createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: FlutterBaiduad.splashAdView(
            //android广告ID
            androidId: "7792007",
            //ios广告ID
            iosId: "7792007",
            //请求超时时间 默认超时时间为4200，单位：毫秒
            fetchDelay: 3000,
            //是否显示下载类广告的“隐私”、“权限”等字段 默认值为true
            displayDownloadInfo: true,
            //是否限制点击区域，默认不限制
            limitClick: true,
            //是否展示点击引导按钮，默认不展示，若设置可限制点击区域，则此选项默认打开
            displayClick: true,
            //用户点击开屏下载类广告时，是否弹出Dialog
            // 此选项设置为true的情况下，会覆盖掉 {SplashAd.KEY_DISPLAY_DOWNLOADINFO} 的设置
            popDialogDownLoad: true,
            //开屏广告回调
            callBack: FlutterBaiduAdSplashCallBack(onShow: () {
              print("开屏广告显示了");
            }, onClick: () {
              print("开屏广告点击了");
            }, onFail: (code, message) {
              print("开屏广告加载失败了  $code  $message");
              Navigator.pop(context);
            }, onClose: () {
              print("开屏广告关闭了");
              Navigator.pop(context);
            })),
      ),
    );
  }
}
