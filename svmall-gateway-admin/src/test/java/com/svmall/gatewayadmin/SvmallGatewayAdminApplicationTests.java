package com.svmall.gatewayadmin;

import com.svmall.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SvmallGatewayAdminApplication.class)
class SvmallGatewayAdminApplicationTests {

    @Autowired
    private  RedisUtil redisUtil;
    @Test
    void contextLoads() {

        redisUtil.set("bf","5201314");

    }

}
