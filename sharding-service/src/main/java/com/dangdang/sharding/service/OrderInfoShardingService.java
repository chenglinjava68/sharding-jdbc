package com.dangdang.sharding.service;

import com.dangdang.sharding.domain.OrderInfoEntity;

/**
 * Created by licy13 on 2016/11/23.
 */
public interface OrderInfoShardingService {
    /**
     * 分库分表插入
     *
     * @param orderInfoEntity
     * @return
     */
    public Long insert(OrderInfoEntity orderInfoEntity);

    /**
     * 分库分表删除
     *
     * @param orderId
     */
    public void delete(Long orderId);

}
