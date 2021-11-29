//
//  BannerView.m
//  Pods
//
//  Created by 郭维佳 on 2021/11/27.
//

#import <Foundation/Foundation.h>
#import "BannerView.h"
#import "BaiduMobAdSDK/BaiduMobAdView.h"
#import "StringUtls.h"
#import "BaiduAdManager.h"
#import "LogUtil.h"

@implementation BannerViewFactory{
    NSObject<FlutterBinaryMessenger>*_messenger;
}



- (instancetype)initWithMessenger:(NSObject<FlutterBinaryMessenger> *)messager{
    self = [super init];
    if (self) {
        _messenger = messager;
    }
    return self;
}

-(NSObject<FlutterMessageCodec> *)createArgsCodec{
    return [FlutterStandardMessageCodec sharedInstance];
}

-(NSObject<FlutterPlatformView> *)createWithFrame:(CGRect)frame viewIdentifier:(int64_t)viewId arguments:(id)args{
    BannerView * bannerView = [[BannerView alloc] initWithWithFrame:frame viewIdentifier:viewId arguments:args binaryMessenger:_messenger];
    
    return bannerView;
    
}

@end


@interface BannerView()<BaiduMobAdViewDelegate>
@property(nonatomic,strong) BaiduMobAdView *bdBannerView;
@property(nonatomic,strong) UIView *container;
@property(nonatomic,assign) NSInteger viewId;
@property(nonatomic,strong) FlutterMethodChannel *channel;
@property(nonatomic,strong) NSString *appSid;
@property(nonatomic,strong) NSString *codeId;
@property(nonatomic,strong) NSNumber *width;
@property(nonatomic,strong) NSNumber *height;
@end

@implementation BannerView

- (instancetype)initWithWithFrame:(CGRect)frame viewIdentifier:(int64_t)viewId arguments:(id)args binaryMessenger:(NSObject<FlutterBinaryMessenger> *)messenger{
    if ([super init]) {
        self.container= [[UIView alloc] initWithFrame:CGRectMake(0, 0, 300, 50)];
        _viewId = viewId;
        _appSid = args[@"appSid"];
        _codeId = args[@"iosId"];
        _width =args[@"viewWidth"];
        _height =args[@"viewWidth"];
        [self loadBannerAd];
    }
    return self;
}

//加载广告
-(void)loadBannerAd{
    //load横幅
    [_bdBannerView removeFromSuperview];
    _bdBannerView = [[BaiduMobAdView alloc] init];
    _bdBannerView.AdUnitTag = _codeId;
    _bdBannerView.AdType = BaiduMobAdViewTypeBanner;
    _bdBannerView.delegate = self;
    _bdBannerView.frame = CGRectMake(0, 0, 300, 50);
    [_bdBannerView start];
}

# pragma mark -- delegate
-(NSString *)publisherId{
    if([StringUtls isStringEmpty:self.appSid]){
        return [BaiduAdManager sharedInstance].getAppId;
    }
    return self.appSid;
}

- (NSString *)channelId{
    return @"";
}
/**
 *  启动位置信息
 */
- (BOOL)enableLocation
{
    return NO;
}

/**
 *  广告将要被载入
 */
- (void)willDisplayAd:(BaiduMobAdView *)adview{
    GLog(@"横幅广告: 加载成功");
    [self.container removeFromSuperview];
    [self.container addSubview:adview];
}

/**
 *  广告载入失败
 */
- (void)failedDisplayAd:(BaiduMobFailReason)reason{
    [self.bdBannerView removeFromSuperview];
    GLog(@"横幅广告: 加载失败 %d", reason);
}


/**
 *  本次广告展示成功时的回调
 */
- (void)didAdImpressed{
    GLog(@"横幅广告: 展示了");
}

/**
 *  本次广告展示被用户点击时的回调
 */
- (void)didAdClicked{
    GLog(@"横幅广告: 点击了");
}

/**
 *  在用户点击完广告条出现全屏广告页面以后，用户关闭广告时的回调
 */
- (void)didDismissLandingPage{
    GLog(@"横幅广告: 关闭落地页");
}

/**
 *  用户点击关闭按钮关闭广告后的回调
 */
- (void)didAdClose{
    GLog(@"横幅广告: 关闭");
}



-(UIView *)view{
    return self.container;
    
}

@end
