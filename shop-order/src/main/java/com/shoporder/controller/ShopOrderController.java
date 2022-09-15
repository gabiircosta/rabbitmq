package com.shoporder.controller;

import com.shoporder.dto.ShopOrderDto;
import com.shoporder.service.ShopOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class ShopOrderController {

    @Autowired
    private ShopOrderService service;

    @PostMapping
    public ResponseEntity create (@RequestBody ShopOrderDto orderDto){
        service.create(orderDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ShopOrderDto>> getAll (Pageable pageable){
        Page<ShopOrderDto> orderDtos = service.getAll(pageable);
        return ResponseEntity.ok(orderDtos);
    }
}
