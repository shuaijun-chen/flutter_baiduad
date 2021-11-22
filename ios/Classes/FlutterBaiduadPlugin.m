#import "FlutterBaiduadPlugin.h"
#if __has_include(<flutter_baiduad/flutter_baiduad-Swift.h>)
#import <flutter_baiduad/flutter_baiduad-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_baiduad-Swift.h"
#endif

@implementation FlutterBaiduadPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterBaiduadPlugin registerWithRegistrar:registrar];
}
@end
