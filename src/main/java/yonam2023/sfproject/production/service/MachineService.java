package yonam2023.sfproject.production.service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yonam2023.sfproject.production.domain.MachineData;
import yonam2023.sfproject.production.repository.MachineDataRepository;

import java.net.ConnectException;

@Service
public class MachineService {
    @Autowired
    HttpPostService httpPS;

    @Autowired
    MachineDataRepository mr;

    private Logger logger = LoggerFactory.getLogger(MachineService.class);
    private static final String machineURL="http://localhost:8085/";

    public boolean checkFactory(){
        logger.info("MachineService:check Factory Connection");
        try {
            String res = httpPS.sendGet(machineURL + "cntCheck/");
            return Boolean.valueOf(res);
        }catch(ConnectException e){
            logger.warn("MachineService:Factory Connection failed");
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean wakeFactory(){
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

    public boolean stopFactory(){
        logger.info("MachineService:attempt shut down Factory");
        try {
            String res = httpPS.sendGet(machineURL + "turnOff/");
            logger.info("MachineService:receive {"+res+"}");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean addMachine(int mid){
        logger.info("MachineService:check Machine "+mid+" exists");
        MachineData md = mr.findByMid(mid);
        if(md!=null){
            //db에 이미 등록된 기계임
            logger.warn("MachineService:Machine "+mid+" is Already in DB");
            return false;
        }
        if(!checkMachine(mid)){
            //기계가 존재하지 않음
            logger.warn("MachineService:Machine "+mid+" is not exists");
            return false;
        }
        MachineData smd = MachineData.builder().mid(mid).name("temp").status(true).build();
        mr.save(smd);
        logger.info("MachineService:Machine "+mid+" is now registered");
        return true;
    }

    public boolean runMachine(int mid){
        //run some Machine
        logger.info("MachineService:check Machine "+mid+" exists");
        MachineData md = mr.findByMid(mid);
        if(md==null){
            //db에 없는 기계임
            logger.warn("MachineService:Machine "+mid+" is Not Exists in DB");
            return false;
        }
        try {
            //기계 작동
            String res = httpPS.sendGet(machineURL + "runMachine/"+mid);
            logger.info("MachineService:"+res);
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public boolean stopMachine(int mid){
        //stop Some Machine
        logger.info("MachineService:check Machine "+mid+" exists");
        MachineData md = mr.findByMid(mid);
        if(md==null){
            //db에 없는 기계임
            logger.warn("MachineService:Machine "+mid+" is Not Exists in DB");
            return false;
        }
        try {
            //기계 작동
            String res = httpPS.sendGet(machineURL + "stopMachine/"+mid);
            logger.info("MachineService:"+res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public void getMachineInfo(int mid){

    }

    public boolean checkMachine(int mid){
        //check Machine exist and state
        try {
            logger.info("MachineService:check Machine "+mid+" exists");
            String res = httpPS.sendGet(machineURL + "isMcExist/"+mid);
            return Boolean.parseBoolean(res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void fatalState(int mid){
        logger.error("Fatal Received "+mid);
    }
}
