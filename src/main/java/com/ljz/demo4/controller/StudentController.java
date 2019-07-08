package com.ljz.demo4.controller;

import com.ljz.demo4.entity.Student;
import com.ljz.demo4.repository.QuesStatusRepo;
import com.ljz.demo4.repository.QuestionRepository;
import com.ljz.demo4.repository.StudentRepository;
import com.ljz.demo4.repository.TestpaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/stu")
public class StudentController {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    TestpaperRepository testpaperRepository;
    //当用户点击在线练习时，自动搜索试题
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    QuesStatusRepo questionstatusRepository;
    @GetMapping("/question")
    String searchquestion(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "Student/seachQuestion";
    }

    //查看题目详细内容
    @GetMapping("/content/{qno}")
    String content(@PathVariable(value = "qno") Integer qno
            , Model model) {
        model.addAttribute("question", questionRepository.findByQno(qno));
        return "Student/question";
    }

    @GetMapping("/testpaper")
    String searchtestpaper(Model model) {
        model.addAttribute("testpapers", testpaperRepository.findAll());
        return "Student/searchtestpaper.html";
    }

    @PostMapping("/repwd")
    String repwd(@RequestParam("opttype") String opttype,
                 @RequestParam("pwd1") String pwd1,
                 @RequestParam("pwd2") String pwd2,
                 Map<String, Object> map, HttpSession session) {

        if (opttype.equals("repair")) {
            if (pwd1.equals(pwd2)) {

                Student student = (Student) session.getAttribute("Student");
                String hashed = BCrypt.hashpw(pwd1, BCrypt.gensalt());
                student.setPassword(hashed);
                studentRepository.save(student);
                map.put("msg", "成功");
            } else {
                map.put("msg", "密码不匹配");
            }
        }
        return "Student/repairpwd";
    }



    @PostMapping("/runcode")
    String runcode(Model model,
                   @RequestParam("textareaCode") String code,
                   HttpSession session) throws FileNotFoundException {
        DynamicCompile dynamicCompile =new DynamicCompile();
        //开始编译



        dynamicCompile.compile(code);




        String output = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader("output.txt"));
            StringBuffer sb;
            while (in.ready()) {
                sb = (new StringBuffer(in.readLine()));
                output = output+"<br>"+sb;
            }
            in.close();
        } catch (IOException e) {
        }

model.addAttribute("code",code);

        model.addAttribute("msg",output);
        return "Student/onlinePratical";

    }

//    @ExceptionHandler()
//    String code(Model model,Exception e)
//    {
//        model.addAttribute("msg",e.toString());
//        return "Student/onlinePratical";
//
//    }


    @GetMapping("/starttest/{testno}")
    String testcontent(@PathVariable(value = "testno") Integer testno,Model model){
        return "Student/onlineTest";
    }


    @GetMapping("/save/{qno}")
    String save(@PathVariable (value = "qno") Integer qno){
        switch (qno){
            case 1:
                return "Student/onlineTest";
            case 2:
                return "Student/onlineTestqno_2";
            case 3:
                return "Student/onlineTestqno_3";
            case 4:
                return "Student/onlineTestqno_4";
            case 5:
                return "Student/onlineTestqno_5";
                default:
                    return "Student/onlineTest";
        }
    }



}