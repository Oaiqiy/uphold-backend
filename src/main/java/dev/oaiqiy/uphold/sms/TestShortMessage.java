package dev.oaiqiy.uphold.sms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
@AllArgsConstructor
public class TestShortMessage implements ShortMessageService{
    private final RedisTemplate<String,String> redisTemplate;


    @Override
    public void sendRegisterMessage(String phone) {
        String r = String.valueOf((int)((Math.random()*9+1)*100000));
        String k = phone + ":code";
        redisTemplate.opsForValue().set(k,r);
        redisTemplate.expire(k, Duration.ofMinutes(30));
        log.info(phone+" verification code is : " + r);
    }

}
