package yonam2023.sfproject.production.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.production.FactoryStub;
import yonam2023.sfproject.production.domain.Production;
import yonam2023.sfproject.production.repository.ProductionRepository;
import yonam2023.sfproject.production.service.FactoryService;

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
    FactoryService fs;

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
            return Production.builder().stype("logNotExist").build();
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
            production.setStype(stype);
            production.setSvalue(svalue);

            pr.save(production);

            return "ID가 "+id.toString()+"인 튜플이 stype : " + stype + " / svalue : " + svalue + "로 수정됨.";
        }
    }

    @PostMapping("/insert")
    public @ResponseBody List<Production> productionInsert(@RequestBody Map<String, String> param){
        String stype = param.get("STYPE");
        System.out.println(param.get("SVALUE"));
        int svalue = Integer.parseInt(param.get("SVALUE"));
        Production production= Production.builder().stype(stype).svalue(svalue).build();
        pr.save(production);

        return pr.findAll();
    }

    @PostMapping("/delete")
    public @ResponseBody List<Production> productionDelete(@RequestBody Map<String, String> param){
        Long id = Long.parseLong(param.get("ID"));
        List<Production> lProduction = new ArrayList<>();

        Optional<Production> oProduction =  pr.findById(id);
        if(oProduction.isEmpty()){
            Production rp = Production.builder().stype("logNotExist").build();
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