package com.wen.softwarecrm;

import com.wen.softwarecrm.pojo.Client;
import com.wen.softwarecrm.servcie.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author calwen
 * @since 2022/8/24
 */
@SpringBootTest
public class ServiceTest {
    @Resource
    ClientService clientService;

    @Test
    void t1() {

        List<Client> list = clientService.list(null);
        list.forEach(System.out::println);

        Client client = list.get(0);
        Integer id = client.getId();
        Client clientModel = clientService.get(id);
        System.out.println(client);
        clientService.del(id);


    }
}
