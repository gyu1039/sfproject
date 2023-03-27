package yonam2023.sfproject.logistics.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yonam2023.sfproject.logistics.domain.StoredItem;
import yonam2023.sfproject.logistics.repository.StoredItemRepository;

import java.util.List;


@RequestMapping("/storedItems")
@Controller
@RequiredArgsConstructor
@Slf4j
public class StoredItemViewController {
    private final StoredItemRepository storedItemRepo;

    @GetMapping
    public String storedItemsHome(Model model){
        List<StoredItem> storedItems = storedItemRepo.findAll();
        model.addAttribute("storedItems", storedItems);
        return "storedItems";
    }


}
