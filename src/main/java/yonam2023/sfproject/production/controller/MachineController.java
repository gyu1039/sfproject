package yonam2023.sfproject.production.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.production.service.MachineService;
import yonam2023.sfproject.production.sse.SseService;

import java.util.ArrayList;


@Controller
@RequestMapping("/machine")
public class MachineController {

    @Autowired
    MachineService machineService;

    @Autowired
    SseService se;

    private Logger logger = LoggerFactory.getLogger(MachineController.class);

    @GetMapping("/check/{machine_id}")
    @ResponseBody
    public String checkMachine(@PathVariable("machine_id") int machineId){
        //check Machine code
        logger.info("MachineController:check Machine "+machineId+" exists");
        boolean result = machineService.checkMachine(machineId);
        return Boolean.toString(result);
    }

    @GetMapping("/add/{machine_id}")
    public String addMachine(@PathVariable("machine_id") int machineId){
        //add Machine code
        logger.info("MachineController:attempt add Machine "+machineId);
        if(machineService.addMachine(machineId)){
            logger.info("MachineController:Adding Machine "+machineId+" is successfully done");
            return "redirect:/production";
        }else{
            logger.info("MachineController:Adding Machine "+machineId+" is failed");
            return "production/machineRegistration";
        }

    }

    @GetMapping("/del/{machine_id}")
    public String delMachine(@PathVariable("machine_id") int machineId){
        //del Machine code
        logger.info("MachineController:attempt del Machine "+machineId);
        machineService.delMachine(machineId);

        return "redirect:/production";
    }

    @GetMapping("/run/{machine_id}")
    @ResponseBody
    public String runMachine(@PathVariable("machine_id") int machineId){
        //add Machine code
        logger.info("MachineController:attempt run Machine "+machineId);
        return Boolean.toString(machineService.runMachine(machineId));
    }

    @GetMapping("/stop/{machine_id}")
    @ResponseBody
    public String stopMachine(@PathVariable("machine_id") int machineId){
        //stop code
        logger.info("MachineController:attempt stop Machine "+machineId);
        boolean result = machineService.stopMachine(machineId);
        return Boolean.toString(!result);//가동 상태를 반환. 즉 정지가 성공하면 true를 보내야함.
    }

    @GetMapping("/checkState/{machine_id}")
    @ResponseBody
    public void checkMachineStateGet(@PathVariable("machine_id") int mid){
        //check Machine State code
        logger.info("MachineController:check Machine state"+mid);
        machineService.checkMachineState(mid);
    }

    @PostMapping(value = "/halt/{machine_id}", produces = "application/json; charset=utf8")
    @ResponseBody
    public void fatalStop(@PathVariable("machine_id")int machineId, @RequestBody String data){
        //fatal code
        logger.error("MachineController:Receive Fatal Error Occur On Machine "+machineId+" reason : "+data);
        machineService.fatalState(machineId, data);
    }

    @GetMapping("/machine-test")
    public String mcTestPage(){
        return "production/machineTest";
    }

    @PostMapping("/sensor-data-insert")
    @ResponseBody
    public void insertSensorData(@RequestBody String data){
        logger.info("MachineController:Receive data:"+data);
        machineService.insertSensorData(data);
        //SSE 변화된 값만 부분 전송할 것.
    }

    @GetMapping("/factory-machine-id-list")
    @ResponseBody
    public String machineListGet(){
        logger.info("MachineController:Request Machine ID List");
        ArrayList<Integer> arrayListMids = machineService.getFactoryMidList();
        return arrayListMids.toString();
    }
}
