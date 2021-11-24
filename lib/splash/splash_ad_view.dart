import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_baiduad/flutter_baiduad.dart';

/// @Author: gstory
/// @CreateDate: 2021/11/22 6:49 下午
/// @Email gstory0404@gmail.com
/// @Description: dart类作用描述

class SplashAdView extends StatefulWidget {
  final String androidId;
  final String iosId;
  final String? appSid;
  final int? fetchDelay;
  final bool? displayDownloadInfo;
  final bool? limitClick;
  final bool? displayClick;
  final bool? popDialogDownLoad;
  final FlutterBaiduAdSplashCallBack? callBack;

  const SplashAdView(
      {Key? key,
      required this.androidId,
      required this.iosId,
      this.appSid,
      this.fetchDelay,
      this.displayDownloadInfo,
      this.limitClick,
      this.displayClick,
      this.popDialogDownLoad,
      this.callBack})
      : super(key: key);

  @override
  _SplashAdViewState createState() => _SplashAdViewState();
}

class _SplashAdViewState extends State<SplashAdView> {

  final String _viewType = "com.gstory.flutter_baiduad/SplashAdView";

  MethodChannel? _channel;

  //广告是否显示
  bool _isShowAd = true;

  @override
  void initState() {
    super.initState();
    _isShowAd = true;
  }

  @override
  Widget build(BuildContext context) {
    if (!_isShowAd) {
      return Container();
    }
    if (defaultTargetPlatform == TargetPlatform.android) {
      return Container(
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
        child: AndroidView(
          viewType: _viewType,
          creationParams: {
            "androidId": widget.androidId,
            "appSid": widget.appSid,
            "displayDownloadInfo": widget.displayDownloadInfo,
            "limitClick": widget.limitClick,
            "displayClick": widget.displayClick,
            "popDialogDownLoad": widget.popDialogDownLoad,
            "fetchDelay":widget.fetchDelay
          },
          onPlatformViewCreated: _registerChannel,
          creationParamsCodec: const StandardMessageCodec(),
        ),
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return Container(
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
        child: UiKitView(
          viewType: _viewType,
          creationParams: {
            "iosId": widget.iosId,
            "fetchDelay": widget.fetchDelay,
          },
          onPlatformViewCreated: _registerChannel,
          creationParamsCodec: const StandardMessageCodec(),
        ),
      );
    } else {
      return Container();
    }
  }

  //注册cannel
  void _registerChannel(int id) {
    _channel = MethodChannel("${_viewType}_$id");
    _channel?.setMethodCallHandler(_platformCallHandler);
  }

  //监听原生view传值
  Future<dynamic> _platformCallHandler(MethodCall call) async {
    switch (call.method) {
    //显示广告
      case FlutterBaiduAdMethod.onShow:
        widget.callBack?.onShow!();
        break;
    //关闭
      case FlutterBaiduAdMethod.onClose:
        widget.callBack?.onClose!();
        break;
    //广告加载失败
      case FlutterBaiduAdMethod.onFail:
        if (mounted) {
          setState(() {
            _isShowAd = false;
          });
        }
        Map map = call.arguments;
        widget.callBack?.onFail!(map["code"], map["message"]);
        break;
    //点击
      case FlutterBaiduAdMethod.onClick:
        widget.callBack?.onClick!();
        break;
    }
  }
}
