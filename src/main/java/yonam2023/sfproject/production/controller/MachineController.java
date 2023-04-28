package yonam2023.sfproject.production.controller;

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

    @GetMapping("/checkFactory")
    public void checkFacotory(){
        //check Factory Connect code
    }

    @GetMapping("/addMachine/{McId}")
    @ResponseBody
    public void addMachinePost(@PathVariable("McId") int McId){
        //add Machine code
        System.out.println("MachineController:check Machine "+McId+" exist");
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
        System.out.println("MachineController:check Machine "+McId+" exist");
        ms.runMachine(McId);
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

    @GetMapping("/stopMachine")
    @ResponseBody
    public void stopFactoryStub(int id){
        //stop code
    }

    @GetMapping("/fatalOccur/{McId}")
    @ResponseBody
    public void fatalStop(@PathVariable("McId")int McId){
        //fatal code
        ms.fatalState(McId);
    }

    @GetMapping("/mctest")
    public String mcTestPage(){
        return "machineTest";
    }
}
