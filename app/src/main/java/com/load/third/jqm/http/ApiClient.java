package com.load.third.jqm.http;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.load.third.jqm.bean.HomeExpenseDataBean;
import com.load.third.jqm.bean.RepaymentDataBean;
import com.load.third.jqm.bean.RepaymentUserBean;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.http.result.DataJsonResult;
import com.load.third.jqm.utils.Urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.load.third.jqm.utils.Urls.BASE_URL;

/**
 * 调用API
 */
public class ApiClient {
    private static ApiClient mInstance;
    public String url = BASE_URL + "loan/";

    private ApiClient() {
        super();
    }

    private OkHttpClientManager okHttpClientManager = OkHttpClientManager.getInstance();

    public synchronized static ApiClient getInstance() {
        if (mInstance == null) {
            synchronized (ApiClient.class) {
                mInstance = new ApiClient();

            }
        }
        return mInstance;
    }

    @Deprecated
    public void getSMScode(String mobile, OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        String postText = url + "smsSend" + "?mobile=" + mobile;
        okHttpClientManager.getAsyn(postText, callback);
    }

    @Deprecated
    public void login(String mobile, String code, String mobile_type, String version, OkHttpClientManager.ResultCallback<DataJsonResult<UserBean>> callback) {
        String versionkey = version;
        try {
            versionkey = URLEncoder.encode(version, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("http_msg", "versionkey:" + versionkey);
        String postText = url + "MobileRegister" + "?mobile=" + mobile + "&code=" + code
                + "&mobile_type=" + mobile_type + "&version=" + versionkey;
        okHttpClientManager.getAsyn(postText, callback);
    }

    public void loginWithToken(String token, OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>> callback) {
        String postText = url + "logged/LoginInfo" + "?token=" + token;
        okHttpClientManager.getAsyn(postText, callback);
    }

    @Deprecated
    public void getStatus(String token, OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>> callback) {
        String postText = url + "logged/CurrentProgress" + "?token=" + token;
        okHttpClientManager.getAsyn(postText, callback);
    }

    @Deprecated
    public void getHomeExpenseData(OkHttpClientManager.ResultCallback<DataJsonResult<HomeExpenseDataBean>> callback) {
        String postText = url + "CfgList";
        okHttpClientManager.getAsyn(postText, callback);
    }

    @Deprecated
    public void getRepaymentData(String token, OkHttpClientManager.ResultCallback<DataJsonResult<RepaymentDataBean>> callback) {
        String postText = url + "logged/selectRepay" + "?token=" + token;
        okHttpClientManager.getAsyn(postText, callback);
    }

    public void getRepaymentUser(String token, OkHttpClientManager.ResultCallback<DataJsonResult<RepaymentUserBean>> callback) {
        String postText = url + "logged/selectUserName" + "?token=" + token;
        okHttpClientManager.getAsyn(postText, callback);
    }

    @Deprecated
    public void checkPhone(String token, OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>> callback) {
        String postText = url + Urls.url_checkPhone + "?token=" + token;
        okHttpClientManager.getAsyn(postText, callback);
    }

    @Deprecated
    public void postBorrowInfo(String token, String borrowPeriod, String borrowMoney, OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>> callback) {
        String postText = url + "logged/borrowInInsert" + "?token=" + token + "&borrowPeriod=" + borrowPeriod + "&borrowMoney=" + borrowMoney;
        okHttpClientManager.getAsyn(postText, callback);
    }

    public void myInfoFirst(String token, String realName, String idcard, String qq, String email, int education,
                            int marriage, int child, String temporaryAdddress, int temporaryTime,
                            OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        String postText = url + "logged/updateGR" + "?token=" + token + "&realName=" + realName + "&idcard=" + idcard
                + "&qq=" + qq + "&email=" + email + "&education=" + education + "&marriage=" + marriage
                + "&child=" + child + "&temporaryAdddress=" + temporaryAdddress + "&temporaryTime=" + temporaryTime;
        okHttpClientManager.getAsyn(postText, callback);
    }

    public void myInfoSecond(String token, int positionId, int incomeId, int payDate, String companyName, String companyProvince,
                             String companyCity, String companyAddress, String companyCode, String companyTel,
                             OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        String postText = url + "logged/updateZY" + "?token=" + token + "&positionId=" + positionId + "&incomeId=" + incomeId
                + "&payDate=" + payDate + "&companyName=" + companyName + "&companyProvince=" + companyProvince
                + "&companyCity=" + companyCity + "&companyAddress=" + companyAddress + "&companyCode="
                + companyCode + "&companyTel=" + companyTel;
        okHttpClientManager.getAsyn(postText, callback);
    }

    public void myInfoThird(String token, int cognateRelation, String cognateMobile, int socialRelation, String socialMobile,
                            OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        String postText = url + "logged/updateSH" + "?token=" + token + "&cognateRelation="
                + cognateRelation + "&cognateMobile=" + cognateMobile
                + "&socialRelation=" + socialRelation + "&socialMobile=" + socialMobile;
        okHttpClientManager.getAsyn(postText, callback);
    }

    public void getBankName(String bankcard, OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>> callback) {
        String postText = url + "logged/selectBankName" + "?bankcard=" + bankcard;
        okHttpClientManager.getAsyn(postText, callback);
    }
    @Deprecated
    public void bindBankCard(String token, String bankcard, String bankname, String bankAccount,
                             OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        String postText = url + "logged/saveBank" + "?token=" + token + "&bankcard=" + bankcard
                + "&bankname=" + bankname + "&bankAccount=" + bankAccount;
        okHttpClientManager.getAsyn(postText, callback);
    }

    public void getQiNiuName(OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>> callback) {
        okHttpClientManager.getAsyn(url + Urls.url_getQiNiuName, callback);
    }

    public void getQiNiuToken(OkHttpClientManager.ResultCallback<JSONObject> callback) {
        okHttpClientManager.getAsyn(url + Urls.url_getQiNiuToken, callback);
    }

    public void bindIdCard(String token, String url0, String url1, String url2, String url3, String url4,
                           OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        String postText = url + "logged/newSavePic" + "?token=" + token + "&url0=" + url0 + "&url1=" + url1
                + "&url2=" + url2 + "&url3=" + url3 + "&url4=" + url4;
        okHttpClientManager.getAsyn(postText, callback);
    }

    @Deprecated
    public void postContacts(String token, String addressList,
                             OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        String postText = url + "logged/getAddressList" + "?token=" + token + "&addressList=" + addressList;
        okHttpClientManager.getAsyn(postText, callback);
    }

    @Deprecated
    public void bindGetuiCid(String token, String clientid,
                             OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        String postText = url + "logged/updateCid" + "?token=" + token + "&cid=" + clientid;
        okHttpClientManager.getAsyn(postText, callback);
    }

    @Deprecated
    public void isStudentAuth(String token, OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        okHttpClientManager.getAsyn(url + "logged/selectStudentAuth" + "?token=" + token, callback);
    }
    @Deprecated
    public void studyAuth(String token, String username, String password, String captcha, OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>> callback) {
        okHttpClientManager.getAsyn(Urls.url_xuexin_verify + "?token=" + token + "&username=" + username
                + "&password=" + password + "&captcha=" + captcha, callback);
    }

    @Deprecated
    public void postPosition(String token, String latitude, String longitude, OkHttpClientManager.ResultCallback<DataJsonResult<String>> callback) {
        okHttpClientManager.getAsyn(BASE_URL + "loan/logged/updatePosition"
                + "?token=" + token + "&latitude=" + latitude + "&longitude=" + longitude, callback);
    }

    //是否上线审核中
    @Deprecated
    public void isChecking(OkHttpClientManager.ResultCallback<String> callback) {
        String postText = BASE_URL + "app/downloadApp/version.json";
        okHttpClientManager.getAsyn(postText, callback);
    }
}
