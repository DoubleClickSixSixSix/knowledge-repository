package org.yohe.learning.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * @author yuhang.zhang
 * @project knowledge-repository
 * @create 2019-03-19 下午12:05
 * @desc kafka生产者
 **/
@Slf4j
@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(topic, message);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("send msg failed: {]", message, ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                if (log.isDebugEnabled()) {
                    log.debug("send msg success:{}", result.toString());
                }
            }
        });
    }
}
