package com.wen.softwarecrm;

import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import com.wen.softwarecrm.pojo.Client;
import com.wen.softwarecrm.pojo.Serving;
import com.wen.softwarecrm.pojo.User;
import com.wen.softwarecrm.servcie.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class ReleasedaoTest {
    @Resource
    BaseMapper baseMapper;
    @Resource
    ClientService clientService;

    @Test
    void pass() {
        log.info("baseMapper.getList \n");
        List<Client> list = clientService.list(null);
        list.forEach(System.out::println);

        Client client = list.get(0);
        Integer id = client.getId();

        log.info("baseMapper.getById \n");
        Client clientModel = clientService.get(id);
        System.out.println(clientModel);

        log.info("baseMapper.add \n");
        Client addModel = new Client();
        addModel.setId(null);
        addModel.setName("test");
        addModel.setPhone("123");
        addModel.setAddress("123");
        addModel.setLevel(0);
        addModel.setCreditLevel(0);
        addModel.setManager("test");
        addModel.setLegalPerson("test");
        addModel.setLoseReason("test");
        clientService.add(addModel);

        log.info("baseMapper.deleteById \n");
        clientService.del(id);
    }

    @Test
    void transactionalTest() {
        clientService.transactional();
    }

    @Test
    void cache() {
        List<Client> list = clientService.list(null);
        list.forEach(System.out::println);

        Client client = list.get(0);
        Integer id = client.getId();

        log.info("baseMapper.getById \n");
        Client clientModel = clientService.get(id);
        System.out.println(clientModel);
    }

    @Test
    void get() {
        List<User> list = baseMapper.getList(User.class);
        list.forEach(System.out::println);
    }

    @Test
    void add() {
        Client addModel = new Client();
        addModel.setId(null);
        addModel.setName("test");
        addModel.setPhone("123");
        addModel.setAddress("123");
        addModel.setLevel(0);
        addModel.setCreditLevel(0);
        addModel.setManager("test");
        addModel.setLegalPerson("test");
        addModel.setLoseReason("test");
        System.out.println(addModel);
        clientService.add(addModel);
        System.out.println(addModel);
    }

    @Test
    void addTime() {
        Serving serving = new Serving();
        serving.setUserName("calwen");
        serving.setTitle("add");
        serving.setContent("add");
        serving.setType("2");
        serving.setCreateName("calwen");
        baseMapper.add(serving);
        System.out.println(serving);
    }

    @Test
    void save() {
        Serving serving = new Serving();
        serving.setId(12);
        serving.setUserName("calwen");
        serving.setTitle("upt");
        serving.setContent("upc");
        long l = System.currentTimeMillis();
        baseMapper.save(serving);
        System.out.println(serving);
        System.out.println(System.currentTimeMillis() - l);
    }

    @Test
    void order() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.order("type,create_time");
        wrapper.orderDesc("update_time");
        List<Serving> list = baseMapper.getList(Serving.class, wrapper);
        list.forEach(System.out::println);
    }

    @Test
    void in() {
//        List<Serving> list = baseMapper.getList(Serving.class);
//        System.out.println(list);
//
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.in("user_name", "calwen", 3);
//        list = baseMapper.getList(Serving.class, wrapper);
//        System.out.println(list);
//

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_name", "calwen");
        List<Serving> list = baseMapper.getList(Serving.class, wrapper);
        System.out.println(list);
    }


}
