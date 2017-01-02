package com.test;

import java.io.Serializable;

public class ChatmeterAccountLocations implements Serializable, Comparable<ChatmeterAccountLocations> {
    private String accountName;
    private String accountId;
    private Integer billableLocationsCount = 0;
    private Integer auditLocationsCount = 0;
    private Integer billableLocationsDiff = 0;
    private Integer auditLocationsDiff = 0;

    public ChatmeterAccountLocations(String accountName, String accountId) {
        this.accountName = accountName;
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountId() {
        return accountId;
    }

    public Integer getBillableLocationsCount() {
        return billableLocationsCount;
    }

    public void setBillableLocationsCount(Integer billableLocationsCount) {
        this.billableLocationsCount = billableLocationsCount;
    }

    public Integer getAuditLocationsCount() {
        return auditLocationsCount;
    }

    public void setAuditLocationsCount(Integer auditLocationsCount) {
        this.auditLocationsCount = auditLocationsCount;
    }

    public Integer getBillableLocationsDiff() {
        return billableLocationsDiff;
    }

    public void setBillableLocationsDiff(Integer billableLocationsDiff) {
        this.billableLocationsDiff = billableLocationsDiff;
    }

    public Integer getAuditLocationsDiff() {
        return auditLocationsDiff;
    }

    public void setAuditLocationsDiff(Integer auditLocationsDiff) {
        this.auditLocationsDiff = auditLocationsDiff;
    }

    public int compareTo(ChatmeterAccountLocations o) {
        int result = 0;
        result = this.billableLocationsCount - o.billableLocationsCount;
        if(result == 0) {
            result = this.auditLocationsCount - o.auditLocationsCount;
        }
        if(result == 0) {
            result = this.accountName.compareTo(o.accountName);
        }

        return result;
    }
}
