package com.boy.softwarecrm.controller;

import com.boy.softwarecrm.dto.ClientFindDto;
import com.boy.softwarecrm.pojo.Client;
import com.boy.softwarecrm.servcie.ClientService;
import com.boy.softwarecrm.utils.ResultUtil;
import com.boy.softwarecrm.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Resource
    ClientService service;

    @GetMapping("/{id}")
    public ResultVO<Client> get(@PathVariable Integer id) {
        return ResultUtil.success(service.get(id));
    }

    @GetMapping("/list")
    public ResultVO<List<Client>> list(ClientFindDto findDto) {
        return ResultUtil.success(service.list(findDto));
    }

    @PostMapping
    public ResultVO<String> add(@RequestBody Client client) {
        service.add(client);
        return ResultUtil.successDo();
    }

    @DeleteMapping("/{id}")
    public ResultVO<String> del(@PathVariable Integer id) {
        service.del(id);
        return ResultUtil.successDo();
    }

    @PutMapping("/{id}")
    public ResultVO<String> update(@PathVariable Integer id, @RequestBody Client client) {
        service.update(id, client);
        return ResultUtil.successDo();
    }
}
