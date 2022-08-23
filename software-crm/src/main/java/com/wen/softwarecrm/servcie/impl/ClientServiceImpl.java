package com.wen.softwarecrm.servcie.impl;

import com.wen.softwarecrm.dto.ClientFindDto;
import com.wen.softwarecrm.pojo.Client;
import com.wen.softwarecrm.servcie.ClientService;
import com.wen.releasedao.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
/**
 * Demo
 * 1.注入 BaseMapper。
 * 2.调用API。
 * @author calwen
 * @since 2022/8/19
 */
@Service
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
}
