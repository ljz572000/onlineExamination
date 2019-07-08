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
        Questionstatus questionstatus = new Questionstatus();

        questionstatus.setQno(questionRepository.findByQno(1).getQno());
        questionstatus.setSno(studentRepository.findBySno(20160750).getSno());

        questionstatus.setDone(true);
        questionstatus.setCode("code");
        questionstatus.setContent("content");
        quesStatusRepo.save(questionstatus);
        System.out.println(quesStatusRepo.findAll());
    }
    @Test
    public void test2() {
        //添加管理员
//        Admin admin = new Admin();
//        admin.setAno(20160750);
//        String hashed = BCrypt.hashpw("20160750", BCrypt.gensalt());
//        admin.setPassword(hashed);
//        admin.setAname("李金洲");
//        admin.setPhone("88240848");
//        adminRepo.save(admin);
           Admin admin = adminRepo.findByAno(20160750);
        System.out.println(admin);

    }

}
