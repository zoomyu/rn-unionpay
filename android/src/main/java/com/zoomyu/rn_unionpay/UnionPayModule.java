package com.zoomyu.rn_unionpay;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.unionpay.UPPayAssistEx;

/**
 * Created by yuwei on 17-8-31.
 */

public class UnionPayModule extends ReactContextBaseJavaModule {

    private static final int UNION_PAY_REQUEST = 0x0a;
    private static final String R_ACTIVITY_DOES_NOT_EXIST = "R_ACTIVITY_DOES_NOT_EXIST";
    private static final String R_PLUGIN_NOT_FOUND  = "R_PLUGIN_NOT_FOUND";
    private static final String R_PAY_CANCELLED  = "R_PAY_CANCELLED";
    private static final String R_PAY_NO_DATA  = "R_PAY_NO_DATA";

    private Promise mUnionPayPromise;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (requestCode == UNION_PAY_REQUEST ) {
                if (mUnionPayPromise != null) {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        mUnionPayPromise.reject(R_PAY_CANCELLED, "UnionPay was cancelled");
                    } else if (resultCode == Activity.RESULT_OK) {
                       if (data == null) {
                           mUnionPayPromise.reject(R_PAY_NO_DATA, "No result");
                       }
                       String pay_result = data.getExtras().getString("pay_result");
                       String secret = data.getExtras().getString("secret");
                       String result_data = data.getExtras().getString("result_data");
                       String qn = data.getExtras().getString("qn");
                       String sid = data.getExtras().getString("sid");

                       WritableMap params = Arguments.createMap();
                       params.putString("pay_result", pay_result);
                       params.putString("secret", secret);
                       params.putString("result_data", result_data);
                       params.putString("qn", qn);
                       params.putString("sid", sid);
                       mUnionPayPromise.resolve(params);
                    }
                    mUnionPayPromise = null;
                }
            }
        }
    };
    public UnionPayModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "UnionPay";
    }

    @ReactMethod
    public void pay(String tn, String mode, Promise promise) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(R_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }
        mUnionPayPromise = promise;
        // mMode参数解释： 0 - 启动银联正式环境 1 - 连接银联测试环境
        int rs = UPPayAssistEx.startPay(this.getCurrentActivity(), null, null, tn, mode);
        if(UPPayAssistEx.PLUGIN_NOT_FOUND == rs) {
            promise.reject(R_PLUGIN_NOT_FOUND, "Plugin doesn't found");
            return;
        } else {
            Log.d("UnionPayModule", "pay: " + rs);
        }
    }
}
