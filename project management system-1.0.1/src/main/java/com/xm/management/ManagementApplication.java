package com.xm.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@MapperScan("com.xm.management.dao")
public class ManagementApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
        System.out.println("启动成功");
    }
    //文件上传大小限制配置
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("204800KB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("204800KB");

        return factory.createMultipartConfig();
    }

    //解决跨域问题-CORS"跨域资源共享"
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")  //可以被跨域的路径
                .allowCredentials(true)
                .allowedHeaders("*")    //允许的请求header
                .allowedOrigins("*")    //允许的请求域名
                .allowedMethods("*");    //允许的请求方法

    }

}

