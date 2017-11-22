package com.load.third.jqm.bean;

/**
 * Created by Administrator on 2017/4/14.
 */

public class RepaymentDataBean {
    /**
     * due_time : 2017-04-12
     * repay_amount : 1000
     * surplus_time : 6
     */

    private String due_time;
    private int repay_amount;
    private int surplus_time;

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public int getRepay_amount() {
        return repay_amount;
    }

    public void setRepay_amount(int repay_amount) {
        this.repay_amount = repay_amount;
    }

    public int getSurplus_time() {
        return surplus_time;
    }

    public void setSurplus_time(int surplus_time) {
        this.surplus_time = surplus_time;
    }
}
