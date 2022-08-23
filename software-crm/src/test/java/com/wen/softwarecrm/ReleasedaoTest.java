package com.wen.softwarecrm;

import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.softwarecrm.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ReleasedaoTest {
    @Resource
    BaseMapper baseMapper;

    @Test
    void get() {
        System.out.println(baseMapper.getList(User.class));
    }
}
