package yonam2023.sfproject.production.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yonam2023.sfproject.production.domain.MachineData;
import yonam2023.sfproject.production.repository.MachineDataRepository;
import yonam2023.sfproject.production.repository.ProductionRepository;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class MachineService {
    @Autowired
    HttpPostService httpPS;

    @Autowired
    MachineDataRepository mr;

    private static final String machineURL="http://localhost:8085/";

    public boolean checkFactory(){
        System.out.println("MachineService:check Factory Connection");
        try {
            String res = httpPS.sendGet(machineURL + "cntCheck/");
            return Boolean.valueOf(res);
        }catch(ConnectException e){
            System.out.println("MachineService:Factory Connection failed");
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean addMachine(int id){
        System.out.println("MachineService:check Machine "+id+" exists");
        MachineData md = mr.findByMid(id);
        if(md!=null){
            //db에 이미 등록된 기계임
            System.out.println("MachineService:Machine "+id+" is Already in DB");
            return false;
        }
        if(!checkMachine(id)){
            //기계가 존재하지 않음
            System.out.println("MachineService:Machine "+id+" is not exists");
            return false;
        }
        MachineData smd = MachineData.builder().mid(id).name("temp").status(true).build();
        mr.save(smd);
        System.out.println("MachineService:Machine "+id+" is now registered");
        return true;
    }

    public boolean runMachine(int id){
        //run some Machine
        System.out.println("MachineService:check Machine "+id+" exists");
        MachineData md = mr.findByMid(id);
        if(md==null){
            //db에 없는 기계임
            System.out.println("MachineService:Machine "+id+" is Not Exists in DB");
            return false;
        }
        try {
            String res = httpPS.sendGet(machineURL + "runMachine/"+id);
            System.out.println("MachineService:"+res);
        }catch (Exception e){
            System.out.println(e);
        }

        return true;
    }

    public void stopMachine(){
        //stop Some Machine
    }

    public void getMachineInfo(int id){

    }

    public boolean checkMachine(int id){
        //check Machine exist and state
        try {
            String res = httpPS.sendGet(machineURL + "isMcExist/"+id);
            return Boolean.parseBoolean(res);
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    public void fatalState(int McId){
        System.out.println("Fatal Received "+McId);
    }
}
