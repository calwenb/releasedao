package com.wen.softwarecrm.servcie.impl;

import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.softwarecrm.pojo.Serving;
import com.wen.softwarecrm.servcie.ServingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ServingServiceImpl implements ServingService {
    @Resource
    BaseMapper baseMapper;

    @Override
    public boolean add(Serving serving) {
        serving.setCreateTime(new Date());
        return baseMapper.add(serving);
    }

    @Override
    public boolean delete(Integer id) {
        return baseMapper.deleteById(Serving.class, id);
    }

    @Override
    public boolean update(Serving serving) {
        return baseMapper.save(serving);
    }

    @Override
    public Serving get(Integer id) {
        return baseMapper.getById(Serving.class, id);
    }

    @Override
    public List<Serving> list() {
        return baseMapper.getList(Serving.class);
    }
}
