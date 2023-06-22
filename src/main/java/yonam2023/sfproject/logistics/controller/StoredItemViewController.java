package yonam2023.sfproject.logistics.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yonam2023.sfproject.logistics.domain.StoredItem;
import yonam2023.sfproject.logistics.repository.StoredItemRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/storedItems")
@Controller
@RequiredArgsConstructor
@Slf4j
public class StoredItemViewController {
    private final StoredItemRepository storedItemRepo;

    //더미 데이터 생성.
    @PostConstruct
    public void init(){
        List<StoredItem> storedItems = new ArrayList<>();
        for(int i = 0; i<20; i++){
            storedItems.add(new StoredItem("item "+i, 0));
        }
        storedItems.add(new StoredItem("설탕", 10000));
        storedItems.add(new StoredItem("캔", 10000));
        storedItems.add(new StoredItem("제품", 0));
        storedItemRepo.saveAll(storedItems);
    }

    @GetMapping
    public String storedItemsHome(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<StoredItem> all = storedItemRepo.findAll(pageable);
        model.addAttribute("pageObj", all);

        return "logistics/storedItem/storedItems";
    }


}
