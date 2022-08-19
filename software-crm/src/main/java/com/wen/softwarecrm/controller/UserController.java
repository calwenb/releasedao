package com.wen.softwarecrm.controller;


import com.wen.softwarecrm.dto.UserDto;
import com.wen.softwarecrm.dto.UserPwdDto;
import com.wen.softwarecrm.pojo.User;
import com.wen.softwarecrm.servcie.UserService;
import com.wen.softwarecrm.utils.ResultUtil;
import com.wen.softwarecrm.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * UserController类
 */
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {
    @Resource
    UserService userService;

    @PostMapping("/login")
    public ResultVO<Integer> login(@RequestBody UserDto userDto) {
        User user = userService.login(userDto.getLoginName(), userDto.getPassword());
        if (user == null) {
            return ResultUtil.error("账号密码错误");
        }
        return ResultUtil.success(user.getId());

    }

    @PostMapping("/register")
    public ResultVO<String> register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    @GetMapping("/info/{id}")
    public ResultVO<User> info(@PathVariable Integer id) {
        User user = userService.get(id);
        if (user == null) {
            return ResultUtil.error("用户不存在");
        }
        user.setPassword("");
        return ResultUtil.success(user);
    }

    @PutMapping("/{id}/pdw")
    public ResultVO<String> changePassword(@PathVariable Integer id,
                                           @RequestBody UserPwdDto dto) {
        userService.changePassword(id, dto.getOldPwd(), dto.getNewPwd());
        return ResultUtil.successDo();
    }

    @PutMapping("/{id}")
    public ResultVO<String> update(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return ResultUtil.successDo();
    }
}
