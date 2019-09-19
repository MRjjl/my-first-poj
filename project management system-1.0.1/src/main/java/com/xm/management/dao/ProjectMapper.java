package com.xm.management.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xm.management.model.Project;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface ProjectMapper extends BaseMapper<Project> {
    /*
    自定义查询 ${ew.customSqlSegment｝  接收条件构造器条件
    */
    @Select("select * from project ${ew.customSqlSegment} ")
    List<Project> selectAll(@Param(Constants.WRAPPER) Wrapper<Project> queryWrapper);

    //自定义分页查询
    IPage<Project> selectProjectPage(Page<Project> page,
                                     @Param(Constants.WRAPPER) Wrapper<Project> queryWrapper1);

}
