package com.wen.softwarecrm;

import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.softwarecrm.pojo.User;
import com.wen.softwarecrm.servcie.ClientService;
import com.wen.softwarecrm.servcie.UserService;
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
