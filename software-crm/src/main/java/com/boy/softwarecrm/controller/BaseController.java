package com.boy.softwarecrm.controller;

import com.boy.softwarecrm.servcie.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 同时给Controller注入多个Service
 *
 * @author calwen
 */
@Repository
public abstract class BaseController {
    @Resource
    UserService userService;


}
