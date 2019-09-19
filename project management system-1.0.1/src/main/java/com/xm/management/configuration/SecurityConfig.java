package com.xm.management.configuration;

import com.xm.management.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
// @RequestMapping("/user")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    UserDetailsService customUserService() { // 注册UserDetailsService 的bean
        return new CustomUserDetailsService();
    }

    // 实现密码的加密
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Override
    //重写configure(HttpSecurity http)的方法，这里面来自定义自己的拦截方法和业务逻辑
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //自定义配置
        httpSecurity.authorizeRequests()
                .antMatchers("/js/**", "/css/**", "/images/*", "/fonts/**", "/**/*.png", "/**/*.jpg").permitAll()
                .antMatchers("/", "/login", "/signin").permitAll()
                .antMatchers("/project/all", "/project", "/project/update","/project/downword","/project/downfile","/project/downfileEm","/project/uploadfile","/uploadfile","/sysLog","sysLog/findAll","/sysLog","/findAll").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/projectsystempage/loginerror.html")
                .defaultSuccessUrl("/projectsystempage/homepage.html")
                .permitAll()
                .and()
                .rememberMe().rememberMeParameter("remember-me") //其实默认就是remember-me，这里可以指定更换
                .and()
                .logout()
                .logoutSuccessUrl("/projectsystempage/loginpage.html")  //退出登录
                .permitAll()
                .and()
                .csrf().disable();
    }



    // 密码加密
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
    }
}