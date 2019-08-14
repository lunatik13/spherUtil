package com.unme.sphereutil;

import java.util.Optional;

public class StandVm {
    private String name;
    private String dataStore;
    private String dcName;
    private String folderName;
    private String hostName;
    private String operation;
    private boolean powerOn;
    private String resourcePool;
    private String snapName;
    private String vmName;
    private String cloneName;
    private String ip;
    private Optional<String> dns;
    private Optional<String> gateway;
    private Optional<String> subnet;

    public Optional<String> getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = Optional.of(dns);
    }

    public Optional<String> getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = Optional.of(gateway);
    }

    public Optional<String> getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = Optional.of(subnet);
    }

    public String getIp() { return ip; }

    public void setIp(String ip) { this.ip = ip; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataStore() {
        return dataStore;
    }

    public void setDataStore(String dataStore) {
        this.dataStore = dataStore;
    }

    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }

    public String getResourcePool() {
        return resourcePool;
    }

    public void setResourcePool(String resourcePool) {
        this.resourcePool = resourcePool;
    }

    public String getSnapName() {
        return snapName;
    }

    public void setSnapName(String snapName) {
        this.snapName = snapName;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getCloneName() {
        return cloneName;
    }

    public void setCloneName(String cloneName) {
        this.cloneName = cloneName;
    }
}
