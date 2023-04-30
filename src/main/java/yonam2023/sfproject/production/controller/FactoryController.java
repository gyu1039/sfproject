package yonam2023.sfproject.production.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yonam2023.sfproject.production.service.FactoryService;

@Controller
@RequestMapping("/factory")
public class FactoryController {
    //add factory management request method
    private Logger logger = LoggerFactory.getLogger(FactoryController.class);
    @Autowired
    FactoryService fs;

    @GetMapping("/check")
    @ResponseBody
    public String checkFactory(){
        //check Factory Connect code
        logger.info("FactoryController:check Factory Connection");
        boolean res = fs.checkFactoryConnection();
        if(res){
            //Connection ok
            logger.info("FactoryController:Factory Connection ok");
            return "true";
        }else{
            //Connection failed
            logger.info("FactoryController:Factory Connection failed");
            return "false";
        }
    }

    @GetMapping("/startup")
    @ResponseBody
    public void startupFactoryStub(){
        //run Factory code
        /*
        Thread ProductionStub = new FactoryStub();
        ProductionStub.run();
         */
        logger.info("FactoryController:attempt wake up Factory");
        fs.startupFactory();
    }

    @GetMapping("/pause")
    @ResponseBody
    public void pauseFactoryStub(){
        //stop Factory code
        logger.info("FactoryController:attempt pause Factory");
        fs.pauseFactory();
    }

    @GetMapping("/shutdown")
    @ResponseBody
    public void shutdownFactoryStub(){
        //stop Factory code
        logger.info("FactoryController:attempt shut down Factory");
        fs.shutdownFactory();
    }
}
