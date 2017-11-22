package com.supcoder.fundcrawler.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * HistoryEntity
 *
 * @author lee
 * @date 2017/11/14
 */

public class MoneyEntity extends RealmObject {

    @PrimaryKey
    private String fundId;

    private String money;


    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
