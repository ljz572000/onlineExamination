package com.ljz.demo4.controller;

import com.ljz.demo4.entity.Admin;
import com.ljz.demo4.entity.Student;
import com.ljz.demo4.entity.Teacher;
import com.ljz.demo4.repository.AdminRepo;
import com.ljz.demo4.repository.StudentRepository;
import com.ljz.demo4.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class Login {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    AdminRepo adminRepository;

    @PostMapping(value = "/login")
    public String login(
            @RequestParam("id") String id,
            @RequestParam("password") String password,
            @RequestParam("user_type") String user_type,
            Map<String, Object> map,
            HttpSession session) {


        if (id == "" || password == "") {
            map.put("msg", "用户名密码为空");
            return "login";
        }

        if (user_type.equals("students")) {

            Student stu = studentRepository.findBySno(Integer.parseInt(id));
            if (stu == null){
                map.put("msg", "输入的用户不存在");
                return "login";
            }
            if (BCrypt.checkpw(password, stu.getPassword())) {
                session.setAttribute("user_type", user_type);
                session.setAttribute("Student", stu);
                return "redirect:/student/index";
            } else {

                map.put("msg", "输入的用户不存在");
                return "login";
            }


        } else if (user_type.equals("teachers")) {
            Teacher tea = teacherRepository.findByTno(Integer.parseInt(id));
            if (tea == null){
                map.put("msg", "输入的用户不存在");
                return "login";
            }

            if (BCrypt.checkpw(password,tea.getPassword())){
                session.setAttribute("user_type", user_type);
                session.setAttribute("Teacher", tea);
                return "redirect:/tea/index";
            }else {
                map.put("msg", "输入的账号，密码有误");
                return "login";
            }



        } else {


            Admin admin = adminRepository.findByAno(Integer.parseInt(id));

            if (admin == null){
                map.put("msg", "输入的用户不存在");
                return "login";
            }
            if (BCrypt.checkpw(password,admin.getPassword())){
                session.setAttribute("user_type", user_type);
                session.setAttribute("Admin", admin);
                return "redirect:/admin/index";
            }else {
                map.put("msg", "输入的账号，密码有误");
                return "login";
            }

        }

    }
}
