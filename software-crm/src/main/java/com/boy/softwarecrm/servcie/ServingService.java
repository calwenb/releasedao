package com.boy.softwarecrm.servcie;


import com.boy.softwarecrm.pojo.Serving;

import java.util.List;

public interface ServingService {
    int add(Serving serving);

    int delete(Integer id);

    int update(Serving serving);

    Serving get(Integer id);

    List<Serving> list();
}
