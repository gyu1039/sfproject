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

    @GetMapping("/wakeStub")
    @ResponseBody
    public void runFactoryStub(){
        //run Factory code
        /*
        Thread ProductionStub = new FactoryStub();
        ProductionStub.run();
         */
        logger.info("MachineController:attempt wake up Factory");
        ms.wakeFactory();
    }
    @GetMapping("/stopStub")
    @ResponseBody
    public void stopFactoryStub(){
        //stop Factory code
        logger.info("MachineController:attempt shut down Factory");
        ms.stopFactory();
    }


    @GetMapping("/chkMachine/{McId}")
    @ResponseBody
    public void checkMachine(@PathVariable("McId") int mid){
        //del Machine code
        logger.info("MachineController:check Machine "+mid+" exists");
        ms.checkMachine(mid);
    }

    @GetMapping("/addMachine/{McId}")
    @ResponseBody
    public void addMachine(@PathVariable("McId") int mid){
        //add Machine code
        logger.info("MachineController:attempt add Machine "+mid);
        ms.addMachine(mid);
    }

    @GetMapping("/delMachine/{McId}")
    @ResponseBody
    public void delMachine(@PathVariable("McId") int mid){
        //del Machine code
        logger.info("MachineController:attempt del Machine "+mid);
        ms.delMachine(mid);
    }

    @GetMapping("/runMachine/{McId}")
    @ResponseBody
    public void runMachine(@PathVariable("McId") int mid){
        //add Machine code
        logger.info("MachineController:attempt run Machine "+mid);
        ms.runMachine(mid);
    }

    @GetMapping("/stopMachine/{McId}")
    @ResponseBody
    public void stopMachine(@PathVariable("McId") int mid){
        //stop code
        logger.info("MachineController:attempt stop Machine "+mid);
        ms.stopMachine(mid);
    }

    @GetMapping("/checkMcState/{McId}")
    public void checkMachineStateGet(@PathVariable("McId") int mid){
        //check Machine State code
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
}
