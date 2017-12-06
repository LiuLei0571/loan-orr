package com.load.third.jqm.httpUtil;

import android.content.Context;
import android.util.Log;

import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.http.ApiClient;
import com.load.third.jqm.http.OkHttpClientManager;
import com.load.third.jqm.http.result.DataJsonResult;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.StringUtils;
import com.squareup.okhttp.Request;

/**
 * Created by Administrator on 2017/4/20.
 */
@Deprecated
public class GetuiGetUtils {
    @Deprecated
    public static void bindGetuiCid(final Context context, String clientid) {
        String token = UserDao.getInstance(context).getToken( );
        if (StringUtils.isNotBlank(token)) {
            ApiClient.getInstance( ).bindGetuiCid(token, clientid, new OkHttpClientManager.ResultCallback<DataJsonResult<String>>( ) {

                @Override
                public void onError(Request request, Exception e, String error) {
                    ToastUtils.showToast(context, "网络请求失败");
                    Log.e("http_msg", "个推ID绑定失败");
                }

                @Override
                public void onResponse(DataJsonResult<String> response) {
                    if (response.getSuccess( ) == "true") {
                        Log.e("http_msg", "个推ID绑定成功");
                    } else {
                        ToastUtils.showToast(context, response.getMessage( ));
                        Log.e("http_msg", "个推ID绑定 " + response.getMessage( ));
                    }
                }
            });
        }
    }
}
