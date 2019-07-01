package org.yohe.learning.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yohe.learning.config.ZkConfig;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author yuhang.zhang
 * @project knowledge-repository
 * @create 2019-03-20 上午11:42
 * @desc zk集群初始化
 **/
@Component
public class ZkClusterManager {

    private static Logger logger = LoggerFactory.getLogger(ZkClusterManager.class);

    @Resource
    private ZkConfig zkConfig;

    private CuratorFramework client;

    private TreeCache cache;

    @PostConstruct
    public void init() {
        logger.info("init ZKConf：{}", zkConfig.toString());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkConfig.getServerList())
                .retryPolicy(new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMilliseconds(), zkConfig.getMaxRetries(), zkConfig.getMaxSleepTimeMilliseconds()))
                .namespace(zkConfig.getNamespace());
        if (0 != zkConfig.getSessionTimeoutMilliseconds()) {
            builder.sessionTimeoutMs(zkConfig.getSessionTimeoutMilliseconds());
        }
        if (0 != zkConfig.getConnectionTimeoutMilliseconds()) {
            builder.connectionTimeoutMs(zkConfig.getConnectionTimeoutMilliseconds());
        }
        try {
            client = builder.build();
            client.start();
            client.blockUntilConnected();
            logger.info("init ZkNodes");
            initZkNodes();
            logger.info("start treeCache");
            initTreeCache("/");
            logger.info("init ZkClusterManager completed");

            client.getConnectionStateListenable().addListener((client1, newState) -> {
                if (newState == ConnectionState.LOST) {
                    logger.warn("zk connection lost");
                } else if (newState == ConnectionState.CONNECTED) {
                    logger.info("zk connection connected");
                } else if (newState == ConnectionState.RECONNECTED){
                    logger.info("zk connection reconnected");
                }
            });
        } catch (Exception e) {
            logger.error("init ZkClusterManager failed", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("jvm shutdown close ZkClusterManager");
            close();
        }));
    }

    public CuratorFramework getZkClient() {
        return client;
    }

    public void close() {
        logger.info("closing zkClient");
        String zkPath = "";
        try {
            //zk自动移除机制有延迟 手动关闭
            remove(zkPath);
            logger.info("remove CLIENT_IP_PATH:{} success", zkPath);
        } catch (Exception e) {
            logger.error("remove CLIENT_IP_PATH:{} error", zkPath, e);
        }
        if (null != cache) {
            cache.close();
        }
        CloseableUtils.closeQuietly(client);
    }

    private void initZkNodes() {
        createNode(CreateMode.PERSISTENT, "/k");
    }

    private void initTreeCache(String watchRootPath) throws Exception {
        cache = new TreeCache(client, watchRootPath);
        cache.start();
    }

    public void createNode(CreateMode mode, String path , String nodeData) {
        try {
            client.create().creatingParentsIfNeeded().withMode(mode).forPath(path,nodeData.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("创建节点出错,path:{},nodeData:{}", path, nodeData, e);
        }
    }

    public void createNode(CreateMode mode, String path) {
        createNode(mode, path, "");
    }

    public void remove(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            logger.error("删除zk路径失败,path:{}", path, e);
        }
    }

}
