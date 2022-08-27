package com.wen.softwarecrm;

import com.wen.softwarecrm.pojo.Client;
import com.wen.softwarecrm.servcie.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author calwen
 * @since 2022/8/24
 */
@SpringBootTest
@Slf4j
public class ServiceTest {
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

}
