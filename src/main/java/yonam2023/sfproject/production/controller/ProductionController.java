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
import yonam2023.sfproject.production.domain.MachineData;
import yonam2023.sfproject.production.domain.MachineRegistData;
import yonam2023.sfproject.production.domain.Production;
import yonam2023.sfproject.production.repository.MachineDataRepository;
import yonam2023.sfproject.production.repository.ProductionRepository;
import yonam2023.sfproject.production.service.FactoryService;
import yonam2023.sfproject.production.service.MachineService;

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

        model.addAttribute("list", all);
        return "production/machineOverview";
    }

    @GetMapping("/factoryManagement")
    public String factoryManagementPage(){
        logger.info("ProductionController:factoryManagementPage called");
        return "production/factoryManagement";
    }
    @GetMapping("/machineRegistration")
    public String machineRegistrationPage(Model model){
        logger.info("ProductionController:machineRegistration called");
        MachineRegistData machineRegistData = MachineRegistData.builder().mid(0).build();
        model.addAttribute("machineRegistData", machineRegistData);

        return "production/machineRegistration";
    }

    @PostMapping("/machineRegistration")
    public String addMachinePost(@ModelAttribute(name = "machineRegistData") MachineRegistData machineRegistData, Model model){
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
            return "production/machineRegistration";
        }
    }

    @GetMapping("/machineDetail/{McId}")
    public String machineDetailGet(@PathVariable("McId") int mid, Model model){
        logger.info("ProductionController:Request Machine "+mid+" detail");
        if(!ms.isMachineInDB(mid)){
            logger.info("ProductionController:Requested Machine "+mid+" is not registered");
            return "redirect:/production";
        }
        MachineData machineData = mr.findByMid(mid);
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


}