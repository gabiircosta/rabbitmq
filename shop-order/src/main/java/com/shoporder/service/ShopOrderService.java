package com.shoporder.service;

import com.shoporder.dto.ShopOrderDto;
import com.shoporder.persistence.model.ShopOrder;
import com.shoporder.persistence.repository.ShopOrderRepository;
import com.shoporder.utils.QueueUtils;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.shoporder.utils.QueueUtils.*;

@Service
public class ShopOrderService {

    @Autowired
    private ShopOrderRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void create(ShopOrderDto orderDto){
        ShopOrder shopOrder = mapper.map(orderDto, ShopOrder.class);
        repository.save(shopOrder);

        rabbitTemplate.convertAndSend(QUEUE_NAME, shopOrder.getId());
    }

    public Page<ShopOrderDto> getAll(Pageable pageable) {
        Page<ShopOrderDto> map = repository.findAll(pageable).map(o -> mapper.map(o, ShopOrderDto.class));
        return map;
    }

    @RabbitListener(queues = QUEUE_NAME)
    private void subscribe(Long id){
        Optional<ShopOrder> shopOrder = repository.findById(id);

        if(shopOrder.isPresent()){
            shopOrder.get().setStatus("DONE");
            repository.save(shopOrder.get());
        }
    }
}
