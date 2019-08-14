# sphereApiUtil
Manage Vmware Shpere VMs util
- Fill config
- Deploy

### How to build
```
mvn clean package
```
### Fill start.yaml config
```
---
name: "Start Stend Config"
vms:
  -
    name: Debian
    dataStore: "ds2"
    dcName: "Dev DataCenter"
    folderName: "project"
    hostName: "Debian9"
    operation: "clone"
    powerOn: true
    resourcePool: "pool"
    snapName: "clear"
    vmName: "Debian9"
    cloneName: "Debian9"
    ip: "192.168.1.2"
```

### How to start
```
java -jar sphereApi-1.0-SNAPSHOT-shaded.jar sphereUser spherePassword start.yaml
```
