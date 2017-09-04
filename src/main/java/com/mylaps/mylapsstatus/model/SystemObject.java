package com.mylaps.mylapsstatus.model;

public class SystemObject {
    private int systemId;
    private String systemName;
    private String systemStatus;

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(final int systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(final String systemName) {
        this.systemName = systemName;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(final String systemStatus) {
        this.systemStatus = systemStatus;
    }
}
