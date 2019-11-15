package com.ljz.demo4.controller;

import com.ljz.demo4.entity.Question;
import com.ljz.demo4.entity.Sscore;
import com.ljz.demo4.entity.Student;
import com.ljz.demo4.entity.Testpaper;
import com.ljz.demo4.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
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

    @Autowired
    SscoreRepo sscoreRepo;
    @GetMapping("/question")
    String searchquestion(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "Student/seachquestion";
    }

    //查看题目详细内容
    @GetMapping("/content/{qno}")
    String content(@PathVariable(value = "qno") Integer qno
            , Model model) {
        model.addAttribute("question", questionRepository.findByQno(qno));
        return "Student/question";
    }

    @GetMapping("/testpaper")
    String searchtestpaper(Model model,HttpSession session) {

        Student student = (Student) session.getAttribute("Student");
        model.addAttribute("testpapers", testpaperRepository.findAll());
        return "Student/searchtestpaper.html";
    }

    @GetMapping("/exit")
    String exittest(Model model,HttpSession session){
        //保存试卷
        String [] code = (String[]) session.getAttribute("codelist");
        String [] content = (String[]) session.getAttribute("contentlist");
        Integer testno = (Integer) session.getAttribute("testno");
        Student student = (Student) session.getAttribute("Student");
        Sscore sscore = new Sscore();
        sscore.setCode_0(code[0]);
        sscore.setCode_1(code[1]);
        sscore.setCode_2(code[2]);
        sscore.setCode_3(code[3]);
        sscore.setCode_4(code[4]);
        sscore.setContent_0(content[0]);
        sscore.setContent_1(content[1]);
        sscore.setContent_2(content[2]);
        sscore.setContent_3(content[3]);
        sscore.setContent_4(content[4]);
        sscore.setTestno(testno);
        sscore.setSno(student.getSno());
        Sscore s = sscoreRepo.save(sscore);
        model.addAttribute("testpapers", testpaperRepository.findAll());
        session.setAttribute("sscores",sscoreRepo.findBySno(student.getSno()));

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


    String compile(String code) throws FileNotFoundException {
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
        return output;
    }

    @PostMapping("/runcode")
    String runcode(Model model,
                   @RequestParam("textareaCode") String code,
                   HttpSession session) throws FileNotFoundException {
        model.addAttribute("code",code);
        model.addAttribute("msg",compile(code));
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
    String testcontent(@PathVariable(value = "testno") Integer testno,Model model,HttpSession session){

        Student student = (Student)session.getAttribute("Student");
        if (sscoreRepo.findByTestnoAndSno(testno,student.getSno())!=null){
            model.addAttribute("msg","你已参加了考试");
            return searchtestpaper(model, session);
        }
        Testpaper testpaper = testpaperRepository.findByTestno(testno);
        Question q_0 = questionRepository.findByQno(testpaper.getQno_0());
        session.setAttribute("q_0",q_0);
        Question q_1 = questionRepository.findByQno(testpaper.getQno_1());
        session.setAttribute("q_1",q_1);
        Question q_2 = questionRepository.findByQno(testpaper.getQno_2());
        session.setAttribute("q_2",q_2);
        Question q_3 = questionRepository.findByQno(testpaper.getQno_3());
        session.setAttribute("q_3",q_3);
        Question q_4 = questionRepository.findByQno(testpaper.getQno_4());
        session.setAttribute("q_4",q_4);
        String[] code = new String[5];
        String[] content = new String[5];
        for (int i = 0;i<5;i++) {
            code[i] = "import java.sql.*;\n" +
                    " public class Test {\n" +
                    "\tpublic static void hello(){\n" +
                    "\t\tSystem.out.println(\"hello \");\n" +
                    "\t}\n" +
                    "}";
            content[i] ="";
        }
        session.setAttribute("codelist",code);
        session.setAttribute("contentlist",content);
        session.setAttribute("testno",testno);

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

    @PostMapping("/test_runcode/{qno}")
    String savecode(
            @PathVariable(value = "qno") Integer qno,
            @RequestParam("textareaCode") String textareaCode,HttpSession session
    ) throws FileNotFoundException {
        String[] newcode = (String[]) session.getAttribute("codelist");
        String[] newcontent = (String[]) session.getAttribute("contentlist");
        //更新代码
        switch (qno){
            case 1:
                newcontent[0] = compile(textareaCode);
                newcode[0] = textareaCode;
                break;
            case 2:
                newcontent[1] = compile(textareaCode);
                newcode[1] = textareaCode;
                break;
            case 3:
                newcontent[2] = compile(textareaCode);
                newcode[2] = textareaCode;
                break;
            case 4:
                newcontent[3] = compile(textareaCode);
                newcode[3] = textareaCode;
                break;
            case 5:
                newcontent[4] = compile(textareaCode);
                newcode[4] = textareaCode;
                break;
        }
        session.setAttribute("contentlist",newcontent);
        session.setAttribute("codelist",newcode);
        return save(qno);
    }



    @GetMapping("/lookscore")
    String lookscore(Model model,HttpSession session){
        Student student = (Student) session.getAttribute("Student");
        List<Sscore> sscores = sscoreRepo.findBySno(student.getSno());
        model.addAttribute("sscores",sscores);
        return "Student/lookscore";
    }
}