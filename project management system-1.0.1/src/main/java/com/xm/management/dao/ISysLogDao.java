package com.xm.management.dao;

import com.xm.management.model.SysLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ISysLogDao {

    @Insert("insert into sysLog(visitTime,username,ip,executionTime,method) values(#{visitTime},#{username},#{ip},#{executionTime},#{method})")
    public void save(SysLog sysLog) throws Exception;

    @Select("select * from sysLog")
    List<SysLog> findAll() throws Exception;
}
