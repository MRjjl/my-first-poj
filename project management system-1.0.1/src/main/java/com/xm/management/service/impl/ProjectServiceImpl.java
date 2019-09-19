package com.xm.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xm.management.dao.ProjectMapper;
import com.xm.management.model.Project;
import com.xm.management.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    /*
     * 使用mybatis-plus中ActiveRecord方法实现
     * */

    // ProjectMapper projectMapper;
    // 查询全部-分页
    public List<Project> selectAll() {
        Project project = new Project();
        QueryWrapper<Project> queryWrapper1 = Wrappers.<Project>query();
        Page<Project> page = new Page<Project>(1, 1000);
        IPage<Project> iPage = project.selectPage(page, queryWrapper1);
        System.out.println("总页数" + iPage.getPages());
        System.out.println("总记录数" + iPage.getTotal());
        List<Project> projectList = iPage.getRecords();
        return projectList;
    }

    // 条件查询-分页
    public List<Project> selectBy(String pnumber, String pname, String sdate,String pmanager) {
        Project project = new Project();
        QueryWrapper<Project> queryWrapper1 = Wrappers.<Project>query();
        queryWrapper1.like(StringUtils.isNotEmpty(pname), "p_name", pname).
                like(StringUtils.isNotEmpty(pnumber), "p_number", pnumber).
                like(StringUtils.isNotEmpty(sdate), "s_date", sdate).
                like(StringUtils.isNotEmpty(pmanager), "p_manager", sdate);
        Page<Project> page = new Page<Project>(1, 1000);
        IPage<Project> iPage = project.selectPage(page, queryWrapper1);
        System.out.println("总页数" + iPage.getPages());
        System.out.println("总记录数" + iPage.getTotal());
        List<Project> projectList = iPage.getRecords();
        return projectList;
    }

    // 单条更新
    public boolean updateProject(Project project) {
        UpdateWrapper<Project> updateWrapper = new UpdateWrapper<Project>();
        updateWrapper.eq("p_number", project.getPNumber());
        Boolean ifupdate = project.update(updateWrapper);
        return ifupdate;
    }


}