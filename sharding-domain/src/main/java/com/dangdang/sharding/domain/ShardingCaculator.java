package com.dangdang.sharding.domain;


import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分片计算器
 *
 * @auther qigong on 5/28/15 1:06 PM.
 */
public class ShardingCaculator {

    private static final Logger logger = LoggerFactory.getLogger(ShardingCaculator.class);

    /**
     * 根据分片参数值计算分表名
     *
     * @param shardingPara
     * @return 分表名0xxx
     */
    public static String caculateTableName(Long shardingPara) {
        if (shardingPara >= 0) {
//            return "TradeOrder" + getNumberWithZeroSuffix((shardingPara % 10000) % 512);
            return "orderinfo" + getNumberWithZeroSuffix((shardingPara % 10000) % 512);

        }
        return null;
    }

    /**
     * 根据分片参数值计算分表名
     *
     * @param shardingPara
     * @return 分表名0xxx
     */
    public static Integer caculateTableIndex(Long shardingPara) {
        if (shardingPara >= 0) {
            return new Long(shardingPara % 10000 % 64).intValue();
        }
        return null;
    }


    /**
     * 根据分片参数值计算分库名（逻辑库）从1开始
     *
     * @param shardingPara
     * @return 分库名000x
     */
    public static String caculateSchemaName(String fieldName, Long shardingPara) {
        if (shardingPara >= 0) {

            if ("sellerUserId".equals(fieldName)) {
                return "sellertrade" + getNumberWithZeroSuffix(((shardingPara % 10000) % 512) / 64);
            } else {
//                return "trade" + getNumberWithZeroSuffix(((shardingPara % 10000) % 512) / 64);
                return "receive" + getNumberWithZeroSuffix(((shardingPara % 10000) % 512) / 64 + 1);

            }
        }
        return null;
    }

    /**
     * 根据分片参数值计算分库名（逻辑库）从1开始
     *
     * @param shardingPara
     * @return 分库名000x
     */
    public static Integer caculateSchemaIndex(Long shardingPara) {
        if (shardingPara >= 0) {
            return new Long((shardingPara % 10000 % 512) / 64 + 1).intValue();
        }
        return null;
    }

    /**
     * 根据分片参数值计算数据源名
     *
     * @param shardingPara
     * @return DatasourceName 见数据源配置文件
     */
    public static String caculateDatasourceName(String fieldName, Long shardingPara) {
        if (shardingPara >= 0) {
            if ("sellerUserId".equals(fieldName)) {
                return "seller_ds_" + ((shardingPara % 10000) % 512) / 256;
            } else {
                return "buyer_ds_" + ((shardingPara % 10000) % 512) / 256;
            }
        }
        return null;
    }

    /**
     * 4 位数字补 0
     *
     * @param number
     * @return
     */
    public static String getNumberWithZeroSuffix(long number) {
        if (number >= 100) {
            return "0" + number;
        } else if (number >= 10) {
            return "00" + number;
        } else if (number >= 0) {
            return "000" + number;
        }
        return null;
    }

    /**
     * 按订单号批量查询：跨表查，先按分表做分组
     *
     * @param listShopOrderIds
     * @return tableNo -> orderIds
     */
    public static Map<Integer, List<Long>> getTableNoAndOrderIdsMap(List<Long> listShopOrderIds) {

        HashMap<Integer, List<Long>> shopOrderIdsMap = new HashMap();
        if (listShopOrderIds == null || listShopOrderIds.size() == 0) {
            return shopOrderIdsMap;
        }
        for (Long shopOrderId : listShopOrderIds) {
            Integer tableNo = ShardingCaculator.caculateTableIndex(shopOrderId);
            List<Long> orderIds = shopOrderIdsMap.get(tableNo);
            if (orderIds == null) {
                orderIds = new ArrayList<>();
            }
            orderIds.add(shopOrderId);
            shopOrderIdsMap.put(tableNo, orderIds);
        }
        return shopOrderIdsMap;
    }


    /**
     * 分库分表信息（前 5 位）补 0
     *
     * @param number
     * @return
     */
    public static String getNumberWithZeroPrefix(long number) {
        if (number >= 1000) {
            return number + "0";
        } else if (number >= 100) {
            return number + "00";
        } else if (number >= 10) {
            return number + "000";
        } else if (number >= 0) {
            return number + "0000";
        }
        return null;
    }

    private final static Object lock = new Object();

    //当前最新的订单号信息
    private static ConcurrentHashMap<String, Long> currentOrderInfo = new ConcurrentHashMap<>();

    static {
        currentOrderInfo.put("currentTime", 0L);
        currentOrderInfo.put("increment", 0L);
    }

    /**
     * 生成订单号
     *
     * @return
     */
    public static long generateOrderNo() {
        long orderIdNo = 0L;
        long start = System.currentTimeMillis();
        synchronized (lock) {
            long currentTimeMap = currentOrderInfo.get("currentTime");
            long currentTime = System.currentTimeMillis();
            if (currentTimeMap == currentTime ) {
                currentOrderInfo.put("increment", currentOrderInfo.get("increment") + 1);
            } else {
                currentOrderInfo.put("increment", 0L);
                currentOrderInfo.put("currentTime", currentTime);
            }
            long increment = currentOrderInfo.get("increment");
            orderIdNo = mergeIncrement(increment, currentTime);
            logger.info("orderIdNo:{}-increment:{}", orderIdNo, increment);
        }
        return orderIdNo;
    }

    private static long mergeIncrement(long increment, long currentTime) {
        if (increment < 10) {
            return currentTime * 10 + increment;
        } else if (increment < 100) {
            return currentTime * 100 + increment;
        } else if (increment < 1000) {
            return currentTime * 1000 + increment;
        } else {
            return currentTime * 10000 + increment;
        }
    }

    public static void main(String args[]) {

      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                for (int s = 0; s < 100000; s++) {
                    System.out.println(generateOrderNo());
                }
            }
        }).start();*/
      for (int i=1;i<9;i++){
          for (int s = (i-1)*64; s < 64*i; s++) {
              String ss="DROP TABLE IF EXISTS `db_"+i+"`.`orderinfo"+s+"`;CREATE TABLE `db_"+i+"`.`orderinfo"+s+"` (  `orderId` bigint(20) NOT NULL,  `orderInfo` text,  `status` int(11) DEFAULT NULL,  `createTime` datetime DEFAULT NULL,  `cartInfo` text,  `readTime` datetime DEFAULT NULL,  `readTimes` int(11) DEFAULT '0',  `memberId` varchar(50) DEFAULT NULL,  `platId` varchar(50) DEFAULT NULL,  `lenovoId` bigint(20) DEFAULT NULL,  `merchantId` varchar(50) DEFAULT NULL,  `sourceId` varchar(50) DEFAULT NULL,  `memberCode` varchar(50) DEFAULT NULL,`UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,PRIMARY KEY (`orderId`),KEY `idx_read_status` (`readTimes`,`status`) USING BTREE,KEY `index_status` (`status`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
              System.out.println(ss);
          }
      }


    }
}
