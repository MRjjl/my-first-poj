package com.xm.management.controller;

import com.xm.management.model.SysLog;
import com.xm.management.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * Aop日志显示部分
 * */
@RestController
@RequestMapping("/sysLog")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<SysLog> findAll() throws Exception {
        List<SysLog> sysLogList = sysLogService.findAll();
        return sysLogList;
    }
}
