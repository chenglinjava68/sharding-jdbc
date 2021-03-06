/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.sharding.dao.algorithm;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashSet;

public final class SingleKeyModuloDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Long> {
    private static final Logger logger = LoggerFactory.getLogger(SingleKeyModuloTableShardingAlgorithm.class);
    int i=1;
    @Override
    public String doEqualSharding(final Collection<String> availableTargetNames, final ShardingValue<Long> shardingValue) {
        /*for (String each : availableTargetNames) {
            logger.info("availableTargetNames:{} each:{}",availableTargetNames,each);
            if (each.endsWith((shardingValue.getValue() % 512/64 + 1) + "")) {
                return each;
            }
        }*/
        logger.info("i:{} datasource:{},shardingPara：{}",i++,caculateSchemaName(shardingValue.getValue()),shardingValue.getValue() );
        return caculateSchemaName(shardingValue.getValue());
//        throw new UnsupportedOperationException();
    }
    private  String caculateSchemaName(Long shardingPara) {
        if (shardingPara >= 0) {
            return "receive" + getNumberWithZeroSuffix((shardingPara  % 512) / 64 + 1);
        }
        return null;
    }
    /**
     * 4 位数字补 0
     *
     * @param number
     * @return
     */
    private String getNumberWithZeroSuffix(long number) {
        if (number >= 100) {
            return "0" + number;
        } else if (number >= 10) {
            return "00" + number;
        } else if (number >= 0) {
            return "000" + number;
        }
        return null;
    }
    @Override
    public Collection<String> doInSharding(final Collection<String> availableTargetNames, final ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Collection<Long> values = shardingValue.getValues();
        for (Long value : values) {
            for (String dataSourceName : availableTargetNames) {
                if (dataSourceName.endsWith(value % 2 + "")) {
                    result.add(dataSourceName);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(final Collection<String> availableTargetNames, final ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint().intValue(); i <= range.upperEndpoint(); i++) {
            for (String each : availableTargetNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
