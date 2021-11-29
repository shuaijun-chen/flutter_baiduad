import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_baiduad/flutter_baiduad.dart';

/// @Author: gstory
/// @CreateDate: 2021/11/22 6:11 下午
/// @Email gstory0404@gmail.com
/// @Description: dart类作用描述

class BannerAdView extends StatefulWidget {
  final String androidId;
  final String iosId;
  final String? appSid;
  final bool? autoplay;
  final double viewWidth;
  final double viewHeight;
  final FlutterBaiduAdBannerCallBack? callBack;

  const BannerAdView(
      {Key? key,
      required this.androidId,
      required this.iosId,
      this.appSid,
      this.autoplay,
      required this.viewWidth,
      required this.viewHeight,
      this.callBack})
      : super(key: key);

  @override
  _BannerAdViewState createState() => _BannerAdViewState();
}

class _BannerAdViewState extends State<BannerAdView> {

  String _viewType = "com.gstory.flutter_baiduad/BannerAdView";

  MethodChannel? _channel;

  //广告是否显示
  bool _isShowAd = true;

  @override
  void initState() {
    super.initState();
    _isShowAd = true;
    setState(() {

    });
  }

  @override
  Widget build(BuildContext context) {
    if (!_isShowAd) {
      return Container();
    }
    if (defaultTargetPlatform == TargetPlatform.android) {
      return SizedBox(
        width: widget.viewWidth,
        height: widget.viewHeight,
        child: AndroidView(
          viewType: _viewType,
          creationParams: {
            "androidId": widget.androidId,
            "appSid": widget.appSid,
            "autoplay": widget.autoplay,
            "viewWidth": widget.viewWidth,
            "viewHeight": widget.viewHeight,
          },
          onPlatformViewCreated: _registerChannel,
          creationParamsCodec: const StandardMessageCodec(),
        ),
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return SizedBox(
        width: widget.viewWidth,
        height: widget.viewHeight,
        child: UiKitView(
          viewType: _viewType,
          creationParams: {
            "iosId": widget.iosId,
            "appSid": widget.appSid,
            "viewWidth": widget.viewWidth,
            "viewHeight": widget.viewHeight,
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
    //关闭
      case FlutterBaiduAdMethod.onClose:
        if (mounted) {
          setState(() {
            _isShowAd = false;
          });
        }
        widget.callBack?.onClose!();
        break;
    }
  }
}
