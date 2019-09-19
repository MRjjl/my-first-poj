package com.xm.management.service;

import com.xm.management.model.SysLog;

import java.util.List;

public interface SysLogService {

    public void save(SysLog sysLog) throws Exception;

    List<SysLog> findAll() throws Exception;
}
