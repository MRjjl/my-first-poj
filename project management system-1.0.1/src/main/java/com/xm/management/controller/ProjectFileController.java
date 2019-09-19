package com.xm.management.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xm.management.service.ProjectFileService;
import com.xm.management.service.impl.ProjectFileServiceImpl;
import com.xm.management.utils.ExcelImportUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.Random;
import java.util.UUID;

@RestController
public class ProjectFileController {
    @Autowired
    ProjectFileService projectFileService;
    ProjectFileServiceImpl projectFileServiceImpl = new ProjectFileServiceImpl();

    /*
     * 接收上传文件
     * 调用ProjectFileService完成excel解析
     * */
    @RequestMapping(value = "/project/uploadfile")
    @ResponseBody
    public String batchImportUserKnowledge(@RequestParam("uploadfile") MultipartFile file,
                                           // HttpServletRequest request, HttpServletResponse response,
                                           HttpSession session) throws IOException {

        System.out.println("开始判断文件");
        //判断文件是否为空
        if (file == null) {
            session.setAttribute("msg", "文件不能为空！");
            return "文件为空，请重新选择";
        }

        //获取文件名
        String fileName = file.getOriginalFilename();

        //验证文件名是否合格
        if (!ExcelImportUtils.validateExcel(fileName)) {
            session.setAttribute("msg", "文件必须是excel格式！");
            return "格式错误，请导入excel文件";
        }

        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (StringUtils.isEmpty(fileName) || size == 0) {
            session.setAttribute("msg", "文件不能为空！");
            return "excel文件为空，请重新选择";
        }

        //批量导入
        String message = projectFileService.batchImport(fileName, file, "admin");
        session.setAttribute("msg", message);
        return message;
    }

    /*
     * 上传模板下载
     * */
    @RequestMapping("/project/downfileEm")
    public String downLoadEmFile(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "templatefile.xlsx";
        if (fileName != null) {
            // 当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
            // String realPath =
            // request.getServletContext().getRealPath("//WEB-INF//");
            String realPath = "F://files//";
            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    /*
     * project文件导出
     * */
    @RequestMapping(value = "/project/downfile")
    @ResponseBody
    public String dowmLoadFile(HttpServletRequest request, HttpServletResponse response) {
        String name = "project";
        // response.setContentType("application/binary;charset=UTF-8");
        try {
            ServletOutputStream out = response.getOutputStream();
            try {
                //设置文件头：最后一个参数是设置下载文件名
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(name + ".xls", "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String[] titles = {"pnumber", "pname", "businessunit", "salesmanager", "cnumber", "cname", "signatory", "sdate", "smoney"};
            System.out.println("已经设置好titles");
            projectFileService.batchExport(titles, out);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "导出信息失败";
        }
    }


    /*
     * word文件下载
     * */
    @RequestMapping(value = "/project/downword")
    @ResponseBody
    public String dowmWord(HttpServletRequest request, HttpServletResponse response) {
        String name = "random";
        try {
            ServletOutputStream out = response.getOutputStream();
            try {
                //设置文件头：最后一个参数是设置下载文件名
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(name + ".doc", "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            // Random rdm = new Random(1000000000);
            String T=UUID.randomUUID().toString();
            XWPFDocument doc = new XWPFDocument(); //创建word文件
            XWPFParagraph p1 = doc.createParagraph(); //创建段落
            XWPFRun r1 = p1.createRun(); //创建段落文本
            r1.setText(T); //设置文本
            // FileOutputStream out = new FileOutputStream("D:\\simple.docx"); //创建输出流
            doc.write(out);  //输出
            out.close();  //关闭输出流
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "下载文件失败";
        }
    }


//文件保存至服务器
/*package com.xm.management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
public class ProjectFileController {
    //文件上传
    @RequestMapping(value = "/project/uploadfile")
    @ResponseBody
    public String upload(@RequestParam("uploadfile") MultipartFile file) {
        if (file.isEmpty()) {
            return "文件为空";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        System.out.println("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        System.out.println("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = "F://files//";
        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return "上传成功";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }*/
}
