package org.yohe.learning.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

/**
 * @author yuhang.zhang
 * @project knowledge-repository
 * @create 2019-03-19 下午12:05
 * @desc kafka消费者
 **/
@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"${}"}, containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(ConsumerRecord record) {
        log.info("offset: {},partition: {}, timestamp: {}", record.offset(), record.partition(), record.timestamp());
        Optional<Object> value = Optional.ofNullable(record.value());
        if (value.isPresent()) {
            String message = (String) value.get();
            if (log.isDebugEnabled()) {
                log.debug("consume message:{}", message);
            }
            //处理kafka的消息
        }
        log.info("消息消费总耗时：{}ms", System.currentTimeMillis() - record.timestamp());
    }

    @KafkaListener(topics = {"${spring.kafka.topic}"}, containerFactory = "batchFactory", errorHandler = "batchErrorHandler")
    public void receiveMessage(List<ConsumerRecord> records) {
        log.info("消费消息数：" + records.size());
        records.parallelStream().forEach(r -> {
            log.info("offset: {},partition: {}, timestamp: {}", r.offset(), r.partition(), r.timestamp());
            Optional<Object> value = Optional.ofNullable(r.value());
            if (value.isPresent()) {
                String message = (String) value.get();
                //处理kafka的消息
            }
            log.info("消息消费总耗时：{}ms", System.currentTimeMillis() - r.timestamp());
        });
    }

    /**
     * kafka批量消费异常处理
     * @return
     */
    @Bean
    public ConsumerAwareListenerErrorHandler batchErrorHandler() {
        return (message, e, consumer) -> {
            log.info("batchErrorHandler receive: " + message.getPayload().toString());
            return null;
        };
    }
}
