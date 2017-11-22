package com.load.third.jqm.bean;

// FIXME generate failure  field _$List263

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class HomeExpenseDataBean {
    @SerializedName("list")
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * cfgRateId : 1
         * amount : 500
         * period : 7
         * interestFee : 2.1
         * serviceFee : 39.1
         * accountManageFee : 10.1
         * repay : 551.1
         */
        @SerializedName("cfgRateId")
        private int cfgRateId;
        @SerializedName("amount")
        private String amount;
        @SerializedName("period")
        private String period;
        @SerializedName("interestFee")
        private double interestFee;
        @SerializedName("serviceFee")
        private double serviceFee;
        @SerializedName("accountManageFee")
        private double accountManageFee;
        @SerializedName("repay")
        private double repay;

        public int getCfgRateId() {
            return cfgRateId;
        }

        public void setCfgRateId(int cfgRateId) {
            this.cfgRateId = cfgRateId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public double getInterestFee() {
            return interestFee;
        }

        public void setInterestFee(double interestFee) {
            this.interestFee = interestFee;
        }

        public double getServiceFee() {
            return serviceFee;
        }

        public void setServiceFee(double serviceFee) {
            this.serviceFee = serviceFee;
        }

        public double getAccountManageFee() {
            return accountManageFee;
        }

        public void setAccountManageFee(double accountManageFee) {
            this.accountManageFee = accountManageFee;
        }

        public double getRepay() {
            return repay;
        }

        public void setRepay(double repay) {
            this.repay = repay;
        }
    }
}
