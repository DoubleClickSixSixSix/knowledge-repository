package org.yohe.learning.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yuhang.zhang
 * @project knowledge-repository
 * @create 2019-03-20 上午11:42
 * @desc zk集群初始化
 **/
@Component
@Data
@NoArgsConstructor
public class ZkConfig {

    /**
     * 连接Zookeeper服务器的列表.
     * 包括IP地址和端口号.
     * 多个地址用逗号分隔.
     * 如: host1:2181,host2:2181
     */
    @Value("${zookeeper.serverList:10.32.1.7:2181}")
    private String serverList;

    /**
     * 命名空间.
     * 默认是：yohe
     */
    @Value("${zookeeper.namespace:yohe}")
    private String namespace;

    /**
     * 等待重试的间隔时间的初始值.
     * 单位毫秒.
     */
    @Value("${zookeeper.baseSleepTimeMilliseconds:1000}")
    private int baseSleepTimeMilliseconds;

    /**
     * 等待重试的间隔时间的最大值.
     * 单位毫秒.
     */
    @Value("${zookeeper.maxSleepTimeMilliseconds:3000}")
    private int maxSleepTimeMilliseconds;

    /**
     * 最大重试次数.
     */
    @Value("${zookeeper.maxRetries:3}")
    private int maxRetries;

    /**
     * 会话超时时间.
     * 单位毫秒.
     */
    @Value("${zookeeper.sessionTimeoutMilliseconds:15000}")
    private int sessionTimeoutMilliseconds;

    /**
     * 连接超时时间.
     * 单位毫秒.
     */
    @Value("${zookeeper.connectionTimeoutMilliseconds:3000}")
    private int connectionTimeoutMilliseconds;

    /**
     * 包含了必需属性的构造器.
     *
     * @param serverList                连接Zookeeper服务器的列表
     * @param namespace                 命名空间
     * @param baseSleepTimeMilliseconds 等待重试的间隔时间的初始值
     * @param maxSleepTimeMilliseconds  等待重试的间隔时间的最大值
     * @param maxRetries                最大重试次数
     */
    public ZkConfig(final String serverList, final String namespace, final int baseSleepTimeMilliseconds, final int maxSleepTimeMilliseconds, final int maxRetries) {
        this.serverList = serverList;
        this.namespace = namespace;
        this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
        this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
        this.maxRetries = maxRetries;
    }

}
