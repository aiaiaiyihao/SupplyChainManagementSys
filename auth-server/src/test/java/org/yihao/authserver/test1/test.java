package org.yihao.authserver.test1;


import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SpringBootTest
public class test {
//    @Autowired
//    private RedisTemplate<String, Map<String, String>> redisTemplate1;
//
//    @Autowired
//    private RedisTemplate<String, List<String>> redisTemplate2;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void name() throws JsonProcessingException {
        Map<String, String> user3 = new HashMap<>();
        user3.put("username", "yihao");
        user3.put("password", "123456");

        redisTemplate.opsForValue().set("user",user3);
        Map<String, String> retrievedUser = (Map<String, String>) redisTemplate.opsForValue().get("user");
        System.out.println(retrievedUser.get("username"));
        System.out.println(retrievedUser.get("password"));
        stringRedisTemplate.opsForValue().set("user3",objectMapper.writeValueAsString(user3));
    }

    @Test
    void name2() throws JsonProcessingException {
        List<String> lst = new ArrayList<>();
        lst.add("yihao");
        lst.add("edwin");
        lst.add("edlord");
        redisTemplate.opsForValue().set("lst",lst);
        List<String> lst1 = (List<String>)redisTemplate.opsForValue().get("lst");
        for (String s : lst1) {
            System.out.println(s);
        }

//        stringRedisTemplate.opsForValue().set("user2",objectMapper.writeValueAsString(lst));
//        objectMapper(stringRedisTemplate.opsForValue().get("user2"),new TypeReference<List<String>>() {});
    }
}
