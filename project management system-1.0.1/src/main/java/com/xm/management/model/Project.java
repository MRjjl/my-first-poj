package com.xm.management.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;

//@Entity
@Data
@Table(name = "project")
//active record  需要继承model并且有对应mapper接口
@EqualsAndHashCode(callSuper = false)
public class Project extends Model<Project> {
    //@Id
    //主键
    //项目编号
    // @TableId(type=IdType.ID_WORKER_STR)
    @TableId
    private String pNumber;
    //项目名称
    private String pName;
    //所属事业部
    private String businessunit;
    //    销售
    private String salesmanager;
    //    合同编号
    private String cNumber;
    //    合同名称
    private String cName;
    //    签约方
    private String signatory;
    //    签约日期
    @Temporal(TemporalType.DATE)
    private Date sDate;
    //    金额
    private Integer sMoney;
    private String pManager;
}