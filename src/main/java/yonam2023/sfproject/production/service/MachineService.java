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
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class MachineService {
    @Autowired
    HttpPostService httpPS;

    @Autowired
    MachineDataRepository mr;

    private static final String machineURL="http://localhost:8085/";

    public boolean addMachine(int id){
        System.out.println("MachineService:check Machine "+id+" exist");
        MachineData md = mr.findByMid(id);
        if(md!=null){
            System.out.println("MachineService:Machine "+id+" is Already in DB");
            return false;
        }
        if(!checkMachine(id)){
            System.out.println("MachineService:Machine "+id+" is not exist");
            return false;
        }
        MachineData smd = MachineData.builder().mid(id).name("temp").status(true).build();
        mr.save(smd);
        System.out.println("MachineService:Machine "+id+" is now registered");
        return true;
    }

    public void runMachine(){
        //run some Machine
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
}
