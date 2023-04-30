package yonam2023.sfproject.production.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class FactoryService {
    private boolean isConnected = false;
    //공장 접속 여부 식별
    private static final String machineURL="http://localhost:8085/";
    private Logger logger = LoggerFactory.getLogger(FactoryService.class);
    @Autowired
    HttpPostService httpPS;


    public boolean checkFactoryConnection(){
        logger.info("MachineService:check Factory Connection");
        try {
            String res = httpPS.sendGet(machineURL + "cntCheck/");
            logger.info("MachineService:Factory Connection Result:"+res);
            if(Boolean.valueOf(res)){
                isConnected=true;
            }
            return Boolean.valueOf(res);
        }catch(ConnectException e){
            logger.warn("MachineService:Factory Connection failed");
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean startupFactory(){
        logger.info("MachineService:attempt startup Factory");
        try {
            String res = httpPS.sendGet(machineURL + "turnOn/");
            logger.info("MachineService:receive {"+res+"}");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean pauseFactory(){
        logger.info("MachineService:attempt pause Factory");
        try {
            String res = httpPS.sendGet(machineURL + "pause/");
            logger.info("MachineService:receive {"+res+"}");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean shutdownFactory(){
        logger.info("MachineService:attempt shut down Factory");
        try {
            String res = httpPS.sendGet(machineURL + "shutdown/");
            logger.info("MachineService:receive {"+res+"}");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean getFactoryConnectionState(){
        return this.isConnected;
    }
}
