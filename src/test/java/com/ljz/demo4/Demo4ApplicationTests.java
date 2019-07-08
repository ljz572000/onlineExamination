package com.ljz.demo4;

import com.ljz.demo4.controller.BCrypt;
import com.ljz.demo4.entity.*;
import com.ljz.demo4.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Demo4ApplicationTests {

    @Autowired
    GradeRepo gradeRepo;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuesStatusRepo quesStatusRepo;
    @Test
    public void test() {
        Grade grade = new Grade();
        grade.setGid(2016051);
        grade.setGname("计算机051班");
        gradeRepo.save(grade);
    }
    @Test
    public void test2() {
        //添加管理员
        Admin admin = new Admin();
        admin.setAno(20160750);
        String hashed = BCrypt.hashpw("20160750", BCrypt.gensalt());
        admin.setPassword(hashed);
        admin.setAname("李金洲");
        admin.setPhone("88240848");
        adminRepo.save(admin);
           Admin admin2 = adminRepo.findByAno(20160750);
        System.out.println(admin2);

    }

}
