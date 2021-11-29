//
//  LogUtil.h
//  Pods
//
//  Created by 郭维佳 on 2021/11/29.
//
#ifdef DEBUG
#define GLog(...) NSLog(@"%s\n %@\n\n", __func__, [NSString stringWithFormat:__VA_ARGS__])
#else
#define GLog(...)
#endif
