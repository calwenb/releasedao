package com.wen.softwarecrm;

import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.softwarecrm.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class ReleasedaoTest {
    @Resource
    BaseMapper baseMapper;


    @Test
    void get() {
        List<User> list = baseMapper.getList(User.class);
        list.forEach(System.out::println);
    }
}
