package yonam2023.sfproject.production.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.production.domain.*;
import yonam2023.sfproject.production.repository.MachineDataRepository;
import yonam2023.sfproject.production.repository.ProductionRepository;
import yonam2023.sfproject.production.service.FactoryService;
import yonam2023.sfproject.production.service.MachineService;
import yonam2023.sfproject.production.service.SseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/production")
public class ProductionController {

    private Logger logger = LoggerFactory.getLogger(ProductionController.class);
    @Autowired
    ProductionRepository pr;
    @Autowired
    MachineDataRepository mr;
    @Autowired
    FactoryService fs;
    @Autowired
    MachineService ms;
    @Autowired
    SseService se;

    //주로 뷰 관리 코드를 위치 시킬것.
    /*
    @GetMapping
    public String initGet(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.info("ProductionController:Factory Connection State:"+fs.getFactoryConnectionState());
        if(!fs.getFactoryConnectionState()){
            return "production/connectionCheck";
        }

        Page<Production> all = pr.findAll(pageable);
        List<Production> graph = pr.findTop10ByOrderByIdDesc();
        List<String> ids = new ArrayList<String>();
        List<Integer> values = new ArrayList<Integer>();

        for(int i = graph.size()-1; i>=0; i--){
            Production p = graph.get(i);
            ids.add(Long.toString(p.getId()));
            values.add(p.getSvalue());
        }

        model.addAttribute("list", all);
        model.addAttribute("sType", "testSensor");
        model.addAttribute("gIds", ids);
        model.addAttribute("gValues", values);

        return "production/init";
    }
     */
    @GetMapping
    public String initGet(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.info("ProductionController:Factory Connection State:"+fs.getFactoryConnectionState());
        if(!fs.getFactoryConnectionState()){
            return "production/connectionCheck";
        }else{
            model.addAttribute("factoryConnectionState", "Connected");
        }
        if(fs.getFactoryOperationState()){
            model.addAttribute("factoryOperationState", "In Operation");
        }else{
            model.addAttribute("factoryOperationState", "Stopped");
        }

        Page<MachineData> all = mr.findAll(pageable);

        model.addAttribute("pageObj", all);
        return "production/machineOverview";
    }

    @GetMapping("/factoryManagement")
    public String factoryManagementPage(Model model){
        logger.info("ProductionController:factoryManagementPage called");
        if(!fs.getFactoryConnectionState()){
            return "production/connectionCheck";
        }else{
            model.addAttribute("factoryConnectionState", "Connected");
        }
        if(fs.getFactoryOperationState()){
            model.addAttribute("factoryOperationState", "In Operation");
        }else{
            model.addAttribute("factoryOperationState", "Stopped");
        }

        return "production/factoryManagement";
    }
    @GetMapping("/machineRegistration")
    public String machineRegistrationPage(Model model){
        logger.info("ProductionController:machineRegistration called");
        MachineRegistData machineRegistData = new MachineRegistDataBuilder().mid(0).build();
        model.addAttribute("machineRegistData", machineRegistData);
        List<Integer> midList = ms.getFactoryMidList();
        model.addAttribute("midList", midList);

        return "production/machineRegistration";
    }

    @PostMapping("/machineRegistration")
    public String addMachinePost(@ModelAttribute  MachineRegistData machineRegistData, Model model){
        //add Machine code
        logger.info("ProductionController:attempt add Machine\n\tMachine id : "+machineRegistData.getMid()+"\n\tMachine name : "+machineRegistData.getName()+"\n\tMachine description:"+machineRegistData.getDescription());
        //logger.info("MachineController:attempt add Machine "+mid);
        //ms.addMachine(mid);
        int mid = machineRegistData.getMid();
        if(ms.addMachine(machineRegistData)){
            logger.info("ProductionController:Adding Machine "+mid+" is successfully done");
            return "redirect:/production";
        }else{
            logger.info("ProductionController:Adding Machine "+mid+" is failed");
            machineRegistData.setMachineExists(false);
            model.addAttribute("machineRegistData", machineRegistData);
            model.addAttribute("failedReason","Duplicated machine id");
            List<Integer> midList = ms.getFactoryMidList();
            model.addAttribute("midList", midList);

            return "production/machineRegistration";
        }
    }

    @GetMapping("/machineDetail/{mid}")
    public String machineDetailGet(@PathVariable("mid") int mid, Model model){
        logger.info("ProductionController:Request Machine "+mid+" detail");
        if(!ms.isMachineInDB(mid)){
            logger.info("ProductionController:Requested Machine "+mid+" is not registered");
            return "redirect:/production";
        }
        //기계 데이터를 요청해서 최신 데이터로 받아올 필요가 있음.
        //ex)Min, Max 값, Stock 최대치, Stock 현재치 등등.
        MachineData newData = ms.getMachineInfo(mid);
        MachineData machineData = mr.findByMid(mid);
        //갱신
        machineData.setStock(newData.getStock());
        //graphData
        List<Production> graph = pr.findTop10ByMidOrderByIdDesc(mid);
        List<String> ids = new ArrayList<String>();
        List<Integer> values = new ArrayList<Integer>();

        for(int i = graph.size()-1; i>=0; i--){
            Production p = graph.get(i);
            ids.add(Long.toString(p.getId()));
            values.add(p.getSvalue());
        }

        model.addAttribute("sType", "testSensor");
        model.addAttribute("gIds", ids);
        model.addAttribute("gValues", values);

        model.addAttribute("machineData", machineData);

        //machine이 가진 재고와 소비량을 반환 하는 코드 필요.

        return "production/machineDetail";
    }

    @GetMapping("/getAll")
    public @ResponseBody List<Production> productionGetAll(){
        return pr.findAll();
    }

    @GetMapping("/getTen")//10개만 구하기
    public @ResponseBody List<Production> productionGetTen(){
        return pr.findTop10ByOrderByIdDesc();
    }

    @GetMapping("/{id}")
    public @ResponseBody Production productionGetAll(@PathVariable long id){
        Optional<Production> oProduction =  pr.findById(id);
        if(oProduction.isEmpty()){
            return Production.builder().build();
        }else{
            return oProduction.get();
        }
    }

    @PostMapping("/update")
    public @ResponseBody String productionUpdate(@RequestBody Map<String, String> param){

        Long id = Long.parseLong(param.get("ID"));
        String stype = param.get("STYPE");
        int svalue = Integer.parseInt(param.get("SVALUE"));

        Optional<Production> oProduction =  pr.findById(id);
        if(oProduction.isEmpty()){
            return "그런 값은 존재하지 않음.";
        }else{
            Production production = oProduction.get();
            production.setSvalue(svalue);

            pr.save(production);

            return "ID가 "+id.toString()+"인 튜플이 stype : " + stype + " / svalue : " + svalue + "로 수정됨.";
        }
    }

    /*
    @PostMapping("/insert")
    public @ResponseBody List<Production> productionInsert(@RequestBody Map<String, String> param){
        String stype = param.get("STYPE");
        System.out.println(param.get("SVALUE"));
        int svalue = Integer.parseInt(param.get("SVALUE"));
        Production production= Production.builder().stype(stype).svalue(svalue).build();
        pr.save(production);

        return pr.findAll();
    }
     */

    @PostMapping("/delete")
    public @ResponseBody List<Production> productionDelete(@RequestBody Map<String, String> param){
        Long id = Long.parseLong(param.get("ID"));
        List<Production> lProduction = new ArrayList<>();

        Optional<Production> oProduction =  pr.findById(id);
        if(oProduction.isEmpty()){
            Production rp = Production.builder().build();
            lProduction.add(rp);
            return lProduction;
        }else{
            pr.deleteById(id);

            return pr.findAll();
        }
    }

    @GetMapping("/deleteAll")
    public @ResponseBody List<Production> productionDeleteAll(){
        pr.deleteAll();
        return pr.findAll();
    }

    //추후 id에 따른 재고 추가 구현해야함.
    @GetMapping("/addStock")
    public String addStockGet(Model model){
        model.addAttribute("machineStockAddData", new MachineStockAddData(1010, 100, 1000));
        return "production/machineStockAdd";
    }

    @PostMapping("/addStock")
    public String addStockPost(@ModelAttribute("machineStockAddData") MachineStockAddData data, Model model){
        //기계로 재료를 보내는 코드.
        //생산 부서에 충분한 재고가 있는지 점검하는 코드 필요.
        //해당 재고를 검색해서 현재 양이 얼마인지 표시하면 좋음.
        //->별도 페이지로 구성?

        //test code
        logger.info("MachineController:Receive Add Stock :"+data.getAmount()+" to "+data.getMid());
        String result = ms.addStockToMachine(data);
        if(result.equals("Machine Not Found")){
            logger.info("Machine Not Found");
            model.addAttribute("machineStockAddData", data);
            return "production/machineStockAdd";
        }
        //추후 수정 필요
        logger.info("MachineController:Stock Successfully Added : "+result);
        model.addAttribute("machineStockAddData", data);
        return "production/machineStockAdd";
    }
    @GetMapping("/addStock/{mid}")
    public String addStockGetByMid(@PathVariable("mid")int mid, Model model){
        //mid로 여는 재고 페이지
        //페이지 열때 해당 mid의 기계가 있는지 체크

        logger.info("ProductionController:Adding Stock Resources to Machine "+mid+" Page");
        if(!ms.isMachineInDB(mid)){
            logger.info("ProductionController:Requested Machine "+mid+" is not registered");
            return "redirect:/production";
        }
        //기계 정보를 받아오는 코드 작성 요
        MachineData machineData = mr.findByMid(mid);
        
        model.addAttribute("machineStockAddData", new MachineStockAddData(mid, 100, machineData.getMaxStock()));
        return "production/machineStockAdd";
    }
    @PostMapping("/addStock/{mid}")
    public String addStockPost(@PathVariable("mid")int mid, @ModelAttribute("machineStockAddData") MachineStockAddData data, Model model){
        //mid로 여는 재고 페이지
        //지정 불가능이므로 생략
        //logger.info("ProductionController:Adding Stock Resources to Machine "+mid);
        //if(!ms.isMachineInDB(mid)){
        //    logger.info("ProductionController:Requested Machine "+mid+" is not registered");
        //    return "redirect:/production";
        //}
        //재고량 체크후 초과일때 되돌리는 코드도 여기 포함하면 됨

        MachineData machineData = mr.findByMid(mid);

        //test code
        logger.info("MachineController:Receive Add Stock :"+data.getAmount()+" to "+data.getMid());
        String result = ms.addStockToMachine(data);
        if(result.equals("Machine Not Found")){
            logger.info("Machine Not Found");
            model.addAttribute("machineStockAddData", data);
            return "production/machineStockAdd";
        }

        logger.info("MachineController:Stock Successfully Added : "+result);
        model.addAttribute("machineStockAddData", data);
        model.addAttribute("flag", "close");
        return "production/machineStockAdd";
    }
}