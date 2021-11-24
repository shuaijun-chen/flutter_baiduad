import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_baiduad/flutter_baiduad.dart';
import 'package:flutter_baiduad_example/banner_page.dart';
import 'package:flutter_baiduad_example/splash_page.dart';

import 'native_page.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  bool? _isRegister;

  StreamSubscription? _adStream;

  String _sdkVersion = "";
  String _oaid = "";

  @override
  void initState() {
    super.initState();
    _initRegister();
    _adStream = FlutterBaiduAdStream.initAdStream(
        //激励广告结果监听
        flutterBaiduAdRewardCallBack: FlutterBaiduAdRewardCallBack(
          onShow: () {
            print("激励广告显示");
          },
          onClose: () {
            print("激励广告关闭");
          },
          onFail: (code, message) {
            print("激励广告失败 $code $message");
          },
          onClick: () {
            print("激励广告点击");
          },
          onSkip: () {
            print("激励广告跳过");
          },
          onReady: () {
            print("激励广告预加载准备就绪");
            //展示激励广告
            FlutterBaiduad.showRewardVideoAd();
          },
          onUnReady: () {
            print("激励广告预加载未准备就绪");
          },
          onFinish: () {
            print("激励广告完成");
          },
          onVerify: (verify, rewardName, rewardAmount) {
            print("激励广告奖励  $verify   $rewardName   $rewardAmount");
          },
        ),
        flutterBaiduAdInteractionCallBack: FlutterBaiduAdInteractionCallBack(
          onClose: () {
            print("插屏广告关闭了");
          },
          onFail: (code, message) {
            print("插屏广告出错了 $code $message");
          },
          onClick: () {
            print("插屏广告点击了");
          },
          onShow: () {
            print("插屏广告显示了");
          },
          onReady: () {
            print("插屏广告准备就绪");
            FlutterBaiduad.showInterstitialAd();
          },
          onUnReady: () {
            print("插屏广告未准备就绪");
          },
          onExpose: () {
            print("插屏广告曝光");
          },
        ));
  }

  //注册
  void _initRegister() async {
    _oaid = await FlutterBaiduad.getOAID();
    _isRegister = await FlutterBaiduad.register(
      androidAppId: "b423d90d",
      //穿山甲广告 Android appid 必填
      iosAppId: "b423d90d",
      //是否打印日志 发布时改为false
      debug: true,
    );
    //隐私敏感权限API&限制个性化广告推荐
    if (Platform.isAndroid) {
      await FlutterBaiduad.andridPrivacy(
        //读取设备ID的权限（建议授权）
        readDeviceID: false,
        //读取已安装应用列表权限（建议授权）
        appList: false,
        //读取粗略地理位置权限
        location: false,
        //读写外部存储权限
        storage: false,
        //设置限制个性化广告推荐
        personalAds: false,
      );
    }
    _sdkVersion = await FlutterBaiduad.getSDKVersion();
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('flutter_baiduad'),
        ),
        body: Center(
          child: Column(
            children: [
              Text('百青藤SDK初始化: $_isRegister\n'),
              Text('百青藤SDK版本: $_sdkVersion\n'),
              Text('Andorid信通院MSA_OAID $_oaid\n'),
              //激励广告
              MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: const Text('激励广告'),
                onPressed: () async {
                  await FlutterBaiduad.loadRewardAd(
                    //android广告id
                    androidId: "7792010",
                    //ios广告id
                    iosId: "7792010",
                    //支持动态设置APPSID，该信息可从移动联盟获得
                    appSid: "",
                    //用户id
                    userID: "123",
                    //奖励
                    rewardName: "100金币",
                    //奖励数
                    rewardAmount: 100,
                    //扩展参数 服务器验证使用
                    customData: "",
                    //是否使用SurfaceView
                    useSurfaceView: false,
                    //设置点击跳过时是否展示提示弹框
                    isShowDialog: true,
                    //设置是否展示奖励领取倒计时提示
                    useRewardCountdown: true,
                  );
                },
              ),
              //Banner广告
              MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: const Text('Banner横幅广告'),
                onPressed: () async {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) {
                        return BannerPage();
                      },
                    ),
                  );
                },
              ),
              //开屏广告
              MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: const Text('splash开屏广告'),
                onPressed: () async {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) {
                        return SplashPage();
                      },
                    ),
                  );
                },
              ),
              //信息流广告
              MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: const Text('native信息流广告'),
                onPressed: () async {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) {
                        return NativePage();
                      },
                    ),
                  );
                },
              ),
              //插屏广告
              MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: const Text('插屏广告'),
                onPressed: () async {
                  FlutterBaiduad.loadInterstitialAd(
                      androidId: "7792008",
                      iosId: "7792008",
                      isFullScreen: false);
                },
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    _adStream?.cancel();
    super.dispose();
  }
}
