package org.yohe.learning.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

/**
 * @author yuhang.zhang
 * @project knowledge-repository
 * @create 2019-03-19 上午11:25
 * @desc hbase配置
 **/
@Configuration
public class HBaseConfig {

    /*@Bean
    public HbaseTemplate hbaseTemplate(@Value("${hbase.zookeeper.quorum}") String quorum,
                                       @Value("${hbase.zookeeper.port}") String port) {
        HbaseTemplate hbaseTemplate = new HbaseTemplate();
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", quorum);
        conf.set("hbase.zookeeper.port", port);
        hbaseTemplate.setConfiguration(conf);
        hbaseTemplate.setAutoFlush(true);
        return hbaseTemplate;
    }*/
}
