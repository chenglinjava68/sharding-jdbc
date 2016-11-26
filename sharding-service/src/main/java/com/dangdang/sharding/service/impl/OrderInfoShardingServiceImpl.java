package com.dangdang.sharding.service.impl;

import com.dangdang.sharding.dao.mapper.OrderInfoShardingMapper;
import com.dangdang.sharding.dao.singlemapper.OrderInfoSingleMapper;
import com.dangdang.sharding.domain.OrderInfoEntity;
import com.dangdang.sharding.service.OrderInfoShardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by licy13 on 2016/11/23.
 */
@Service
@Transactional
public class OrderInfoShardingServiceImpl implements OrderInfoShardingService{
    @Autowired
    private OrderInfoShardingMapper orderInfoShardingMapper;

    @Override
    public Long insert(OrderInfoEntity orderInfoEntity) {
        return orderInfoShardingMapper.insert(orderInfoEntity);
    }

    @Override
    public void delete(Long orderId) {
        orderInfoShardingMapper.delete(orderId);
    }


}
