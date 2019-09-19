package com.xm.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xm.management.dao.ProjectMapper;
import com.xm.management.model.Project;
import com.xm.management.service.ProjectFileService;
import com.xm.management.service.ProjectService;
import com.xm.management.utils.ExcelImportUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ProjectFileServiceImpl implements ProjectFileService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    ProjectServiceImpl projectServiceImpl;

    /*
     * 接收Excel文件
     *
     **/
    public String batchImport(String fileName, MultipartFile mfile, String userName) {
        File uploadDir = new File("f:\\test\\");
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File("f:\\test\\" + new Date().getTime() + ".xlsx");
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            mfile.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if (ExcelImportUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            //根据excel里面的内容读取知识库信息
            return readExcelValue(wb, userName, tempFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return "导入出错！请检查数据！";
    }

    /*
     * 解析Excel里面的数据
     *
     **/
    private String readExcelValue(Workbook wb, String userName, File tempFile) {

        //错误信息接收器
        String errorMsg = "";
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if (totalRows >= 2 && sheet.getRow(1) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        List<Project> projectsList = new ArrayList<Project>();
        Project project;

        String br = "<br/>";

        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            String rowMessage = "";
            Row row = sheet.getRow(r);
            if (row == null) {
                errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
                continue;
            }
            project = new Project();

            String pnumber = "";
            String pname = "";
            String businessunit = "";
            String salesmanager = "";
            String cnumber = "";
            String cname = "";
            String signatory = "";
            java.sql.Date sdate;
            String smoney;
            String pmanager;

            //循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {

                    if (c == 0) {
                        pnumber = cell.getStringCellValue();
                        if (StringUtils.isEmpty(pnumber)) {
                            rowMessage += "项目编号不能为空；";
                        }
                        project.setPNumber(pnumber);
                    } else if (c == 1) {
                        pname = cell.getStringCellValue();
                        if (StringUtils.isEmpty(pname)) {
                            rowMessage += "项目名称不能为空；";
                        }
                        project.setPName(pname);
                    } else if (c == 2) {
                        businessunit = cell.getStringCellValue();
                        project.setBusinessunit(businessunit);
                    } else if (c == 3) {
                        salesmanager = cell.getStringCellValue();
                        project.setSalesmanager(salesmanager);
                    } else if (c == 4) {
                        cnumber = cell.getStringCellValue();
                        if (StringUtils.isEmpty(cnumber)) {
                            rowMessage += "合同编号不能为空；";
                        }
                        project.setCNumber(cnumber);
                    } else if (c == 5) {
                        cname = cell.getStringCellValue();
                        if (StringUtils.isEmpty(cname)) {
                            rowMessage += "合同名称不能为空；";
                        }
                        project.setCName(cname);
                    } else if (c == 6) {
                        signatory = cell.getStringCellValue();
                        if (StringUtils.isEmpty(signatory)) {
                            rowMessage += "签约方不能为空；";
                        }
                        project.setSignatory(signatory);
                    } else if (c == 7) {
                        //时间类型转换
                        Date temp = cell.getDateCellValue();
                        sdate = new java.sql.Date(temp.getTime());
                        if (sdate == null) {
                            rowMessage += "签约日期不能为空；";
                        }
                        project.setSDate(sdate);
                    } else if (c == 8) {

                        smoney = cell.getStringCellValue();
                        Integer smoneyint = Integer.valueOf(smoney);
                        project.setSMoney(smoneyint);
                    }
                    else if (c == 9) {
                        pmanager = cell.getStringCellValue();
                        project.setPManager(pmanager);
                    }

                } else {
                    rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
                }
            }
            //拼接每行的错误提示
            if (!StringUtils.isEmpty(rowMessage)) {
                errorMsg += br + "第" + (r) + "行，" + rowMessage;
            } else {
                projectsList.add(project);
            }
        }

        //删除上传的临时文件
        // if (tempFile.exists()) {
            tempFile.delete();
        // }
        //全部验证通过才导入到数据库
        if (StringUtils.isEmpty(errorMsg)) {
            for (Project projects : projectsList) {
                projectMapper.insert(projects);
            }
            errorMsg = "导入成功，共" + projectsList.size() + "条数据！";
        }
        return errorMsg;
    }

    /*
     * 导出project的数据成excel
     *
     **/
    public void batchExport(String[] titles, ServletOutputStream out) throws Exception {
        try {
            // 第一步，创建一个workbook，对应一个Excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();

            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet hssfSheet = workbook.createSheet("project");

            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = hssfSheet.createRow(0);
            // 第四步，创建单元格，并设置值表头
            HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
            //居中样式
            // hssfCellStyle.setAlignment(hssfCellStyle.);

            HSSFCell hssfCell = null;
            for (int i = 0; i < titles.length; i++) {
                hssfCell = row.createCell(i);//列索引从0开始
                hssfCell.setCellValue(titles[i]);//列名1
                hssfCell.setCellStyle(hssfCellStyle);//列居中显示
            }
            List<Project> plist=projectServiceImpl.selectAll();

            for (int i = 0; i < plist.size(); i++) {
                row = hssfSheet.createRow(i + 1);
                Project project = plist.get(i);

                // 第六步，创建单元格，并设置值
                String pnumber = null;
                if (project.getPNumber() != null) {
                    pnumber = project.getPNumber();
                }
                row.createCell(0).setCellValue(pnumber);
                String pname = null;
                if (project.getPName() != null) {
                    pname = project.getPName();
                }
                row.createCell(1).setCellValue(pname);
                String businessunit =null;
                if (project.getBusinessunit() != null) {
                    businessunit = project.getBusinessunit();
                }
                row.createCell(2).setCellValue(businessunit);
                String salesmanager = null;
                if (project.getSalesmanager() != null) {
                    salesmanager = project.getSalesmanager();
                }
                row.createCell(3).setCellValue(salesmanager);
                String cnumber = null;
                if (project.getCNumber() != null) {
                    cnumber = project.getCNumber();
                }
                row.createCell(4).setCellValue(cnumber);
                String cname = null;
                if (project.getCName() != null) {
                    cname = project.getCName();
                }
                row.createCell(5).setCellValue(cname);
                String signatory = null;
                if (project.getSignatory() != null) {
                    signatory = project.getSignatory();
                }
                row.createCell(6).setCellValue(signatory);
                java.sql.Date sdate = null;
               String ssdate=null;
                if (project.getSDate() != null) {
                    sdate = project.getSDate();
                    ssdate=sdate.toString();
                }
                row.createCell(7).setCellValue(ssdate);
                Integer smoney =null;
                if (project.getSMoney() != null) {
                    smoney = project.getSMoney();
                }
                row.createCell(8).setCellValue(smoney);
                String pmanager = null;
                if (project.getPManager() != null) {
                    pmanager = project.getPManager();
                }
                row.createCell(9).setCellValue(pmanager);
            }
            // 第七步，将文件输出到客户端浏览器
            try {

                workbook.write(out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("文件输出失败！");
        }
    }
}