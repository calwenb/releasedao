package com.wen.softwarecrm.servcie.impl;

import com.wen.softwarecrm.dto.ClientFindDto;
import com.wen.softwarecrm.pojo.Client;
import com.wen.softwarecrm.servcie.ClientService;
import com.wen.releasedao.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Demo
 * 1.注入 BaseMapper。
 * 2.调用API。
 *
 * @author calwen
 * @since 2022/8/19
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    @Resource
    BaseMapper baseMapper;

    @Override
    public Client get(Integer id) {
        return baseMapper.getById(Client.class, id);
    }

    @Override
    public List<Client> list(ClientFindDto findDto) {
        return baseMapper.getList(Client.class);
    }

    @Override
    public void add(Client client) {
        baseMapper.add(client);
    }

    @Override
    public void update(Integer id, Client client) {
        baseMapper.save(client);
    }

    @Override
    public void del(Integer id) {
        baseMapper.deleteById(Client.class, id);
    }

    /**
     * 事务测试
     */
    @Override
    public void transactional() {
        List<Client> list = list(null);
        list.forEach(System.out::println);

        Client client = list.get(0);
        Integer id = client.getId();

        Client clientModel = get(id);
        System.out.println(clientModel);

        Client addModel = new Client();
        addModel.setId(null);
        addModel.setName("test transactional");
        addModel.setPhone("123");
        addModel.setAddress("123");
        addModel.setLevel(0);
        addModel.setCreditLevel(0);
        addModel.setManager("test");
        addModel.setLegalPerson("test");
        addModel.setLoseReason("test");
        add(addModel);

        del(id);
        System.out.println(1 / 0);

    }
}
