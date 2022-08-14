package com.boy.softwarecrm.controller;

import com.boy.softwarecrm.pojo.Serving;
import com.boy.softwarecrm.servcie.ServingService;
import com.boy.softwarecrm.utils.ResultUtil;
import com.boy.softwarecrm.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/servings")
public class ServingController {
    @Resource
    ServingService service;


    @GetMapping("/{id}")
    public ResultVO<Serving> get(@PathVariable Integer id) {
        return ResultUtil.success(service.get(id));
    }

    @GetMapping("/list")
    public ResultVO<List<Serving>> list() {
        return ResultUtil.success(service.list());
    }

    @PostMapping
    public ResultVO<Serving> add(@RequestBody Serving serving) {
        service.add(serving);
        return ResultUtil.successDo();

    }

    @PutMapping("/{id}")
    public ResultVO<Serving> update(@PathVariable Integer id, @RequestBody Serving serving) {
        serving.setId(id);
        service.update(serving);
        return ResultUtil.successDo();
    }

    @DeleteMapping("/{id}")
    public ResultVO<Serving> del(@PathVariable Integer id) {
        service.delete(id);
        return ResultUtil.successDo();
    }
}
