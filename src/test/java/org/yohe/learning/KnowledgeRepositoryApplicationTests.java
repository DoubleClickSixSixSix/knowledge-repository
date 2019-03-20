package org.yohe.learning;

import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class KnowledgeRepositoryApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeRepositoryApplicationTests.class, args);
    }

}
