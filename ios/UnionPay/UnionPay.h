//
//  UnionPay.h
//  UnionPay
//
//  Created by xinjing li on 2017/8/11.
//  Copyright © 2017年 xinjing li. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface UnionPay : NSObject <RCTBridgeModule>
@property NSString* schemeStr;
@property RCTPromiseRejectBlock myReject; // reject function
@property RCTPromiseResolveBlock myResolve;//resolve function

@end
