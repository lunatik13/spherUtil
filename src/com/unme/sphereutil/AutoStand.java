package com.unme.sphereutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.unme.sphereutil.*;
import com.vmware.vim25.mo.ServiceInstance;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AutoStand {
    String serverAddress     = "server.com";
    String username          = "user";
    @Test(description = "Let`s start VMs")

    public DeployedVms run(String username, String password, String yamlName) throws MalformedURLException, RemoteException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        DeployedVms deployedVms = new DeployedVms();
        List<DeployedVm> deployedVmList = new ArrayList<>();

        try {
            // Read yaml config
            StandVms vms = mapper.readValue(new File(yamlName), StandVms.class);
            int vmNumber = vms.getVms().size();
            for (int i = 0; i < vmNumber; i++ ) {
                StandVm vm = vms.getVms().get(i);
                if (vms.getVms().get(i).getOperation().equals("clone")) {
                    OpsVm opsVm = new OpsVm();
                    ServiceInstance si = new ServiceInstance(new URL("https://"+ serverAddress +"/sdk"),
                            username, password, true);
                    SphereStandConnection connClone = new SphereStandConnection.Builder(si,
                            vm.getDcName(),
                            vm.getVmName(),
                            "clone" ).
                            cloneName(vm.getCloneName()).
                            snapName(vm.getSnapName()).
                            dataStore(vm.getDataStore()).
                            resourcePool(vm.getResourcePool()).
                            hostName(vm.getVmName()).
                            folderName(vm.getFolderName()).
                            powerOn(vm.isPowerOn()).
                            ip(vm.getIp()).
                            build();
                    boolean cloningStatus = opsVm.cloneAndStartVM(connClone);

                }
                TimeUnit.SECONDS.sleep(5);
            }

            // Ждем, пока не развернется стенд
            TimeUnit.SECONDS.sleep(90);

            // Get deployed VMs IPs from sphere
            for (int i = 0; i < vmNumber; i++ ) {
                StandVm standVm = vms.getVms().get(i);
                OpsVm opsVm = new OpsVm();
                ServiceInstance si = new ServiceInstance(new URL("https://"+ serverAddress +"/sdk"),
                        username, password, true);
                SphereStandConnection connGetIp = new SphereStandConnection.Builder(si,
                        standVm.getDcName(),
                        standVm.getCloneName(),
                        "getIp" ).
                        build();
                deployedVmList.add(opsVm.getIpAddressByName(connGetIp));
            }
            //Assert.assertNotNull(vms.getVms());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return deployedVms;
        }
        deployedVms.setDeployedVms(deployedVmList);
        return deployedVms;
    }

    // Stop deployed VMs
    public boolean stop(DeployedVms deployedVms, String password) throws MalformedURLException, RemoteException, InterruptedException {
        int vmNumber = deployedVms.getDeployedVms().size();
        for (int i = 0; i < vmNumber; i++ ) {
            OpsVm opsVm = new OpsVm();
            ServiceInstance si = new ServiceInstance(new URL("https://"+ serverAddress +"/sdk"),
                    username, password, true);

            SphereStandConnection connDelVm = new SphereStandConnection.Builder(si,
                    "QA DataCenter",
                    deployedVms.getDeployedVms().get(i).getName(),
                    "poweroff" ).
                    build();

            boolean statusDestroy = opsVm.powerOpsVm(connDelVm);
        }
        return false;
    }

    // Delete deployed VMs
    public boolean destroy(DeployedVms deployedVms, String password) throws MalformedURLException, RemoteException, InterruptedException {
        int vmNumber = deployedVms.getDeployedVms().size();
        for (int i = 0; i < vmNumber; i++ ) {
            OpsVm opsVm = new OpsVm();
            ServiceInstance si = new ServiceInstance(new URL("https://"+ serverAddress +"/sdk"),
                    username, password, true);

            SphereStandConnection connDelVm = new SphereStandConnection.Builder(si,
                    "QA DataCenter",
                    deployedVms.getDeployedVms().get(i).getName(),
                    "destroy" ).
                    build();

            boolean statusDestroy = opsVm.powerOpsVm(connDelVm);
        }
        return false;
    }



}
