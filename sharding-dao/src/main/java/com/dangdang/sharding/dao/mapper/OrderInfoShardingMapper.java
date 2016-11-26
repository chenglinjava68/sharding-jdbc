package com.dangdang.sharding.dao.mapper;

import com.dangdang.sharding.domain.OrderInfoEntity;
import org.apache.ibatis.annotations.Param;

/**
 *
* @ClassName: OrderInfoMapper
* @Description: 订单信息mapper
 */
public interface OrderInfoShardingMapper {

	/**
	 * 接单方法，保存订单信息
	 * @param orderInfoEntity
	 * @return
	 */
	public Long insert( @Param("orderInfoEntity") OrderInfoEntity orderInfoEntity);

	public void delete( @Param("orderId") Long orderId);


}
