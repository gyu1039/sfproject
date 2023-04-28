package yonam2023.sfproject.production.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yonam2023.sfproject.production.FactoryStub;
import yonam2023.sfproject.production.service.HttpPostService;
import yonam2023.sfproject.production.service.MachineService;

@Controller
public class MachineController {

    @Autowired
    MachineService ms;
    @GetMapping("/addMachine/{McId}")
    @ResponseBody
    public void addMachinePost(@PathVariable("McId") int id){
        //add Machine code
        System.out.println("MachineController:check Machine "+id+" exist");
        ms.addMachine(id);
    }
    @GetMapping("/delMachine")
    public void delMachinePost(){
        //del Machine code
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
    public ResponseEntity stopFactoryStub(int id){
        //stop code
        return new ResponseEntity(HttpStatus.OK);
    }
}
