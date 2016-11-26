package com.dangdang.sharding.service.impl;

import com.dangdang.sharding.dao.singlemapper.OrderInfoSingleMapper;
import com.dangdang.sharding.domain.OrderInfoEntity;
import com.dangdang.sharding.service.OrderInfoSingleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by licy13 on 2016/11/23.
 */
@Service
public class OrderInfoSingleServiceImpl implements OrderInfoSingleService {
    @Autowired
    private OrderInfoSingleMapper orderInfoSingleMapper;


    @Override
    public Long insertSingleTsharding(OrderInfoEntity orderInfoEntity) {
        return orderInfoSingleMapper.insert(orderInfoEntity);
    }

    @Override
    public void deleteSingleTsharding(Long orderId) {
        orderInfoSingleMapper.delete(orderId);
    }

}
