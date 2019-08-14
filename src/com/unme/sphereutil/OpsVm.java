package com.unme.sphereutil;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

import java.rmi.RemoteException;
import java.util.TreeSet;
import java.util.logging.Logger;


public class OpsVm {

    private DeployedVm deployedVms;

    public boolean powerOpsVm(SphereStandConnection conn) throws RemoteException, InterruptedException {
        boolean status = false;
        Datacenter dc;
        VirtualMachine vm = null;
        Folder folder;
        Logger log = Logger.getLogger(OpsVm.class.getName());


        try {
                vm = findVmInDataCenter(conn);

                if("reboot".equalsIgnoreCase(conn.getOperation()))
                {
                    vm.rebootGuest();
                    System.out.println(conn.getVmName() + " guest OS rebooted");
                    status = true;
                }

                else if("poweron".equalsIgnoreCase(conn.getOperation())) {
                    Task task = vm.powerOnVM_Task(null);

                    if(task.waitForTask().equals(Task.SUCCESS))
                    {
                        System.out.println(conn.getVmName() + " powered on");
                        status = true;
                    }
                }

                else if("poweroff".equalsIgnoreCase(conn.getOperation()))
                {
                    Task task = vm.powerOffVM_Task();
                    if(task.waitForTask().equals(Task.SUCCESS))
                    {
                        System.out.println(conn.getVmName() + " powered off");
                        status = true;
                    }
                }

                else if("reset".equalsIgnoreCase(conn.getOperation()))
                {
                    Task task = vm.resetVM_Task();
                    if(task.waitForTask().equals(Task.SUCCESS))
                    {
                        System.out.println(conn.getVmName() + " reset");
                        status = true;
                    }
                }

                else if("standby".equalsIgnoreCase(conn.getOperation()))
                {
                    vm.standbyGuest();
                    System.out.println(conn.getVmName() + " guest OS standby");
                    status = true;
                }

                else if("suspend".equalsIgnoreCase(conn.getOperation()))
                {
                    Task task = vm.suspendVM_Task();
                    if(task.waitForTask().equals(Task.SUCCESS))
                    {
                        System.out.println(conn.getVmName() + " suspended");
                    }
                }

                else if("shutdown".equalsIgnoreCase(conn.getOperation()))
                {
                    Task task = vm.suspendVM_Task();
                    if(task.waitForTask().equals(Task.SUCCESS))
                    {
                        System.out.println(conn.getVmName() + " shutdown");
                        status = true;
                    }
                }

                else if("destroy".equalsIgnoreCase(conn.getOperation())) {
                    Task task = vm.destroy_Task();
                    if(task.waitForTask().equals(Task.SUCCESS))
                    {
                        System.out.println(conn.getVmName() + " destroyed");
                        status = true;
                    }
                }

                else {
                    System.out.println("Invalid operation. Exiting...");
                    status = false;
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        conn.getSi().getServerConnection().logout();
        return status;
    }


    private static ManagedObjectReference findSnapshotInTree(VirtualMachineSnapshotTree[] snapTree, String snapName) {
        for(int i=0; i<snapTree.length; i++) {
            VirtualMachineSnapshotTree node = snapTree[i];
            if(snapName.equals(node.getName())) {
                return node.getSnapshot();
            }
            else {
                VirtualMachineSnapshotTree[] childTree = node.getChildSnapshotList();
                if(childTree!=null) {
                    ManagedObjectReference mor = findSnapshotInTree(childTree, snapName);
                    if(mor!=null) {
                        return mor;
                    }
                }
            }
        }
        return null;
    }

    public DeployedVm getIpAddressByName(SphereStandConnection sphereStendConnection) throws RemoteException {
        DeployedVm deployedVm = new DeployedVm();
        VirtualMachine virtualMachine;
        TreeSet<String> ipAddresses = new TreeSet<String>();
        virtualMachine = findVmInDataCenter(sphereStendConnection);

        // add the Ip address reported by VMware tools, this should be primary
        if (virtualMachine.getGuest().getIpAddress() != null)
            deployedVm.setPrimaryIp(virtualMachine.getGuest().getIpAddress());
            //System.out.println(virtualMachine.getGuest().getIpAddress());

        // if possible, iterate over all virtual networks networks and add interface Ip addresses
        if (virtualMachine.getGuest().getNet() != null) {
            for (GuestNicInfo guestNicInfo : virtualMachine.getGuest().getNet()) {
                if (guestNicInfo.getIpAddress() != null) {
                    for (String ipAddress : guestNicInfo.getIpAddress()) {
                        ipAddresses.add(ipAddress);
                    }
                }
            }
            deployedVm.setIps(ipAddresses);
        }

        if (virtualMachine.getGuest().getGuestFamily() != null) {
            deployedVm.setOsFamily(virtualMachine.getGuest().getGuestFamily());
        }

        if (virtualMachine.getGuest().getGuestFullName() != null) {
            deployedVm.setType(virtualMachine.getGuest().getGuestFullName());
        }
        if (virtualMachine.getName() != null) {
            deployedVm.setName(virtualMachine.getName());
        }
        return deployedVm;
    }
    private static VirtualMachine findVmInDataCenter(SphereStandConnection conn) throws RemoteException {
        VirtualMachine vm = null;
        Datacenter dc;
        dc = (Datacenter) new InventoryNavigator(conn.getSi().getRootFolder()).searchManagedEntity("Datacenter",
                conn.getDcName());
        System.out.println("DC Name::" + dc.getName());
        Folder rootFolder = dc.getVmFolder();
        try {
            vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", conn.getVmName());
            System.out.println("VM Name::" + vm.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (vm == null) {
            String logMessage = "No VM " + conn.getVmName() + " found";
            System.out.println(logMessage);
            conn.getSi().getServerConnection().logout();
        }
        return vm;
    }

    public boolean cloneAndStartVM(SphereStandConnection conn) throws RemoteException, InterruptedException {
        VirtualMachine vm;
        Folder folder;
        boolean status = false;

        try {
                vm = findVmInDataCenter(conn);
                VirtualMachineRelocateSpec rSpec = new VirtualMachineRelocateSpec();
                VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
                VirtualMachineSnapshotTree[] snapTree = vm.getSnapshot().getRootSnapshotList();
                ManagedObjectReference snapMOR = findSnapshotInTree(snapTree,conn.getSnapName());

                //if (snapMOR != null) {
                    // ArrayList<Integer> independentVirtualDiskKeys = independentVirtualDiskKeys();
               // }


                // Set Folder
                folder = (Folder) vm.getParent();

                // Set clone resource pool
                System.out.println("Using parent resource pool " + conn.getResourcePool());
                ResourcePool vmResourcePool;

                vmResourcePool = vm.getResourcePool();

                ManagedObjectReference rsMOR = vmResourcePool.getMOR();
                rSpec.setPool(rsMOR);

                // Log resource pool owner
                try {
                    System.out.println("Resource pool owner: " + vmResourcePool.getOwner());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                // Set specific host
                //HostSystem host = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity("HostSystem", hostName);
                //rSpec.setHost(host.getMOR());

                // Set data store
                ManagedObjectReference ds = new ManagedObjectReference();
                //rSpec.setDatastore(ds);

                rSpec.setDiskMoveType("createNewChildDiskBacking");

                cloneSpec.setLocation(rSpec);
                cloneSpec.setPowerOn(conn.getPowerOn());
                cloneSpec.setTemplate(false);
                cloneSpec.setSnapshot(snapMOR);

                /*if (!conn.getIp().equals("dhcp")) {
                    CustomizationSpec customSpec = new CustomizationSpec();
                    CustomizationGlobalIPSettings gIp = new CustomizationGlobalIPSettings();
                    customSpec.setGlobalIPSettings(gIp);

                    CustomizationFixedIp staticIp = new CustomizationFixedIp();
                    staticIp.setIpAddress(conn.getIp());
                    System.out.println("Test setting static IP" + staticIp.getIpAddress());

                    CustomizationIPSettings adapter = new CustomizationIPSettings();
                    adapter.setIp(staticIp);
                    adapter.setDnsServerList(new String[] {"192.168.87.15"});
                    adapter.setGateway(new String[] {"192.168.0.6"});
                    adapter.setSubnetMask("255.255.0.0");
                    CustomizationAdapterMapping adapterMap = new CustomizationAdapterMapping();
                    adapterMap.setAdapter(adapter);

                    CustomizationAdapterMapping[] nicSettingMap = new CustomizationAdapterMapping[]{adapterMap};
                    customSpec.setNicSettingMap(nicSettingMap);
                    cloneSpec.setCustomization(customSpec);

                }*/


                /* Exec */

                Task task = vm.cloneVM_Task(folder, conn.getCloneName(), cloneSpec);
                System.out.println("Launching the VM clone task. It might take a while. Please wait for the result ...");
                String taskStatus = task.waitForTask();

                if(taskStatus.equals(Task.SUCCESS)) {
                    System.out.println("Virtual Machine got cloned successfully.");
                    status = true;
                    conn.getSi().getServerConnection().logout();
                    return true;
                }
                else {
                    System.out.println("Failure -: Virtual Machine cannot be cloned");
                    return false;
                }

            }
         catch (Exception e) {
            e.printStackTrace();
        }
        conn.getSi().getServerConnection().logout();
        return status;
    }

}
