package yonam2023.sfproject.production.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yonam2023.sfproject.production.service.FactoryService;
import yonam2023.sfproject.production.sse.SseService;

@Controller
@RequestMapping("/factory")
public class FactoryController {
    //add factory management request method
    private Logger logger = LoggerFactory.getLogger(FactoryController.class);
    @Autowired
    FactoryService fs;

    @Autowired
    SseService se;

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
    public String startupFactoryStub(){
        //run Factory code
        /*
        Thread ProductionStub = new FactoryStub();
        ProductionStub.run();
         */
        logger.info("FactoryController:attempt wake up Factory");
        boolean result=fs.startupFactory();

        if(result){
            se.updateServerState("Connected,In Operation");
            return "true";
        }else {
            return "false";
        }
    }

    @GetMapping("/pause")
    @ResponseBody
    public String pauseFactoryStub(){
        //stop Factory code
        logger.info("FactoryController:attempt pause Factory");
        boolean result = fs.pauseFactory();
        se.updateServerState("Connected,Paused");

        return Boolean.toString(result);
    }

    @GetMapping("/shutdown")
    @ResponseBody
    public String shutdownFactoryStub(){
        //stop Factory code
        logger.info("FactoryController:attempt shut down Factory");
        fs.shutdownFactory();
        se.updateServerState("Disconnected,Stopped");
        return "true";
    }
}
