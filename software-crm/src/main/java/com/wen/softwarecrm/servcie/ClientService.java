package com.wen.softwarecrm.servcie;

import com.wen.softwarecrm.dto.ClientFindDto;
import com.wen.softwarecrm.pojo.Client;

import java.util.List;

public interface ClientService {

    Client get(Integer id);

    List<Client> list(ClientFindDto findDto);

    void add(Client client);

    void update(Integer id, Client client);

    void del(Integer id);
    void transactional();
}
