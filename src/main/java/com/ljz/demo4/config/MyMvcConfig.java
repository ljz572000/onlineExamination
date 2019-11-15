package com.ljz.demo4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index").setViewName("login");
        registry.addViewController("/home").setViewName("login");
        registry.addViewController("/student/index").setViewName("Student/index");
        registry.addViewController("/student/repairpwd").setViewName("Student/repairpwd");
        registry.addViewController("/Student/seachquestion").setViewName("Student/seachquestion.html");
        registry.addViewController("/student/onlinePratical").setViewName("Student/onlinePratical.html");
        registry.addViewController("/student/question").setViewName("Student/question.html");
        registry.addViewController("/admin/index").setViewName("Admin/index");
        registry.addViewController("/admin/bath").setViewName("Admin/bath");
        registry.addViewController("/admin/reset").setViewName("Admin/reset");
        registry.addViewController("/admin/repairpwd").setViewName("Admin/repairpwd");
        registry.addViewController("/admin/registered").setViewName("Admin/registered");
        registry.addViewController("/tea/index").setViewName("Teacher/index");
        registry.addViewController("/tea/commitpaper").setViewName("Teacher/commitpaper");
        registry.addViewController("/tea/commitquestion").setViewName("Teacher/commitquestion");
//        registry.addViewController("/tea/lookpaper").setViewName("Teacher/lookpaper");
        registry.addViewController("/tea/lookscore").setViewName("Teacher/lookscore");
//        registry.addViewController("/tea/scoreexport").setViewName("Teacher/scoreexport");
        registry.addViewController("/tea/repairwd").setViewName("Teacher/repairpwd");
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/", "/index", "/home", "/login");
    }
}

