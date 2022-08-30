package com.wen.softwarecrm.servcie;


import com.wen.softwarecrm.pojo.Serving;

import java.util.List;

public interface ServingService {
    boolean add(Serving serving);

    boolean delete(Integer id);

    boolean update(Serving serving);

    Serving get(Integer id);

    List<Serving> list();
}
