package com.ljz.demo4.controller;

import com.ljz.demo4.entity.Question;
import com.ljz.demo4.entity.Teacher;
import com.ljz.demo4.entity.Testpaper;
import com.ljz.demo4.repository.GradeRepo;
import com.ljz.demo4.repository.QuestionRepository;
import com.ljz.demo4.repository.TeacherRepository;
import com.ljz.demo4.repository.TestpaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/tea")
public class TeacherController {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    TestpaperRepository testpaperRepository;
    @Autowired
    GradeRepo classesRepository;
@Autowired
TeacherRepository teacherRepository;


    @PostMapping("/commitquestions")
    String commitQuestion
            (
            Model model,
    @RequestParam("qname") String qname,
    @RequestParam("content") String content,
    @RequestParam("inputrule") String inputrule,
    @RequestParam("outputrule") String outputrule,
    @RequestParam("inputexample") String inputexample,
    @RequestParam("outputexample") String outputexample,
    @RequestParam("rating") String rating
                          )
    {
        Question question =new Question();
        question.setQname(qname);
        question.setContent(content);
        question.setInputrule(inputrule);
        question.setOutputrule(outputrule);
        question.setInputexample(inputexample);
        question.setOutputexample(outputexample);
        question.setRating(rating);
        questionRepository.save(question);
        model.addAttribute("msg","成功");
        model.addAttribute("questions", questionRepository.findAll());
        return "Teacher/questionStatus";
    }


    @PostMapping("/commitpapers")
    String commitPapers
            (
            Model model,
    @RequestParam("testname") String testname,
    @RequestParam("cno") Integer cno,
    @RequestParam("startdate") String startdate,
    @RequestParam("enddate") String enddate,
    @RequestParam("testtime") String testtime,


    @RequestParam("qno_0") Integer qno_0,
    @RequestParam("qscore_0") Double qscore_0,

                @RequestParam("qno_1") Integer qno_1,
    @RequestParam("qscore_1") Double qscore_1,

                @RequestParam("qno_2") Integer qno_2,
    @RequestParam("qscore_2") Double qscore_2,

            @RequestParam("qno_3") Integer qno_3,
    @RequestParam("qscore_3") Double qscore_3,

                @RequestParam("qno_4") Integer qno_4,
    @RequestParam("qscore_4") Double qscore_4,
            HttpSession session

                          )
    {

        if (classesRepository.findByGid(cno) == null){
            model.addAttribute("msg","班级号不存在或格式错误");
        }
        if (questionRepository.findByQno(qno_0) == null){
            model.addAttribute("msg","第一题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }
        if (questionRepository.findByQno(qno_1) == null){
            model.addAttribute("msg","第二题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }
        if (questionRepository.findByQno(qno_2) == null){
            model.addAttribute("msg","第三题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }
        if (questionRepository.findByQno(qno_3) == null){
            model.addAttribute("msg","第四题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }if (questionRepository.findByQno(qno_4) == null){
            model.addAttribute("msg","第五题题目不存在或格式错误");
            return "Teacher/commitpaper";
        }


        Testpaper testpaper = new Testpaper();
        testpaper.setTestname(testname);
        Teacher teacher = (Teacher) session.getAttribute("Teacher");
        testpaper.setTno(teacher.getTno());
        testpaper.setGid(cno);
        testpaper.setStartdate(java.sql.Date.valueOf(startdate));
        testpaper.setEnddate(java.sql.Date.valueOf(enddate));
        testpaper.setTesttime(java.sql.Time.valueOf(testtime+":00"));

        testpaper.setQno_0(qno_0);
        testpaper.setQscore_0(qscore_0);

        testpaper.setQno_1(qno_1);
        testpaper.setQscore_1(qscore_1);

        testpaper.setQno_2(qno_2);
        testpaper.setQscore_2(qscore_2);

        testpaper.setQno_3(qno_3);
        testpaper.setQscore_3(qscore_3);

        testpaper.setQno_4(qno_4);
        testpaper.setQscore_4(qscore_4);

        Testpaper newtestpaper = testpaperRepository.save(testpaper);
//        System.out.println(newtestpaper);
        model.addAttribute("msg","成功");
//        model.addAttribute("testpaper", testpaperRepository.findByTno(newtestpaper.getTno()));
        return "Teacher/paperStatus";
    }



    //查看题目详细内容
    @GetMapping("/content/{qno}")
    String content(@PathVariable(value = "qno") Integer qno
            , Model model) {
        model.addAttribute("question", questionRepository.findByQno(qno));
        return "Teacher/question";
    }



    @PostMapping("/repwd")
    String repairpwd(Model model,
                     @RequestParam("opttype") String opttype,
                     @RequestParam("pwd1") String pwd1,
                     @RequestParam("pwd2") String pwd2,
                     HttpSession session
    ){
        if (pwd1.equals(pwd2)){
            Teacher tea = (Teacher) session.getAttribute("Teacher");
            String hashed = BCrypt.hashpw(pwd1, BCrypt.gensalt());
            tea.setPassword(hashed);
            teacherRepository.save(tea);
            model.addAttribute("msg","成功修改密码为"+pwd1);
        }else {

            model.addAttribute("msg","两次输入的密码不相同，请重新输入");
        }

        return "Teacher/repairpwd";
    }
}

