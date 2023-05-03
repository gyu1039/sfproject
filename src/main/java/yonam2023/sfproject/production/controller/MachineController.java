package yonam2023.sfproject.production.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.production.service.MachineService;

import java.util.ArrayList;


@Controller
@RequestMapping("/machine")
public class MachineController {

    @Autowired
    MachineService ms;

    private Logger logger = LoggerFactory.getLogger(MachineController.class);

    @GetMapping("/chk/{McId}")
    @ResponseBody
    public String checkMachine(@PathVariable("McId") int mid){
        //check Machine code
        logger.info("MachineController:check Machine "+mid+" exists");
        boolean result = ms.checkMachine(mid);
        return Boolean.toString(result);
    }

    @GetMapping("/add/{McId}")
    public String addMachine(@PathVariable("McId") int mid){
        //add Machine code
        logger.info("MachineController:attempt add Machine "+mid);
        if(ms.addMachine(mid)){
            logger.info("MachineController:Adding Machine "+mid+" is successfully done");
            return "redirect:/production";
        }else{
            logger.info("MachineController:Adding Machine "+mid+" is failed");
            return "production/machineRegistration";
        }

    }

    @GetMapping("/del/{McId}")
    public String delMachine(@PathVariable("McId") int mid){
        //del Machine code
        logger.info("MachineController:attempt del Machine "+mid);
        ms.delMachine(mid);

        return "redirect:/production";
    }

    @GetMapping("/run/{McId}")
    @ResponseBody
    public String runMachine(@PathVariable("McId") int mid){
        //add Machine code
        logger.info("MachineController:attempt run Machine "+mid);
        return Boolean.toString(ms.runMachine(mid));
    }

    @GetMapping("/stop/{McId}")
    @ResponseBody
    public String stopMachine(@PathVariable("McId") int mid){
        //stop code
        logger.info("MachineController:attempt stop Machine "+mid);
        boolean result = ms.stopMachine(mid);
        return Boolean.toString(!result);//가동 상태를 반환. 즉 정지가 성공하면 true를 보내야함.
    }

    @GetMapping("/checkState/{McId}")
    @ResponseBody
    public void checkMachineStateGet(@PathVariable("McId") int mid){
        //check Machine State code
        logger.info("MachineController:check Machine state"+mid);
        ms.checkMachineState(mid);
    }

    @GetMapping("/fatalOccur/{McId}")
    @ResponseBody
    public void fatalStop(@PathVariable("McId")int McId){
        //fatal code
        logger.error("MachineController:Receive Fatal Error Occur On Machine "+McId);
        ms.fatalState(McId);
    }

    @GetMapping("/mctest")
    public String mcTestPage(){
        return "production/machineTest";
    }

    @PostMapping("/insertData")
    @ResponseBody
    public void insertSensorData(@RequestBody String data){
        logger.info("MachineController:Receive data:"+data);
        ms.insertSensorData(data);
    }

    @GetMapping("/getFactoryMidList")
    @ResponseBody
    public String midListGet(){
        logger.info("MachineController:Request Machine ID List");
        ArrayList<Integer> arrayListMids = ms.getFactoryMidList();
        return arrayListMids.toString();
    }
}
