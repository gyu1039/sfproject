package yonam2023.sfproject.production.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import yonam2023.sfproject.production.service.MachineService;

@Controller
public class MachineController {

    @Autowired
    MachineService ms;

    private Logger logger = LoggerFactory.getLogger(MachineController.class);

    @GetMapping("/checkFactory")
    @ResponseBody
    public void checkFactory(){
        //check Factory Connect code
        logger.info("MachineController:check Factory Connection");
        boolean res = ms.checkFactory();
        if(res){
            //Connection ok
            logger.info("MachineController:Factory Connection ok");
        }else{
            //Connection failed
            logger.info("MachineController:Factory Connection failed");
        }
    }

    @GetMapping("/addMachine/{McId}")
    @ResponseBody
    public void addMachinePost(@PathVariable("McId") int McId){
        //add Machine code
        logger.info("MachineController:attempt add Machine "+McId);
        ms.addMachine(McId);
    }
    @GetMapping("/delMachine")
    public void delMachinePost(){
        //del Machine code
    }

    @GetMapping("/runMachine/{McId}")
    @ResponseBody
    public void runMachinePost(@PathVariable("McId") int McId){
        //add Machine code
        logger.info("MachineController:attempt run Machine "+McId);
        ms.runMachine(McId);
    }

    @GetMapping("/stopMachine/{McId}")
    @ResponseBody
    public void stopFactoryStub(@PathVariable("McId") int McId){
        //stop code

    }

    @GetMapping("/checkMcState")
    public void checkMachineStateGet(){
        //check Machine Status code
    }

    @GetMapping("/wakeStub")
    @ResponseBody
    public ResponseEntity runFactoryStub(int id){
        //run code
        /*
        Thread ProductionStub = new FactoryStub();
        ProductionStub.run();
         */
        return new ResponseEntity(HttpStatus.OK);
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
        return "machineTest";
    }
}
