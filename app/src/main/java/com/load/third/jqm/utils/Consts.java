package com.load.third.jqm.utils;

/**
 * Created by Administrator on 2017/4/12.
 */

public class Consts {
    //        0	 	可以正常借贷
//        20	int	可以正常借贷，且是第N次再借贷
//        1	  	资料尚未提交
//        2	 	资料已提交,待审核
//        3	 	已审核，待绑卡
//        4	 	已绑卡，待拍照
//        5	 	已拍照，待验证
//        7	 	已放款
//        8	 	放款失败
//        10	int	重新上传证件照
//        13	int	等待放款
//        14	int	等待放款
    public static final int STATUS_BORROW_FIRST = 0;
    public static final int STATUS_BORROW_AGAIN = 20;
    public static final int STATUS_PSOT_INFO= 1;
    public static final int STATUS_CHECKING_INFO= 2;
    public static final int STATUS_POST_BANK_CARD= 3;
    public static final int STATUS_POAT_ID_CARD = 4;
    public static final int STATUS_CHECKING_ID_CARD = 5;
    public static final int STATUS_PAY_SUCCESS= 7;
    public static final int STATUS_PAY_ERROR = 8;
    public static final int STATUS_REPOST_ID_CARD= 10;
    public static final int STATUS_WAIT_PAY_13= 13;
    public static final int STATUS_WAIT_PAY_14= 14;

    //借贷后客服QQ
    public static final String QQ_after_borrow="2036238164";
    //借贷前客服QQ
    public static final String QQ_before_borrow="2036238164";
}
