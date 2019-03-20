package org.yohe.learning.test;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yohe.learning.zookeeper.ZkClusterManager;
import javax.annotation.PostConstruct;

/**
 * @author yuhang.zhang
 * @project knowledge-repository
 * @create 2019-03-20 下午3:22
 * @desc
 **/
@Component
public class ZkTest {

    @Autowired
    private ZkClusterManager zkClusterManager;

    @PostConstruct
    public void test() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                CuratorFramework client = zkClusterManager.getZkClient();
                System.out.println("zkClient: " + client);
            }).start();
        }
    }
}
