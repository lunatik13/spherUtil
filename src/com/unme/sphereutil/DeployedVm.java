package com.unme.sphereutil;

import java.util.TreeSet;

public class DeployedVm {
    private  String name;
    private  String type;
    private  String primaryIp;
    private  String osFamily;
    private  String dcName;

    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    public String getPrimaryIp() {
        return primaryIp;
    }

    public void setPrimaryIp(String primaryIp) {
        this.primaryIp = primaryIp;
    }

    public TreeSet<String> getIps() {
        return Ips;
    }

    public void setIps(TreeSet<String> ips) {
        Ips = ips;
    }

    private  TreeSet<String> Ips;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
