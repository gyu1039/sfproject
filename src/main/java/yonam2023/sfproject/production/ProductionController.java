package yonam2023.sfproject.production;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.production.domain.Production;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/production")
public class ProductionController {

    @Autowired
    ProductionRepository productionRepository;

    @GetMapping
    public String testGet() {

        return "production";
    }

    @GetMapping("/getAll")
    public @ResponseBody List<Production> productionGetAll(){
        return productionRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody Production productionGetAll(@PathVariable long id){
        Optional<Production> oProduction =  productionRepository.findById(id);
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

        Optional<Production> oProduction =  productionRepository.findById(id);
        if(oProduction.isEmpty()){
            return "그런 값은 존재하지 않음.";
        }else{
            Production production = oProduction.get();
            production.setStype(stype);
            production.setSvalue(svalue);

            productionRepository.save(production);

            return "ID가 "+id.toString()+"인 튜플이 stype : " + stype + " / svalue : " + svalue + "로 수정됨.";
        }
    }

    @PostMapping("/insert")
    public @ResponseBody List<Production> productionInsert(@RequestBody Map<String, String> param){
        String stype = param.get("STYPE");
        int svalue = Integer.parseInt(param.get("SVALUE"));
        Production production= Production.builder().stype(stype).svalue(svalue).build();
        productionRepository.save(production);

        return productionRepository.findAll();
    }

    @PostMapping("/delete")
    public @ResponseBody List<Production> productionDelete(@RequestBody Map<String, String> param){
        Long id = Long.parseLong(param.get("ID"));
        List<Production> lProduction = new ArrayList<>();

        Optional<Production> oProduction =  productionRepository.findById(id);
        if(oProduction.isEmpty()){
            Production rp = Production.builder().stype("logNotExist").build();
            lProduction.add(rp);
            return lProduction;
        }else{
            productionRepository.deleteById(id);

            return productionRepository.findAll();
        }
    }

    @GetMapping("/deleteAll")
    public @ResponseBody List<Production> productionDeleteAll(){
        productionRepository.deleteAll();
        return productionRepository.findAll();
    }
}