package com.boy.softwarecrm.servcie;

import com.boy.softwarecrm.dto.UserDto;
import com.boy.softwarecrm.pojo.User;
import com.boy.softwarecrm.vo.ResultVO;

/**
 * @author calwen
 */
public interface UserService {


    User login(String loginName, String pwd);

    User get(Integer id);


    ResultVO<String> register(UserDto user);

    void changePassword(Integer id, String oldpdw, String newpdw);

    void update(User user);


}
