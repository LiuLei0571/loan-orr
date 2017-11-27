package com.load.third.jqm.newHttp;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/20.
 * 邮箱：liulei2@aixuedai.com
 */


public interface Apis {
    ApiRequest home = ApiRequest.get("loan/CfgList");
    ApiRequest smsCode = ApiRequest.get("loan/smsSend");
    ApiRequest login = ApiRequest.get("loan/MobileRegister");
    ApiRequest loginWithToken = ApiRequest.get("loan/logged/LoginInfo").setHasToken(true);
    ApiRequest getStatus = ApiRequest.get("loan/logged/CurrentProgress").setHasToken(true);
    ApiRequest getRepaymentData = ApiRequest.get("loan/logged/selectRepay").setHasToken(true);
    ApiRequest getRepaymentUser = ApiRequest.get("loan/logged/selectUserName").setHasToken(true);
    ApiRequest checkPhone = ApiRequest.get("loan/logged/GetMessage").setHasToken(true);
    ApiRequest postBorrowInfo = ApiRequest.get("loan/logged/borrowInInsert").setHasToken(true);
    ApiRequest myInfoFirst = ApiRequest.get("loan/logged/updateGR").setHasToken(true);
    ApiRequest myInfoSecond = ApiRequest.get("loan/logged/updateZY").setHasToken(true);
    ApiRequest myInfoThird = ApiRequest.get("loan/logged/updateSH").setHasToken(true);
    ApiRequest getBankName = ApiRequest.get("loan/logged/selectBankName").setHasToken(true);
    ApiRequest bindBankCard = ApiRequest.get("loan/logged/saveBank").setHasToken(true);
    ApiRequest getQiNiuName = ApiRequest.get("loan/QiniuReName").setHasToken(false);
    ApiRequest getQiNiuToken = ApiRequest.get("loan/QiniuToken").setHasToken(false);
    ApiRequest bindIdCard = ApiRequest.get("loan/logged/newSavePic").setHasToken(true);
    ApiRequest postContacts = ApiRequest.get("loan/logged/getAddressList").setHasToken(true);
    ApiRequest bindGetuiCid = ApiRequest.get("loan/logged/updateCid").setHasToken(true);
    ApiRequest isStudentAuth = ApiRequest.get("loan/logged/selectStudentAuth").setHasToken(true);
    ApiRequest studyAuth = ApiRequest.get("loanEducation/logged/StudyAuth").setHasToken(true);
    ApiRequest postPosition = ApiRequest.get("loan/logged/updatePosition").setHasToken(true);
    ApiRequest isChecking = ApiRequest.get("app/downloadApp/version.json").setHasToken(true);
}
