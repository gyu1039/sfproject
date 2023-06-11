package yonam2023.sfproject.production.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam2023.sfproject.logistics.domain.StoredItem;
import yonam2023.sfproject.logistics.repository.StoredItemRepository;
import yonam2023.sfproject.production.Exception.MachineNotFoundException;
import yonam2023.sfproject.production.Exception.ResourceNotEnoughException;
import yonam2023.sfproject.production.Exception.ResourceNotFoundException;
import yonam2023.sfproject.production.domain.MachineData;
import yonam2023.sfproject.production.domain.MachineRegistData;
import yonam2023.sfproject.production.domain.MachineStockAddData;
import yonam2023.sfproject.production.domain.Production;
import yonam2023.sfproject.production.repository.MachineDataRepository;
import yonam2023.sfproject.production.repository.ProductionRepository;

import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class MachineService {
    @Autowired
    HttpPostService httpPS;

    @Autowired
    MachineDataRepository machineDataRepository;

    @Autowired
    ProductionRepository productionRepository;

    @Autowired
    SseService sseService;

    @Autowired
    StoredItemRepository storedItemRepository;

    private Logger logger = LoggerFactory.getLogger(MachineService.class);
    private static final String machineURL="http://localhost:8085/";


    public boolean addMachine(int machineId){
        logger.info("MachineService:attempt to add Machine "+machineId+" by mid");
        if (isMachineInDB(machineId)) {
            //db에 이미 등록된 기계임
            logger.warn("MachineService:Machine "+ machineId +" is Already in DB");
            return false;
        }
        if (!checkMachine(machineId)) {
            //기계가 존재하지 않음
            logger.warn("MachineService:Machine "+machineId+" is not exists");
            return false;
        }
        MachineData smd = MachineData.builder().machineId(machineId).name("temp").state(true).build();
        machineDataRepository.save(smd);
        logger.info("MachineService:Machine "+machineId+" is now registered");
        return true;
    }
    public boolean addMachine(MachineRegistData machineRegistData){
        int mid = machineRegistData.getMachineId();
        logger.info("MachineService:attempt to add Machine "+mid+" by Data");
        if (isMachineInDB(mid)) {
            //db에 이미 등록된 기계임
            logger.warn("MachineService:Machine "+mid+" is Already in DB");
            return false;
        }
        if (!checkMachine(mid)) {
            //기계가 존재하지 않음
            logger.warn("MachineService:Machine "+mid+" is not exists");
            return false;
        }
        //기계로부터 데이터 받아옴.
        MachineData receiveData = getMachineInfo(mid);

        MachineData smd = MachineData.builder()
                .machineId(machineRegistData.getMachineId())
                .name(machineRegistData.getName())
                .state(false)
                .description(machineRegistData.getDescription())
                .min(receiveData.getMin())
                .max(receiveData.getMax())
                .stock(receiveData.getStock())
                .maxStock(receiveData.getMaxStock())
                .resourceType(receiveData.getResourceType())
                .build();
        machineDataRepository.save(smd);
        logger.info("MachineService:Machine "+mid+" is now registered");
        return true;
    }

    @Transactional
    public boolean delMachine(int mid){
        //del machine code
        //삭제 절차 : 확인, 정지, 삭제
        logger.info("MachineService:check Machine "+mid+" exists");

        MachineData md = machineDataRepository.findByMachineId(mid);

        if (md==null) {
            //db에 없는 기계임
            logger.warn("MachineService:Machine "+mid+" is Not Exists in DB");
            return false;
        }
        if (md.isState()) {
            //기계가 작동중임.
            stopMachine(mid);
        }
        machineDataRepository.delete(md);//기계 목록에서 제거
        //production에서는 유지함.
        logger.info("MachineService:Machine "+mid+" was successfully delete from DB");
        return true;
    }

    public boolean runMachine(int mid){
        //run some Machine
        logger.info("MachineService:check Machine "+mid+" exists");

        MachineData md = machineDataRepository.findByMachineId(mid);

        if (md==null) {
            //db에 없는 기계임
            logger.warn("MachineService:Machine "+mid+" is Not Exists in DB");
            return false;
        }
        try {
            //기계 작동
            String res = httpPS.sendGet(machineURL + "runMachine/"+mid);
            if (Boolean.valueOf(res)) {
                logger.info("MachineService:Machine "+mid+" Run Successful : "+res);
                md.setState(true);
                md.setFatal(false);
                machineDataRepository.save(md);
                //SSE
                sseService.updateMachineState(mid+":Running");//command 패턴 적용 가능?
                sseService.updateMachineFatal(mid+":Normal");
                sseService.updateMachineDetailState(mid+":Running");
                sseService.updateMachineDetailFatal(mid+":Normal");
                return true;
            } else {
                logger.info("MachineService:Machine "+mid+" failed to start up : "+res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean stopMachine(int mid){
        //stop Some Machine
        logger.info("MachineService:check Machine "+mid+" exists");

        MachineData md = machineDataRepository.findByMachineId(mid);

        if (md==null) {
            //db에 없는 기계임
            logger.warn("MachineService:Machine "+mid+" is Not Exists in DB");
            return false;
        }
        try {
            //기계 중지
            String res = httpPS.sendGet(machineURL + "stopMachine/"+mid);
            logger.info("MachineService:Machine "+mid+" Stop Result : "+res);
            if (Boolean.valueOf(res)) {
                logger.info("MachineService:Machine "+mid+" Stop Successful : "+res);
                md.setState(false);
                machineDataRepository.save(md);

                //SSE
                sseService.updateMachineState(mid+":Stopped");
                sseService.updateMachineDetailState(mid+":Stopped");
                return true;
            } else {
                logger.info("MachineService:Machine "+mid+" failed to stop : "+res);
                return true;//정지 불가능 오류에 대해 고려할 필요 있음.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean checkMachineState(int mid){
        //check Machine State code
        logger.info("MachineService:check Machine "+mid+" exists");
        if (!isMachineInDB(mid)) {
            //db에 없는 기계임
            logger.warn("MachineService:Machine "+mid+" is Not Exists in DB");
            return false;
        }
        try {
            //기계 확인
            String res = httpPS.sendGet(machineURL + "checkMcState/"+mid);
            logger.info("MachineService:Machine "+mid+" Running State : "+res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public MachineData getMachineInfo(int mid){
        //기계 데이터 갱신에 관한 코드
        String res;
        JSONObject jo;
        try {
            //최신값 받아오기.
            res = httpPS.sendGet(machineURL + "getMachineData/"+mid);
            logger.info("MachineService:Machine "+mid+" Data : "+res);
            JSONParser parser = new JSONParser();
            jo = (JSONObject) parser.parse(res);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        MachineData machineData = MachineData.builder()
                .machineId(((Long)jo.get("machineId")).intValue())
                .name((String)jo.get("name"))
                .max(((Long)jo.get("max")).intValue())
                .min(((Long)jo.get("min")).intValue())
                .recentData(((Long)jo.get("current")).intValue())
                .state((Boolean)jo.get("state"))
                .stock(((Long)jo.get("stock")).intValue())
                .maxStock(((Long)jo.get("maxStock")).intValue())
                .resourceType((String)jo.get("resourceType"))
                .build();

        //fatal 값이 없음에 주의.
        return machineData;
    }

    public boolean checkMachine(int mid){
        //check Machine exist and state
        try {
            logger.info("MachineService:check Machine "+mid+" exists");
            String res = httpPS.sendGet(machineURL + "isMcExist/"+mid);
            logger.info("MachineService:checking Result:"+res);
            return Boolean.parseBoolean(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isMachineInDB(int mid) {
        MachineData md = machineDataRepository.findByMachineId(mid);
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
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<jsonArray.size();i++) {
            JSONObject jo = (JSONObject) jsonArray.get(i);
            logger.info("MachineService:InsertData:"+jo.toString());

            int machineId = ((Long)jo.get("machineId")).intValue();//mid 추출
            int value = ((Long)jo.get("value")).intValue();//현재값 추출
            int used = ((Long)jo.get("used")).intValue();//사용량
            int stock = ((Long)jo.get("stock")).intValue();//남은 재료
            boolean hasOutput = (boolean)jo.get("hasOutput");

            if (hasOutput) {
                //출력이 있음.
                String outputType = "error";
                try {
                    outputType= new String(((String)jo.get("productType")).getBytes(StandardCharsets.UTF_8),StandardCharsets.UTF_8);
                } catch (Exception e){
                    e.printStackTrace();
                }
                int output = ((Long)jo.get("output")).intValue();
                StoredItem storedItem = storedItemRepository.findByName(outputType);

                if (storedItem == null) {
                    //이러한 재고 유형이 창고에 없음.
                    storedItemRepository.save(new StoredItem(outputType, output));
                } else {
                    //창고에 저장
                    storedItem.setAmount(storedItem.getAmount() + output);
                    storedItemRepository.save(storedItem);
                }
            }

            Production production = Production.builder()
                    .machineId(machineId)
                    .svalue(value)
                    .used(used)
                    .build();
            productionRepository.save(production);

            MachineData machineData = machineDataRepository.findByMachineId(machineId);
            machineData.setRecentData(value);

            machineDataRepository.save(machineData);

            sb.append(machineId+":"+value+":"+stock+",");

            sseService.updateMachineDetailGraph(machineId);
            sseService.updateMachineDetailStock(machineId+":"+stock+"/"+machineData.getMaxStock());
        }
        //SSE
        if(sb.length()>0) sb.deleteCharAt(sb.length()-1);
        sseService.updateMachineData(sb.toString());
        //MachineDetail 페이지 데이터 갱신고려
    }

    public void fatalState(int mid){

        MachineData md = machineDataRepository.findByMachineId(mid);

        md.setState(false);
        md.setFatal(true);

        machineDataRepository.save(md);

        logger.error("Fatal Received "+mid);

        //SSE
        sseService.updateMachineFatal(mid+":Error");
        sseService.updateMachineState(mid+":Stopped");
        sseService.updateMachineDetailFatal(mid+":Error");
        sseService.updateMachineDetailState(mid+":Stopped");
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

            return intArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String addStockToMachine(MachineStockAddData data) throws Exception {

        //재고를 감소시킬 것.

        MachineData machineData = machineDataRepository.findByMachineId(data.getMachineId());
        StoredItem storedItem = storedItemRepository.findByName(machineData.getResourceType());//창고 재고 가져오기

        if (machineData == null) {
            throw new MachineNotFoundException();
        }
        if (storedItem == null) {
            //재고가 존재하지않음.
            throw new ResourceNotFoundException();
        }

        if (storedItem.getAmount() - data.getAmount()<0) {
            //창고 재고가 부족함
            throw new ResourceNotEnoughException();
        }

        String result = "-";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mid", data.getMachineId());
            jsonObject.put("amount", data.getAmount());

            //result에 적재하지 못한 만큼의 재고가 반환됨.
            result = httpPS.sendPost(machineURL+"addStock", jsonObject);

            //DB에 반영
            machineData.setStock(machineData.getStock()+data.getAmount()>machineData.getMaxStock() ? machineData.getMaxStock() : machineData.getStock()+ data.getAmount());
            machineDataRepository.save(machineData);
            sseService.updateMachineDetailStock(data.getMachineId()+":"+machineData.getStock()+"/"+machineData.getMaxStock());

            storedItem.setAmount(storedItem.getAmount() - data.getAmount() + Integer.parseInt(result));//충전하지 못한 재고 만큼 다시 보충

            storedItemRepository.save(storedItem);//재고 깎음.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
