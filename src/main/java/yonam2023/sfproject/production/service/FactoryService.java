package yonam2023.sfproject.production.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class FactoryService {
    private boolean isConnected = false;

    private boolean workingOn = false;
    //공장 접속 여부 식별
    private static final String machineURL="http://localhost:8085/";
    private Logger logger = LoggerFactory.getLogger(FactoryService.class);
    @Autowired
    HttpPostService httpPS;


    public boolean checkFactoryConnection(){
        logger.info("MachineService:check Factory Connection");
        try {
            String res = httpPS.sendGet(machineURL + "cntCheck/");
            logger.info("FactoryService:Factory Connection Result:"+res);
            if(Boolean.valueOf(res)){
                isConnected=true;
            }
            return Boolean.valueOf(res);
        }catch(ConnectException e){
            logger.warn("FactoryService:Factory Connection failed with Exception");
            e.printStackTrace();
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
            boolean result = Boolean.valueOf(res);
            if(result){//ture일때만 값 변동. 이미켜져있으면 값 변동 없음.
                this.workingOn = true;
            }
            return workingOn;
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
            return Boolean.valueOf(res);
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
            this.isConnected=false;
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean getFactoryOperationState(){
        logger.info("MachineService:check Factory Operation State");
        try {
            String res = httpPS.sendGet(machineURL + "chkOperation/");
            logger.info("MachineService:receive {"+res+"}");
            this.workingOn=Boolean.valueOf(res);
            return workingOn;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public boolean getFactoryConnectionState(){
        return this.isConnected;
    }

}
