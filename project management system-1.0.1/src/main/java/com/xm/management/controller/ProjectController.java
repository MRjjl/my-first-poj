package com.xm.management.controller;

import com.xm.management.model.Project;
import com.xm.management.service.ProjectService;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class ProjectController {
    @Autowired
    ProjectService projectService;
    /*
     * 对数据库mysql的CURD使用mybatis-plus各类控件完成
     * 减少了sql语句的代码
     * 单表操作效率极高
     * */

    // 查询全部-分页
    @RequestMapping(value = "/project/all", method = RequestMethod.GET)
    public List<Project> selectAll() {
        return projectService.selectAll();
    }

    // 条件查询-分页
    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public List<Project> selectBy(@RequestParam(value = "pNumber", required = false) String pnumber,
                                  @RequestParam(value = "pName", required = false) String pname,
                                  @RequestParam(value = "sDate", required = false) String sdate,
                                  @RequestParam(value = "pManager", required = false) String pmanager) {
        return projectService.selectBy(pnumber, pname, sdate,pmanager);
    }

    // 单条更新
    @RequestMapping(value = "/project", method = RequestMethod.PUT)
    public boolean updateByWrapper(Project project) {
        return projectService.updateProject(project);
    }

    // 删除、批量删除-MP service CURD接口实现
    @RequestMapping(value = "/project", method = RequestMethod.DELETE)
    public boolean delete(@RequestParam(value = "pNumber", required = true) List<String> idlist) {
        Boolean ifdelete = projectService.removeByIds(idlist);
        if (ifdelete == true) {
            String msg = ("已删除项目编号为" + idlist + "的项目");
            sendMailMessage(msg);
        }
        return ifdelete;
    }

    //批量增加-MP service CURD接口实现
    @RequestMapping(value = "/project/all", method = RequestMethod.POST)
    public boolean add(List<Project> projectlist) {
        projectlist.forEach(System.out::println);
        Boolean ifinsert = projectService.saveBatch(projectlist);
        return ifinsert;
    }

    //单条增加、修改-MP service CURD接口实现
    @RequestMapping(value = "/project/update", method = RequestMethod.POST)
    public String addOrUpdate(Project project) {
        if (project.getPName() ==""||project.getPName().isEmpty()) {
            return "请输入项目名称";
        }
        else if(project.getPNumber()==""){
            return "请输入项目编号";
        }
        else if(project.getCNumber()==""){
            return "请输入合同编号";
        }
        else if(project.getCName()==""){
            return "请输入合同名称";
        }
        else if(project.getSignatory()==""){
            return "请输入签约方";
        }
        else if(project.getSDate().toString()==""){
            return "请输入签约日期";
        }
        else {
            try {
                Boolean ifinsert = projectService.saveOrUpdate(project);
                if (ifinsert == true)
                    return "修改成功";
            } catch (Exception e) {

                return "修改失败，请按要求填写";
            }
        }
        return null;
    }
    //单条增加 service CURD接口实现
    @RequestMapping(value = "/project", method = RequestMethod.POST)
    public String add(Project project) {
        if (project.getPName() =="") {
            return "请输入项目名称";
        }
        else if(project.getPNumber()==""){
            return "请输入项目编号";
        }
        else if(project.getCNumber()==""){
            return "请输入合同编号";
        }
        else if(project.getCName()==""){
            return "请输入合同名称";
        }
        else if(project.getSignatory()==""){
            return "请输入签约方";
        }
        else if(project.getSDate().toString()==""){
            return "请输入签约日期";
        }
        else {
            try {
                Boolean ifinsert = projectService.save(project);
                if (ifinsert == true)
                    return "新增成功";
            } catch (Exception e) {

                return "新增失败，请按要求填写";
            }
        }
        return null;
    }

    /*
     * 邮箱功能
     * 将删除信息发送给指定邮箱
     * 发件人：jiangliem@163.com
     * 收件人：jiangliem@163.com
     * */
    public static void sendMailMessage(String msg) {
        try {
            //邮箱smtp服务器地址
            String hostName = "smtp.163.com";
            // 帐号与密码
            String userName = "jiangliem@163.com";
            String password = "jljljljl123";
            // 发件人
            String fromAddress = "jiangliem@163.com";
            // 发件人姓名
            String fromName = "mrjj";
            HtmlEmail email = new HtmlEmail();
            email.setHostName(hostName);// 设置smtp服务器
            email.setAuthentication(userName, password);// 设置授权信息
            email.setCharset("utf-8");
            email.setFrom(fromAddress, fromName, "utf-8");// 设置发件人信息
            email.setSubject("项目变动通知");// 设置主题
            email.setHtmlMsg(msg);// 设置邮件内容
            System.out.println("发送的信息为" + msg);
            email.addTo("jiangliem@163.com", "jl", "utf-8");
            // email.addCc("1124324742@qq.com", "jl", "utf-8");
            System.err.println(email.getSmtpPort());
            System.err.println(email.getHostName());
            String res = email.send();// 发送邮件
            System.err.println(res);
        } catch (EmailException e) {
            System.err.println("邮件发送失败");
            e.printStackTrace();
        }
    }


    public static Date string2Date(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = sdf.parse(str);
        return parse;
    }
}