package com.unme.sphereutil;

import com.vmware.vim25.mo.ServiceInstance;

public class SphereStandConnection {
    private final ServiceInstance si;
    private final String dcName;
    private final String vmName;
    private final String operation;
    private final String cloneName;
    private final String snapName;
    private final String dataStore;

    public ServiceInstance getSi() {
        return si;
    }
    public String getDcName() {
        return dcName;
    }
    public String getVmName() {
        return vmName;
    }
    public String getOperation() {
        return operation;
    }
    public String getCloneName() {
        return cloneName;
    }
    public String getSnapName() {
        return snapName;
    }
    public String getDataStore() {
        return dataStore;
    }
    public String getResourcePool() {
        return resourcePool;
    }
    public String getHostName() {
        return hostName;
    }
    public String getFolderName() {
        return folderName;
    }
    public Boolean getPowerOn() {
        return powerOn;
    }
    public String getIp() { return ip; }
    public String getDns() { return dns; }
    public String getGateway() { return gateway; }
    public String getSubnet() { return subnet; }

    private final String resourcePool;
    private final String hostName;
    private final String folderName;
    private final Boolean powerOn;
    private final String ip;
    private final String dns;
    private final String gateway;
    private final String subnet;

    public static class Builder {

        // Required params
        private final ServiceInstance si;
        private final String dcName;
        private final String vmName;
        private final String operation;

        // Optional params
        private String cloneName = null;
        private String snapName = null;
        private String dataStore = null;
        private String resourcePool = null;
        private String hostName = null;
        private String folderName = null;
        private Boolean powerOn = false;
        private String ip = null;
        private String dns = null;
        private String gateway = null;
        private String subnet = null;

        public Builder(ServiceInstance si, String dcName, String vmName, String operation) {
            this.si = si;
            this.dcName = dcName;
            this.vmName = vmName;
            this.operation = operation;
        }

        public Builder cloneName(String val) {
            cloneName = val;
            return this;
        }

        public Builder snapName(String val) {
            snapName = val;
            return this;
        }

        public Builder dataStore(String val) {
            dataStore = val;
            return this;
        }

        public Builder resourcePool(String val) {
            resourcePool = val;
            return this;
        }

        public Builder hostName(String val) {
            hostName = val;
            return this;
        }

        public Builder folderName(String val) {
            folderName = val;
            return this;
        }

        public Builder powerOn(boolean val) {
            powerOn = val;
            return this;
        }

        public Builder ip(String val) {
            ip = val;
            return this;
        }

        public Builder dns(String val) {
            dns = val;
            return this;
        }

        public Builder gateway(String val) {
            gateway = val;
            return this;
        }

        public Builder subnet(String val) {
            subnet = val;
            return this;
        }

        public SphereStandConnection build() {
            return new SphereStandConnection(this);
        }

    }
    private SphereStandConnection(Builder builder) {
        si = builder.si;
        dcName = builder.dcName;
        vmName = builder.vmName;
        operation = builder.operation;
        cloneName = builder.cloneName;
        snapName = builder.snapName;
        dataStore = builder.dataStore;
        resourcePool = builder.resourcePool;
        hostName = builder.hostName;
        folderName = builder.folderName;
        powerOn = builder.powerOn;
        ip = builder.ip;
        dns = builder.dns;
        gateway = builder.gateway;
        subnet = builder.subnet;
    }
}
