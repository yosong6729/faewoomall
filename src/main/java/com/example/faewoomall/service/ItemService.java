package com.example.faewoomall.service;

import com.example.faewoomall.domain.Category;
import com.example.faewoomall.domain.Item;
import com.example.faewoomall.domain.SaleStatus;
import com.example.faewoomall.domain.UploadFile;
import com.example.faewoomall.dto.AddItemDTO;
import com.example.faewoomall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final FileStoreService fileStoreService;

    public Page<Item> findAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(sorts));

        return itemRepository.findAll(pageRequest);
    }

    public Page<Item> findAllByCategoryIsTop(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(sorts));

        return itemRepository.findByCategory(Category.TOP, pageRequest);
    }

    public Page<Item> findAllByCategoryIsPants(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(sorts));

        return itemRepository.findByCategory(Category.PANTS, pageRequest);
    }

    public Item findById(Long itemId) {
        return itemRepository.findItemById(itemId);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public void addItem(AddItemDTO addItemDTO) throws IOException {
        List<MultipartFile> imageFiles = addItemDTO.getImageFiles();
        List<UploadFile> uploadFiles = fileStoreService.storeFiles(imageFiles);

        Item item = new Item();
        item.setName(addItemDTO.getName());
        item.setPrice(addItemDTO.getPrice());
        item.setCategory(Category.valueOf(addItemDTO.getCategory()));
        item.setContent(addItemDTO.getContent());
        item.setSaleStatus(SaleStatus.valueOf(addItemDTO.getSaleStatus()));
        item.setStockQuantity(addItemDTO.getStockQuantity());
        item.setImageFiles(uploadFiles);

        for (UploadFile uploadFile : uploadFiles) {
            uploadFile.setItem(item);
        }

        itemRepository.save(item);
    }


}
