package yonam2023.sfproject.production.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam2023.sfproject.production.domain.MachineData;
import yonam2023.sfproject.production.domain.MachineRegistData;
import yonam2023.sfproject.production.domain.MachineStockAddData;
import yonam2023.sfproject.production.domain.Production;
import yonam2023.sfproject.production.repository.MachineDataRepository;
import yonam2023.sfproject.production.repository.ProductionRepository;

import java.util.*;


@Service
public class MachineService {
    @Autowired
    HttpPostService httpPS;

    @Autowired
    MachineDataRepository mr;

    @Autowired
    ProductionRepository pr;

    private Logger logger = LoggerFactory.getLogger(MachineService.class);
    private static final String machineURL="http://localhost:8085/";


    public boolean addMachine(int mid){
        logger.info("MachineService:attempt to add Machine "+mid+" by mid");
        if(isMachineInDB(mid)){
            //db에 이미 등록된 기계임
            logger.warn("MachineService:Machine "+mid+" is Already in DB");
            return false;
        }
        if(!checkMachine(mid)){
            //기계가 존재하지 않음
            logger.warn("MachineService:Machine "+mid+" is not exists");
            return false;
        }
        MachineData smd = MachineData.builder().mid(mid).name("temp").state(true).build();
        mr.save(smd);
        logger.info("MachineService:Machine "+mid+" is now registered");
        return true;
    }
    public boolean addMachine(MachineRegistData machineRegistData){
        int mid = machineRegistData.getMid();
        logger.info("MachineService:attempt to add Machine "+mid+" by Data");
        if(isMachineInDB(mid)){
            //db에 이미 등록된 기계임
            logger.warn("MachineService:Machine "+mid+" is Already in DB");
            return false;
        }
        if(!checkMachine(mid)){
            //기계가 존재하지 않음
            logger.warn("MachineService:Machine "+mid+" is not exists");
            return false;
        }
        MachineData smd = MachineData.builder()
                .mid(machineRegistData.getMid())
                .name(machineRegistData.getName())
                .state(false)
                .description(machineRegistData.getDescription())
                .build();
        mr.save(smd);
        logger.info("MachineService:Machine "+mid+" is now registered");
        return true;
    }

    @Transactional
    public boolean delMachine(int mid){
        //del machine code
        //삭제 절차 : 확인, 정지, 삭제
        logger.info("MachineService:check Machine "+mid+" exists");
        MachineData md = mr.findByMid(mid);
        if(md==null){
            //db에 없는 기계임
            logger.warn("MachineService:Machine "+mid+" is Not Exists in DB");
            return false;
        }
        if(md.isState()){
            //기계가 작동중임.
            stopMachine(mid);
        }
        mr.delete(md);//기계 목록에서 제거
        //production에서는 유지함.
        logger.info("MachineService:Machine "+mid+" was successfully delete from DB");
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
            if(Boolean.valueOf(res)){
                logger.info("MachineService:Machine "+mid+" Run Successful : "+res);
                md.setState(true);
                md.setFatal(false);
                mr.save(md);
                return true;
            }else{
                logger.info("MachineService:Machine "+mid+" failed to start up : "+res);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
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
            //기계 중지
            String res = httpPS.sendGet(machineURL + "stopMachine/"+mid);
            logger.info("MachineService:Machine "+mid+" Stop Result : "+res);
            if(Boolean.valueOf(res)){
                logger.info("MachineService:Machine "+mid+" Stop Successful : "+res);
                md.setState(false);
                mr.save(md);
                return true;
            }else{
                logger.info("MachineService:Machine "+mid+" failed to stop : "+res);
                return true;//정지 불가능 오류에 대해 고려할 필요 있음.
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean checkMachineState(int mid){
        //check Machine State code
        logger.info("MachineService:check Machine "+mid+" exists");
        if(!isMachineInDB(mid)){
            //db에 없는 기계임
            logger.warn("MachineService:Machine "+mid+" is Not Exists in DB");
            return false;
        }
        try{
            //기계 확인
            String res = httpPS.sendGet(machineURL + "checkMcState/"+mid);
            logger.info("MachineService:Machine "+mid+" Running State : "+res);
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
            logger.info("MachineService:checking Result:"+res);
            return Boolean.parseBoolean(res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean isMachineInDB(int mid) {
        MachineData md = mr.findByMid(mid);
        if (md != null) {
            //db에 이미 등록된 기계임
            logger.warn("MachineService:DB:Machine " + mid + " Exists in DB");
            return true;
        }
        return false;
    }

    public void insertSensorData(String data){
        JSONParser parser = new JSONParser();
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) parser.parse(data);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        for (int i = 0; i<jsonArray.size();i++){
            JSONObject jo = (JSONObject) jsonArray.get(i);
            logger.info("MachineService:InsertData:"+jo.toString());
            int mid = ((Long)jo.get("mid")).intValue();
            int value = ((Long)jo.get("value")).intValue();
            Production production = Production.builder()
                    .mid(mid)
                    .svalue(value)
                    .build();
            pr.save(production);
            MachineData machineData = mr.findByMid(mid);
            machineData.setRecentData(value);
            mr.save(machineData);
        }
    }

    public void fatalState(int mid){
        MachineData md = mr.findByMid(mid);
        md.setState(false);
        md.setFatal(true);
        mr.save(md);
        logger.error("Fatal Received "+mid);
    }

    public ArrayList<Integer> getFactoryMidList(){
        try {
            logger.info("MachineService:Get All Machine IDs From Factory");
            String res = httpPS.sendGet(machineURL + "getMachineIdList");
            logger.info("MachineService:Receive Machine ID List:"+res);
            //가공/코드를 좀 더 간단하게 작동하도록 수정할 필요 있음
            res = res.trim().replace("[", "").replace("]","");
            String[] arrayMids = res.split(",");
            ArrayList<String> arrayListMids = new ArrayList<String>(Arrays.asList(arrayMids));

            Iterator<String> iteratorStr = arrayListMids.iterator();
            ArrayList<Integer> intArray = new ArrayList<Integer>();
            while(iteratorStr.hasNext()){
                intArray.add(Integer.parseInt(iteratorStr.next().trim()));
            }
            intArray.sort(Comparator.naturalOrder());
            //ArrayList<MidDTO> midDTOArrayList = new ArrayList<>();
            //Iterator<Integer> iteratorInt = intArray.iterator();
            //while(iteratorInt.hasNext()){
            //    midDTOArrayList.add(new MidDTO(iteratorInt.next()));
            //}
            //arrayListMids.sort(Comparator.naturalOrder());
            //return midDTOArrayList;
            return intArray;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String addStockToMachine(MachineStockAddData data){
        String result = "-";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mid", data.getMid());
        jsonObject.put("amount", data.getAmount());
        try{
            result = httpPS.sendPost(machineURL+"addStock", jsonObject);

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
