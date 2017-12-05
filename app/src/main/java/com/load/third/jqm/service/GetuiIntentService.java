package com.load.third.jqm.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.load.third.jqm.MyApp;
import com.load.third.jqm.newHttp.ApiManager;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.CustomConsumer;
import com.load.third.jqm.newHttp.UrlParams;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class GetuiIntentService extends GTIntentService {

    public static String payloadData = "";

    public GetuiIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        byte[] payload = msg.getPayload();
        if (payload != null) {
            payloadData = new String(payload);
            Log.d(TAG, "onReceiveMessageData payloadData->" + payloadData);
            //收到个推透传，发送广播
            context.sendBroadcast(new Intent("ACTION_GET_GETUI_MSG"));
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        if (!MyApp.isNeedUpdate) {
            //收到个推clientid，向后台发送绑定
//            GetuiGetUtils.bindGetuiCid(context, clientid);
            Map<String, Object> params = new HashMap<>();
            params.put("cid", clientid);
            ApiManager.apiManager.initRetrofit().getBindGetuiCid(UrlParams.getUrl(Apis.bindGetuiCid.getUrl(), params))
                    .doOnSubscribe(new CustomConsumer<Disposable>(this))
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<String>() {
                        @Override
                        public void doSuccess(BaseResponse<String> result) {

                        }
                    });
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.d(TAG, "onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.d(TAG, "onReceiveCommandResult -> " + cmdMessage);
    }
}
