package com.dangdang.sharding.dao.singlemapper;


import com.dangdang.sharding.domain.OrderInfoEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: OrderInfoMapper
 * @Description: 订单信息mapper
 */
public interface OrderInfoSingleMapper {


    public Long insert(@Param("orderInfoEntity") OrderInfoEntity orderInfoEntity);

    public void delete(@Param("orderId") Long orderId);


}
