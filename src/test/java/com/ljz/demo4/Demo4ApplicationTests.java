package com.ljz.demo4;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.ljz.demo4.controller.BCrypt;
import com.ljz.demo4.entity.*;
import com.ljz.demo4.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    TestpaperRepository testpaperRepository;
    @Autowired
    SscoreRepo sscoreRepo;


    @Test
    public void test3() {
        Testpaper testpaper = testpaperRepository.findByTestno(1);
        Question q_0 = questionRepository.findByQno(testpaper.getQno_0());
        Question q_1 = questionRepository.findByQno(testpaper.getQno_1());
        Question q_2 = questionRepository.findByQno(testpaper.getQno_2());
        Question q_3 = questionRepository.findByQno(testpaper.getQno_3());
        Question q_4 = questionRepository.findByQno(testpaper.getQno_4());
    }
    @Test
    public void test4() {
        List<Sscore> sscores = sscoreRepo.findAll();
        System.out.println(sscores);
    }
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

    @Test
    public void writeV2007() throws IOException {

        OutputStream out = new FileOutputStream("2007.xlsx");
        ExcelWriter writer = EasyExcelFactory.getWriter(out);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet1 = new Sheet(1, 3);
        sheet1.setSheetName("第一个sheet");

        //设置列宽 设置每列的宽度
        Map columnWidth = new HashMap();
        columnWidth.put(0,1000);columnWidth.put(1,5000);columnWidth.put(2,1000);columnWidth.put(3,1000);
        sheet1.setColumnWidthMap(columnWidth);
        sheet1.setHead(createTestListStringHead());
        //or 设置自适应宽度
        //sheet1.setAutoWidth(Boolean.TRUE);
        writer.write1(createTestListObject(1), sheet1);
        
        writer.finish();
        out.close();

    }

    public static List<List<String>> createTestListStringHead(){
        //写sheet3  模型上没有注解，表头数据动态传入
        List<List<String>> head = new ArrayList<List<String>>();
        List<String> headCoulumn1 = new ArrayList<String>();
        List<String> headCoulumn2 = new ArrayList<String>();
        List<String> headCoulumn3 = new ArrayList<String>();

        headCoulumn1.add("考试号");
        headCoulumn2.add("学生学号");
        headCoulumn3.add("考试分数");

        head.add(headCoulumn1);
        head.add(headCoulumn2);
        head.add(headCoulumn3);
        return head;
    }
    public List<List<Object>> createTestListObject(Integer testno) {
        List<List<Object>> object = new ArrayList<List<Object>>();
        List<Sscore> sscores = sscoreRepo.findByTestno(testno);
        for (Sscore s:
             sscores) {
            List<Object> da = new ArrayList<Object>();
            da.add(s.getTestno());
            da.add(s.getSno());
            da.add(s.getScore());
            object.add(da);
        }

        return object;
    }

}
