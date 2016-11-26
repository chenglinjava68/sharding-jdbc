package com.dangdang.sharding.service;

import com.dangdang.sharding.domain.OrderInfoEntity;

/**
 * Created by licy13 on 2016/11/23.
 */
public interface OrderInfoSingleService {


    /**
     * 单库插入
     *
     * @param orderInfoEntity
     * @return
     */
    public Long insertSingleTsharding(OrderInfoEntity orderInfoEntity);

    /**
     * 单库删除
     *
     * @param orderId
     */
    public void deleteSingleTsharding(Long orderId);
}
