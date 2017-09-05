   //
//  UnionPay.m
//  UnionPay
//
//  Created by xinjing li on 2017/8/11.
//  Copyright © 2017年 xinjing li. All rights reserved.
//

#import "UnionPay.h"
#import "UPPaymentControl.h"
#import <UIKit/UIKit.h>



@implementation UnionPay

@synthesize myResolve = _myResolve;
@synthesize myReject = _myReject;

RCT_EXPORT_MODULE();
- (void)dealloc{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

RCT_EXPORT_METHOD(pay:(NSString *)tn
                  mode:(NSString*)mode
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    
    
    //将promise代理到成员变量
    _myResolve = resolve;
    _myReject = reject;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(UnionPayResult:) name:@"UnionPayResult"object:nil];
    
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    UIViewController *rootViewController = window.rootViewController;
    self.schemeStr = @"UPPayDemo";
    
    [[UPPaymentControl defaultControl] startPay:tn fromScheme:self.schemeStr mode:mode viewController:rootViewController];
 
}
- (void)UnionPayResult:(NSNotification *)PayResult{
    _myResolve(PayResult.userInfo);
    
//            if([PayResult.userInfo isEqualToString:@"success"]) {
//              //如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
//              if(data != nil){
//                NSNotification *notification =[NSNotification notificationWithName:@"UnionPayResult" object:nil userInfo:data];
//               [[NSNotificationCenter defaultCenter] postNotification:notification];
//              }
//    
//              //结果code为成功时，去商户后台查询一下确保交易是成功的再展示成功
//            }
//            else if([PayResult.userInfo isEqualToString:@"fail"]) {
//              //交易失败
//            }
//            else if([PayResult.userInfo isEqualToString:@"cancel"]) {
//              //交易取消
//            }
}

@end
