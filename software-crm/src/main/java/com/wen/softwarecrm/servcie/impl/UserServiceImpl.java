package com.wen.softwarecrm.servcie.impl;

import com.wen.softwarecrm.dto.UserDto;
import com.wen.softwarecrm.exception.FailException;
import com.wen.softwarecrm.pojo.User;
import com.wen.softwarecrm.servcie.UserService;
import com.wen.softwarecrm.utils.ResultUtil;
import com.wen.softwarecrm.vo.ResultVO;
import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author calwen
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl implements UserService {
    @Resource
    BaseMapper baseMapper;

    @Override
    public User login(String loginName, String pwd) {
        QueryWrapper wrapper = new QueryWrapper().eq("login_name", loginName).eq("password", pwd);
        return baseMapper.get(User.class, wrapper);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public User get(Integer id) {
        return baseMapper.getById(User.class, id);
    }

    @Override
    public ResultVO<String> register(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setUserType(10);
        user.setRegisterTime(new Date());
        try {
            baseMapper.add(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FailException("账号已存在");
        }
        return ResultUtil.successDo();
    }

    @Override
    public void changePassword(Integer id, String oldpdw, String newpdw) {
        User user = baseMapper.getById(User.class, id);
        if (user == null) {
            throw new FailException("用户不存在");
        }
        if (!Objects.equals(oldpdw, user.getPassword())) {
            throw new FailException("密码错误");
        }
        user.setPassword(newpdw);
        baseMapper.save(user);
    }

    @Override
    public void update(User user) {
        baseMapper.save(user);
    }

}
