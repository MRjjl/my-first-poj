package com.xm.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xm.management.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


public interface ProjectService extends IService<Project> {
    @Autowired
    // 查询全部-分页
    List<Project> selectAll();

    // 条件查询-分页
    List<Project> selectBy(String pnumber, String pname, String sdate,String pmanager);

    // 单条更新
    boolean updateProject(Project project);

}
