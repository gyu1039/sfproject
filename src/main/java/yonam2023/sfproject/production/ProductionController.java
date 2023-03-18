package yonam2023.sfproject.production;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yonam2023.sfproject.production.domain.Production;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/production")
public class ProductionController {

    @Autowired
    ProductionRepository productionRepository;

    @GetMapping
    public String testGet() {

        return "production";
    }

    @PostMapping
    public @ResponseBody List<Production> testPost(@RequestBody Map<String, String> param){
        String stype = param.get("STYPE");
        int svalue = Integer.parseInt(param.get("SVALUE"));
        Production production= Production.builder().stype(stype).svalue(svalue).build();
        productionRepository.save(production);

        return productionRepository.findAll();
    }
}