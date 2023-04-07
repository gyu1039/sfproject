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
import yonam2023.sfproject.logistics.domain.SendRecord;
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
    public String storedItemsHome(Model model, @PageableDefault Pageable pageable){
        Page<StoredItem> all = storedItemRepo.findAll(pageable);
        model.addAttribute("pageObj", all);

        return "logistics/storedItem/storedItems";
    }


}
