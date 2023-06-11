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
import yonam2023.sfproject.production.Exception.MachineNotFoundException;
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
    ProductionRepository productionRepository;
    @Autowired
    MachineDataRepository machineDataRepository;
    @Autowired
    FactoryService factoryService;
    @Autowired
    MachineService machineService;
    @Autowired
    SseService sseService;

    //주로 뷰 관리 코드를 위치 시킬것.

    @GetMapping
    public String overView(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.info("ProductionController:Factory Connection State:"+factoryService.getFactoryConnectionState());
        if (!factoryService.getFactoryConnectionState()) {
            return "production/factory_connection_check";
        } else {
            model.addAttribute("factoryConnectionState", "Connected");
        }
        if (factoryService.getFactoryOperationState()) {
            model.addAttribute("factoryOperationState", "In Operation");
        } else {
            model.addAttribute("factoryOperationState", "Stopped");
        }

        Page<MachineData> all = machineDataRepository.findAll(pageable);

        model.addAttribute("pageObj", all);
        return "production/machine_overview";
    }

    @GetMapping("/factory-management")
    public String factoryManagementPage(Model model){
        logger.info("ProductionController:factoryManagementPage called");
        if(!factoryService.getFactoryConnectionState()){
            return "production/factory_connection_check";
        }else{
            model.addAttribute("factoryConnectionState", "Connected");
        }
        if(factoryService.getFactoryOperationState()){
            model.addAttribute("factoryOperationState", "In Operation");
        }else{
            model.addAttribute("factoryOperationState", "Stopped");
        }

        return "production/factory_management";
    }

    @GetMapping("/machine-registration")
    public String machineRegistrationPage(Model model){
        logger.info("ProductionController:machineRegistration called");
        MachineRegistData machineRegistData = new MachineRegistDataBuilder().machineId(0).build();
        model.addAttribute("machineRegistData", machineRegistData);
        List<Integer> midList = machineService.getFactoryMidList();
        model.addAttribute("midList", midList);

        return "production/machine_registration";
    }

    @PostMapping("/machine-registration")
    public String addMachinePost(@ModelAttribute  MachineRegistData machineRegistData, Model model){
        //add Machine code
        logger.info("ProductionController:attempt add Machine\n\tMachine id : "+machineRegistData.getMachineId()+"\n\tMachine name : "+machineRegistData.getName()+"\n\tMachine description:"+machineRegistData.getDescription());
        //logger.info("MachineController:attempt add Machine "+machineId);
        //ms.addMachine(machineId);
        int machineId = machineRegistData.getMachineId();
        if (machineService.addMachine(machineRegistData)) {
            logger.info("ProductionController:Adding Machine "+machineId+" is successfully done");
            return "redirect:/production";
        } else {
            logger.info("ProductionController:Adding Machine "+machineId+" is failed");
            machineRegistData.setMachineExists(false);
            model.addAttribute("machineRegistData", machineRegistData);
            model.addAttribute("failedReason","Duplicated machine id");
            List<Integer> midList = machineService.getFactoryMidList();
            model.addAttribute("midList", midList);

            return "production/machine_registration";
        }
    }

    @GetMapping("/machine-detail/{machine_id}")
    public String machineDetailGet(@PathVariable("machine_id") int machineId, Model model){
        logger.info("ProductionController:Request Machine "+machineId+" detail");
        if(!machineService.isMachineInDB(machineId)){
            logger.info("ProductionController:Requested Machine "+machineId+" is not registered");
            return "redirect:/production";
        }
        //기계 데이터를 요청해서 최신 데이터로 받아올 필요가 있음.
        //ex)Min, Max 값, Stock 최대치, Stock 현재치 등등.
        MachineData newData = machineService.getMachineInfo(machineId);
        MachineData machineData = machineDataRepository.findByMachineId(machineId);
        //갱신
        machineData.setStock(newData.getStock());
        //graphData
        List<Production> graph = productionRepository.findTop10ByMachineIdOrderByIdDesc(machineId);
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

        return "production/machine_detail";
    }

    @GetMapping("/getAll")
    public @ResponseBody List<Production> productionGetAll(){
        return productionRepository.findAll();
    }

    @GetMapping("/getTen")//10개만 구하기
    public @ResponseBody List<Production> productionGetTen(){
        return productionRepository.findTop10ByOrderByIdDesc();
    }

    @GetMapping("/{id}")
    public @ResponseBody Production productionGetAll(@PathVariable long id){
        Optional<Production> oProduction =  productionRepository.findById(id);
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

        Optional<Production> oProduction =  productionRepository.findById(id);
        if(oProduction.isEmpty()){
            return "그런 값은 존재하지 않음.";
        }else{
            Production production = oProduction.get();
            production.setSvalue(svalue);

            productionRepository.save(production);

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

        Optional<Production> oProduction =  productionRepository.findById(id);
        if(oProduction.isEmpty()){
            Production rp = Production.builder().build();
            lProduction.add(rp);
            return lProduction;
        }else{
            productionRepository.deleteById(id);

            return productionRepository.findAll();
        }
    }

    @GetMapping("/deleteAll")
    public @ResponseBody List<Production> productionDeleteAll(){
        productionRepository.deleteAll();
        return productionRepository.findAll();
    }

    @GetMapping("/stock-add/{machineId}")
    public String addStockGetByMid(@PathVariable("machineId")int machineId, Model model){
        //mid로 여는 재고 페이지
        //페이지 열때 해당 mid의 기계가 있는지 체크

        logger.info("ProductionController:Adding Stock Resources to Machine "+machineId+" Page");
        if(!machineService.isMachineInDB(machineId)){
            logger.info("ProductionController:Requested Machine "+machineId+" is not registered");
            return "redirect:/production";
        }
        //기계 정보를 받아오는 코드 작성 요
        MachineData machineData = machineDataRepository.findByMachineId(machineId);

        model.addAttribute("machineStockAddData", new MachineStockAddData(machineId, 100, machineData.getMaxStock()));
        return "production/machine_stock_add";
    }
    @PostMapping("/stock-add/{machineId}")
    public String addStockPost(@PathVariable("machineId")int machineId, @RequestBody MachineStockAddData data, Model model){
        //mid로 여는 재고 페이지
        //지정 불가능이므로 생략
        //logger.info("ProductionController:Adding Stock Resources to Machine "+machineId);
        //if(!ms.isMachineInDB(machineId)){
        //    logger.info("ProductionController:Requested Machine "+machineId+" is not registered");
        //    return "redirect:/production";
        //}
        //재고량 체크후 초과일때 되돌리는 코드도 여기 포함하면 됨

        //test code
        logger.info("MachineController:Receive Add Stock :"+data.getAmount()+" to "+data.getMachineId());

        String result;

        try {
            result = machineService.addStockToMachine(data);

            logger.info("MachineController:Stock Successfully Added : "+result);
            model.addAttribute("machineStockAddData", data);
            return "production/machine_stock_add";
        } catch (MachineNotFoundException e) {
            //기계가 없음
            logger.info("Machine Not Found");

            model.addAttribute("machineStockAddData", data);

            return "production/machine_stock_add";
        } catch (ReflectiveOperationException e) {
            //기계에 맞는 재고가 존재하지 않음.

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "production/machine_stock_add"; // 임시 return;
    }
}