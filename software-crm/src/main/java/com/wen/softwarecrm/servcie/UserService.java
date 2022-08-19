package com.wen.softwarecrm.servcie;

import com.wen.softwarecrm.dto.UserDto;
import com.wen.softwarecrm.pojo.User;
import com.wen.softwarecrm.vo.ResultVO;

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
