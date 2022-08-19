package com.wen.softwarecrm.servcie.impl;

import com.wen.softwarecrm.pojo.Serving;
import com.wen.softwarecrm.servcie.ServingService;
import com.wen.releasedao.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ServingServiceImpl implements ServingService {
    @Resource
    BaseMapper baseMapper;

    @Override
    public int add(Serving serving) {
        serving.setCreateTime(new Date());
        return baseMapper.insertTarget(serving);
    }

    @Override
    public int delete(Integer id) {
        return baseMapper.deleteTargetById(Serving.class, id);
    }

    @Override
    public int update(Serving serving) {

        return baseMapper.replaceTarget(serving);
    }

    @Override
    public Serving get(Integer id) {
        return baseMapper.selectTargetById(Serving.class, id);
    }

    @Override
    public List<Serving> list() {
        return baseMapper.selectList(Serving.class);
    }
}
