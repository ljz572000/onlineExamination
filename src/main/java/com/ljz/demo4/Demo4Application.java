package com.ljz.demo4;

import com.ljz.demo4.controller.AdminController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan({"com.ljz.demo4","controller"})
public class Demo4Application {

    public static void main(String[] args) {
        new File(AdminController.uploadDirectory).mkdir();
        SpringApplication.run(Demo4Application.class, args);
    }

}
