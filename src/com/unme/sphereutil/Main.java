package com.unme.sphereutil;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.unme.sphereutil.*;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class Main {

    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws MalformedURLException, RemoteException {
        Logger.getRootLogger().setLevel(Level.INFO);
        BasicConfigurator.configure();
        logger.setLevel(Level.INFO);
        logger.info("Entering application.");
        String username = args[0];
        String spherePassword = args[1];
        String yamlName = args[2];
        final AutoStand autoStand = new AutoStand();
        DeployedVms deployedVms = autoStand.run(username, spherePassword, yamlName);

        int countVms = deployedVms.getDeployedVms().size();
        for (int i = 0; i < countVms; i++) {
            logger.info("=================");
            logger.info("Name: " + deployedVms.getDeployedVms().get(i).getName());
            logger.info("Type: " + deployedVms.getDeployedVms().get(i).getType());
            logger.info("IPs: " + deployedVms.getDeployedVms().get(i).getIps());
            logger.info("Primary IP: " + deployedVms.getDeployedVms().get(i).getPrimaryIp());
            logger.info("=================");
        }

        logger.info("Exiting application.");
    }
}
